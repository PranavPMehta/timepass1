
//Importing Libraries
const routes = require('express').Router();

//Importing Models
var MeetingGroup = require('../../model/others/meeting_group');
var Task = require('../../model/others/task_information');
var group_info=require('../..//model/group_text_chat/group_information');
var group_messages=require('../..//model/group_text_chat/group_messages');
var messages=require('../..//model/one_to_one_text_chat/personal_chat');
//Demo Route to Check Connection
routes.get('/CheckOther', (req, res) => {
    res.status(200).json({ message: 'Other Connected!' });
});

routes.post('/saveMessage',async (req,res)=>{
    chat_id1 = req.body.sender_id+"_"+req.body.receiver_id
    chat_id2 = req.body.receiver_id+"_"+req.body.sender_id
    ref=req.body.sender_id+'_'+req.body.receiver_id
    console.log("Data tyope:"+req.body.n_msg)
    console.log("Data tyope:"+req.body.type1)
    await messages.PersonalChat.findOne({
        "$or": [{
            "chat_id": chat_id1
        }, {
            "chat_id": chat_id2
        }]
    },async function(err,result){
         if(!result){  
                var insertJson = new messages.PersonalChat({
                user_1: req.body.sender_id,
                user_2: req.body.receiver_id,
                chat_id: req.body.sender_id+'_'+req.body.receiver_id,
                seqno: 0,
                chat: [{id: ref+'_0',
                    timestamp: new Date(),
                    user_id: req.body.sender_id,
                    status: 'success',
                    location: req.body.loc,
                    reference_id: '' ,
                        msg : {
                            data_type:req.body.type1,
                            data: req.body.n_msg, 
                            key_1: req.body.key_1, 
                            key_2: req.body.key_2
                        }
                       }]
            });
            r = await insertJson.save();
            if(r) res.send(ref+'_0')
         }
         else
         {
            counter = result.seqno+1;
            ref=ref+'_'+counter;
            await messages.PersonalChat.update({_id:result._id}, {$set: { seqno: counter }})
            chat1 = new messages.PersonalMessage1({
                id: ref,
                timestamp: new Date(),
                user_id: req.body.sender_id,
                status: 'success',
                location: req.body.loc,
                reference_id: '' ,
                msg : {data_type:req.body.type1,data: req.body.n_msg, key_1: req.body.key_1, key_2: req.body.key_2}
            });                        
            result.chat.push(chat1);
            r = await result.save()
             console.log("chats"+chat1);
             console.log("qwert"+r);
            if(r) res.send(ref)
        }           
});

})


routes.post("/createMeeting", (req, res) => {
    const presentation_id = req.body.presentation_id;
    const agenda = req.body.agenda;
    const members = req.body.members;
    const date = req.body.date;
    const time = req.body.time;
    mem = []
    console.log(presentation_id);
    console.log(agenda);
    console.log(members);
    console.log(date);
    console.log(time);
    const mem_arr = members.split(',');
    for (i in mem_arr){
        var mem1 =  new MeetingGroup.GroupMember({
            username : mem_arr[i]
        });
        mem.push(mem1);
    }
    console.log("Mem : "+mem);
    var meet = new MeetingGroup.MeetingGroup({
        presentation_id: presentation_id,
        agenda : agenda,
        members: mem,
        creation_timestamp: "Date : " + date + " Time :" + time,
    });
    meet.save(function(err) {
        if (err){ 
            res.json({"message":"Meeting Info not saved successfuly","result":"false"});
            throw err
        };
        console.log('Meeting Info saved successfully!');
        res.json({"message":"Meeting Info saved successfuly","result":"true"});
    })
    
});

routes.post("/createTask", (req, res) => {
    var for_mem = req.body.for_mem;
    var title = req.body.title;
    var desc = req.body.desc;
    const mem_arr = for_mem.split(',');
    mem=[]
    for (i in mem_arr){
        mem1 = {user: mem_arr[i].trim()}
        mem.push(mem1);
    }
    var task = new Task({
        meeting_id:"rr7",
        for_whom : mem,
        title : title,
        description : desc
    })
    task.save(function(err) {
        if (err){ 
            res.json({"message":"Task did not create successfuly","result":"false"});
            throw err
        };
        console.log('Task created successfully!');
        res.json({"message":"Task created successfuly","result":"true"});
    })
    //res.send(task);
    //res.json({'message' : 'Recived','result':'true'});

    
});

routes.get('/getMember',(req,res)=>{
    console.log(req.query);
    MeetingGroup.MeetingGroup.find({},function(err,result1){
        console.log("Result : "+result1[0].members);
        res.send(result1[0].members);
    });
});

routes.get('/getTask',(req,res)=>{
    console.log(req.query);
    Task.find({},function(err,result1){
        console.log("Result : "+result1);
        var js_st = JSON.stringify(result1);
        var js_ob = JSON.parse(js_st);
        /*console.log("JSON String : "+js_st);
        console.log("JSON Object : "+js_ob);*/
        res.json({"message":js_st,"result":"true"});
        //res.send(js_ob);
        
    });
});

routes.post('/getToken',(req,res)=>{
    try{
        console.log(req.body);
        var user1 = req.body.user;
        console.log(user1);
    
        User.findOne({'Username':user1},function(err,user){
            if(err){
                res.json({"message":"Unable to send Notification to "+user1,"result":"false"});    
            }
            res.json({"message":user.notification_token,"result":"true"});
        });
    }
    catch(e){
        //console.log(e);
        
        res.json({"message":"Unable to send Notification to "+user1,"result":"false"});    
        callback(new Error('something bad happened'));

    }
});

module.exports = routes;