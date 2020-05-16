const routes = require('express').Router();

var messages=require('../..//model/one_to_one_text_chat/personal_chat');
var grp_messages = require('../../model/group_text_chat/group_messages');
routes.get('/getChat',(req,res)=>{
    chat_id1 = req.query.sender_id+'_'+req.query.receiver_id
    chat_id2 = req.query.receiver_id+'_'+req.query.sender_id
    messages.PersonalChat.findOne({
        "$or": [{
            "chat_id": chat_id1
        }, {
            "chat_id": chat_id2
        }]
    },function(err,docs){
    
     if(err) throw err;
     var chats =""
     var cc = docs.chat;
     var output = cc.filter(function(x){return x.is_deleted=="false"}); //arr here is you result array
     var output1 = output.filter(function(x){return x.isVault==false});
     //console.log(output1)
     //console.log(output)
    // if(cc) chats=JSON.stringify(cc);
     if(output1) chats=JSON.stringify(output1);
   
         res.json({"message":chats,"result":"true"});
 });
 });

 routes.get('/getGroupChat',(req,res)=>{
    grp_messages.GroupChat.findOne({group_id:req.query.group_id},function(err,docs){
    
     if(err) throw err;
     var chats =JSON.stringify([]);
     if(docs) chats=JSON.stringify(docs.gmessages);
     //console.log(chats);
         res.json({"message":chats,"result":"true"});
 });
 });

     
routes.post('/setReceived',async (req,res)=>{
    console.log("Request received"+req.body.msg_id);
    messages.PersonalChat.updateOne({'chat.id':req.body.msg_id},{$set:{"chat.$.isReceived":true}},
        async function(err,result){
            if(err)
                throw err;
            console.log(result);
        //    res.send("OK");
            res.json({'message':'OK','result':'true'});

        });
});
    
routes.get('/checkReceived',async(req,res)=>{
//       console.log("CAlled"+req.query.msg_id); 
//        messages.PersonalChat.findOne({"chat.reference_id":req.query.msg_id},await function(err,docs){
//            if(err) throw err;
//            if(docs){
//            var chats=JSON.stringify(docs.chat.$);
//                console.log("CHATSSS"+docs.chat.$); //hey undefined yet.chat
//            res.json({"message":chats,"result":true});}
    console.log("RID"+req.query.msg_id);
    result = await messages.PersonalChat.findOne({'chat.reference_id':req.query.msg_id,'chat.isReceived':'true'})
    if(result)
        res.json({"result":true});
    else
        res.json({"result":false});

    });

routes.post('/forwardMessage',(req,res)=>{
    console.log("Forward Message");
    var msg = req.body.message;
    //db.collection.find({})
    messages.PersonalChat.find({},function(e,rlt){
        if(e){
            res.json({"message":e,"result":false});
        }
        else{
            res.json({"message":rlt,"result":true});
        }
    })
})

routes.post('/deleteMessage',async (req,res)=>{
    console.log("Delete Message");
    var reference_id = req.body.reference_id;
    console.log("Reference_ID : "+reference_id);
    await messages.PersonalChat.findOne({'chat.id':reference_id},{"chat.$":1},async function(err,docs){
        if(err){
            res.json({"message":"Failed","result":false});
            return;
        }
        var chat_id =docs.chat[0]._id;
        //console.log(docs.chat[0]._id)
        await messages.PersonalChat.findOneAndUpdate(
            {"chat._id": chat_id },
            { 
                "$set": {
                    "chat.$.is_deleted": "true"
                    //"chat.$.status": "success"
                }
            },
            function(err,doc) {
                if(err){
                    console.log(err);
                    res.json({"message":"Failed","result":false});
                }
                console.log(doc);
                res.json({"message":"Deleted","result":true});
            }
        );
        
    });
//    console.log(result);
//    res.send(result);
});

routes.post('/setImportant',async (req,res)=>{
    console.log("Important Message");
    var reference_id = req.body.reference_id;
    //reference_id = "rksskr1";
    console.log("Reference_ID : "+reference_id);
    await messages.PersonalChat.findOne({'chat.id':reference_id},{"chat.$":1},async function(err,docs){
        if(err){
            res.json({"message":"Failed","result":false});
            return;
        }
        var chat_id =docs.chat[0]._id;
        console.log(docs.chat[0]._id)
        await messages.PersonalChat.findOneAndUpdate(
            {"chat._id": chat_id },
            { 
                "$set": {
                    "chat.$.is_important": "true"
                    //"chat.$.status": "success"
                }
            },
            function(err,doc) {
                if(err){
                    console.log(err);
                    res.json({"message":"Failed","result":false});
                }
                //console.log(doc);
                res.json({"message":"Marked Important","result":true});
            }
        );
    });
//    console.log(result);
//    res.send(result);
});

routes.post('/hideMessage',async (req,res)=>{
    console.log("Hide Message");
    var reference_id = req.body.reference_id;
    //reference_id = "rksskr1";
    console.log("Reference_ID : "+reference_id);
    await messages.PersonalChat.findOne({'chat.id':reference_id},{"chat.$":1},async function(err,docs){
        if(err){
            res.json({"message":"Failed","result":false});
            return;
        }
        var chat_id =docs.chat[0]._id;
        console.log(docs.chat[0]._id)
        await messages.PersonalChat.findOneAndUpdate(
            {"chat._id": chat_id },
            { 
                "$set": {
                    "chat.$.isVault": "true"
                    //"chat.$.status": "success"
                }
            },
            function(err,doc) {
                if(err){
                    console.log(err);
                    res.json({"message":"Failed","result":false});
                }
                //console.log(doc);
                res.json({"message":"Message Moved to Vault","result":true});
            }
        );
    });
//    console.log(result);
//    res.send(result);
});

routes.post('/setSecurityLevel',async (req,res)=>{
    console.log("Set Security Level");
    var reference_id = req.body.reference_id;
    var security_level = req.body.security_level;
    //reference_id = "rksskr1";
    console.log("Reference_ID : "+reference_id);
    console.log("Security Level : "+security_level);
    var confidential = false;
    var moveToVault = false;
    if(security_level === "1"){
        confidential = true;
        moveToVault = false;
    }
    else if(security_level === "2"){
        confidential = true;
        moveToVault = true;
    }
    await messages.PersonalChat.findOne({'chat.id':reference_id},{"chat.$":1},async function(err,docs){
        if(err){
            res.json({"message":"Failed","result":false});
            return;
        }
        var chat_id =docs.chat[0]._id;
        console.log(docs.chat[0]._id)
        await messages.PersonalChat.findOneAndUpdate(
            {"chat._id": chat_id },
            { 
                "$set": {
                    "chat.$.is_confidential": confidential,
                    "chat.$.isVault": moveToVault
                    //"chat.$.status": "success"
                }
            },
            function(err,doc) {
                if(err){
                    console.log(err);
                    res.json({"message":"Failed","result":false});
                }
                //console.log(doc);
                res.json({"message":security_level + "   Security Level Set","result":true});
            }
        );
    });
//    console.log(result);
//    res.send(result);
});

routes.post('/getMessageInfo',async (req,res)=>{
    console.log("Get Message Info");
    var reference_id = req.body.reference_id;
    //reference_id = "rksskr1";
    console.log("Reference_ID : "+reference_id);
    await messages.PersonalChat.findOne({'chat.id':reference_id},{"chat.$":1},async function(err,docs){
        if(err){
            res.json({"message":"Failed","result":false});
            return;
        }
        //console.log(docs);
        var chat_id =docs.chat[0]._id;
        res.json({"message":JSON.stringify(docs.chat[0]),"result":true});
        console.log(docs.chat[0])
//        await messages.PersonalChat.findOneAndUpdate(
//            {"chat._id": chat_id },
//            { 
//                "$set": {
//                    "chat.$.security_level": security_level
//                    //"chat.$.status": "success"
//                }
//            },
//            function(err,doc) {
//                if(err){
//                    console.log(err);
//                    res.json({"message":"Failed","result":false});
//                }
//                //console.log(doc);
//                res.json({"message":"Security Level Set","result":true});
//            }
//        );
    });
//    console.log(result);
//    res.send(result);
});

routes.post('/getSSLmsg',async (req,res)=>{
    console.log("Get Message Info");
    var reference_id = req.body.reference_id;
    //reference_id = "rksskr1";
    console.log("Reference_ID : "+reference_id);
    await messages.PersonalChat.findOne({'chat.id':reference_id},{"chat.$":1},async function(err,docs){
        if(err){
            res.json({"message":"Failed","result":false});
            return;
        }
        //console.log(docs);
        var chat_id =docs.chat[0]._id;
        res.json({"message":docs.chat[0].is_confidential,"result":true});
        console.log(docs.chat[0])
    });
});

 module.exports = routes;
