const routes = require('express').Router();

var messages=require('../../model/one_to_one_text_chat/personal_chat');
var User = require('../../model/user_info/User');

routes.get('/checkThree',(req,res)=>{
         res.json({"message":"Chat Three Dots Connected","result":"true"});
 });

 routes.post('/block_user',(req,res)=>{
     console.log(req.body);
     const logged_in_user = req.body.logged_in_user;
     const user_to_be_blocked = req.body.user_to_be_blocked;
     //console.log(logged_in_user,user_to_be_blocked);
     User.findOne({'user_id':logged_in_user},function(err,result){
         if(err){
             console.log("Err"+err);
         }
         if(result){
             result.blocked_users.push({user:user_to_be_blocked});
             console.log("Result" +result)
             result.save();
             //console.log("Printing result"+result)
             //console.log("Blocked users"+result.blocked_users.user)
             res.json({"message":"User is Blocked","result":"true"});
         }
         else{
            res.json({"message":"Failed to block user","result":"false"});
         }
     });
 });

 routes.post('/unblock_user',(req,res)=>{
    //console.log(req.body);
    const logged_in_user = req.body.logged_in_user;
    const user_to_be_unblocked = req.body.user_to_be_unblocked;
    //console.log(logged_in_user,user_to_be_blocked);
    User.findOne({'user_id':logged_in_user},function(err,result){
        if(err){
            console.log("Err"+err);
        }
        if(result){
            //result.blocked_users.push({user:user_to_be_blocked});
            result.blocked_users = result.blocked_users.filter((item) => item.user !== user_to_be_unblocked);
            result.save();
            //console.log("Printing result"+result)
            //console.log("Blocked users"+result.blocked_users.user)
            res.json({"message":"User is unblocked","result":"true"});
        }
        else{
           res.json({"message":"Failed to Unblock user","result":"false"});
        }
    });
});

routes.post('/check_block_user',(req,res)=>{
    console.log(req.body);
    const logged_in_user = req.body.logged_in_user;
    const user_to_be_checked = req.body.user_to_be_checked;
    // console.log(logged_in_user,user_to_be_checked);
    User.findOne({'user_id':logged_in_user,'blocked_users.user':user_to_be_checked},function(err,result){
        // console.log(err,result)
        if(err){
            console.log("Err"+err);
            res.json({"message":"Some Error Occured","result":"false"});
        }
        if(result){
            res.json({"message":"yes","result":"true"});
        }
        else{
           res.json({"message":"no","result":"true"});
        }
    });
});

routes.post('/setSSLChat',async (req,res)=>{
    //Confidential Levels and their description.
    // 0 -> isConfidential : False and moveToVault : False
    // 1 -> isConfidential : True and moveToVault : False
    // 2 -> isConfidential : True and moveToVault : True
    var setConfidential = req.body.confidentialLevel;
    // console.log("Set COnfidential "+setConfidential)
    const logged_in_user = req.body.logged_in_user;
    const user_to_be_checked = req.body.user_to_be_checked;
    // db.inventory.find ( { quantity: { $in: [20, 50] } } )
    await messages.PersonalChat.findOne({'user_1':{$in:[logged_in_user,user_to_be_checked]},'user_2':{$in:[logged_in_user,user_to_be_checked]}},async function(err,docs){
        if(err){
            res.json({"message":"Some Error Occured","result":"false"});
            console.log("Error in Chat_three_dots.js "+err)
        }
        if(setConfidential === '0'){
            // console.log('isConfidential : False and moveToVault : False')
            docs.isConfidential = "false"
            docs.moveToVault =  "false"
            docs.save()
            
            //res.send('isConfidential : False and moveToVault : False')
        }
        else if(setConfidential === '1'){
            // console.log('isConfidential : True and moveToVault : False')
            docs.isConfidential = "true"
            docs.moveToVault =  "false"
            docs.save()
            // res.send('isConfidential : True and moveToVault : False')
        }
        else if(setConfidential === '2'){
            // console.log('isConfidential : True and moveToVault : True')
            docs.isConfidential = "true"
            docs.moveToVault =  "true"
            docs.save()
            // res.send('isConfidential : True and moveToVault : True')
        }
        res.json({"message":"Security Level Set","result":"true"});
    });



})

routes.post('/check_isConfidential',(req,res)=>{
    //console.log(req.body);
    const logged_in_user = req.body.logged_in_user;
    const user_to_be_checked = req.body.user_to_be_checked;
    //console.log(logged_in_user,user_to_be_blocked);
    messages.PersonalChat.findOne({'user_1':{$in:[logged_in_user,user_to_be_checked]},'user_2':{$in:[logged_in_user,user_to_be_checked]}},async function(err,result){
        if(err){
            console.log("Err"+err);
            res.json({"message":"Some Error Occured","result":"false"});
        }
        if(result){
            res.json({"message":result.isConfidential,"result":"true"});
        }
        else{
           res.json({"message":"no","result":"true"});
        }
    });
});

routes.post('/check_ssl_vault',(req,res)=>{
    //console.log(req.body);
    const logged_in_user = req.body.logged_in_user;
    const user_to_be_checked = req.body.user_to_be_checked;
    //console.log(logged_in_user,user_to_be_blocked);
    messages.PersonalChat.findOne({'user_1':{$in:[logged_in_user,user_to_be_checked]},'user_2':{$in:[logged_in_user,user_to_be_checked]}},async function(err,result){
        if(err){
            console.log("Err"+err);
            res.json({"message":"Some Error Occured","result":"false"});
        }
        if(result){
            res.json({"message":result.moveToVault,"result":"true"});
        }
        else{
           res.json({"message":"no","result":"true"});
        }
    });
});

 module.exports = routes;