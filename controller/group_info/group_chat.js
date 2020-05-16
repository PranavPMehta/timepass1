const routes = require('express').Router();

var grp_messages = require('../../model/group_text_chat/group_messages');

routes.post('/setPoll',async (req,res)=>{
    
    var poll_flag=0;
    
    await grp_messages.GroupChat.findOne(
        {'gmessages.id':req.body.msg_id},
            async function(err,result){
                if(err)
                    throw err;
                if(result){  
                console.log(result);
                for(var i=0;i<result.gmessages.length;i++){
                    if(result.gmessages[i].id == req.body.msg_id){
                        if(result.gmessages.is_poll){
                            poll_flag = 1;
                        }
                    }
                }
                        
                }else{
                    res.json({'message':'message can not polled until it is recieved','result':false});
                }
            });
    
    if(poll_flag == 1){
            res.json({'message':'Message Already polled','result':'true'});
    }else{
        await grp_messages.GroupChat.findOneAndUpdate(
            {'gmessages.id':req.body.msg_id},
            { '$set':{'gmessages.$.is_poll':true}},
                async function(err,result){
                    console.log(result);
                    if(err)
                        throw err;
                    res.json({'message':'Message polled','result':'true'});

                });
    }

    
    });

routes.post('/setAgree',async (req,res)=>{

    var agreecount; 
    var voted;
    var flag_new=0;
    await grp_messages.GroupChat.findOne({'gmessages.id':req.body.msg_id},async function(err,result){
                if(err)
                    throw err;

                for(var i=0;i<result.gmessages.length;i++){
                    if(result.gmessages[i].id == req.body.msg_id){
                        agreecount = result.gmessages[i].agree;
                        voted = result.gmessages[i].voted_user_id;
                        for(var j=0;j<voted.length;j++){
                            if(voted[j].voter_id == req.body.user_id){
                                flag_new = 1;
                            }
                        }

                    }
                }
                console.log("agree ="+agreecount);
            });

    if(flag_new == 1){
        res.json({'message':'Already Voted','result':agreecount.toString()});
    }else{
            agreecount++;
            var newvoter = new grp_messages.Voter({
                voter_id:req.body.user_id
            });

            voted.push(newvoter);

            grp_messages.GroupChat.findOneAndUpdate(
                {'gmessages.id':req.body.msg_id},
                { '$set':{'gmessages.$.agree':agreecount,'gmessages.$.voted_user_id':voted}},
                    async function(err,result){
                        if(err)
                            throw err;
                        res.json({'message':'Message Agreed','result':agreecount.toString()});

                    });
        
    }
    });

routes.post('/setDisagree',async (req,res)=>{
    
    var disagreecount; 
    var voted;
    var flag_new=0;
    await grp_messages.GroupChat.findOne({'gmessages.id':req.body.msg_id},async function(err,result){
                if(err)
                    throw err;
                for(var i=0;i<result.gmessages.length;i++){
                    if(result.gmessages[i].id == req.body.msg_id){
                        disagreecount = result.gmessages[i].disagree;
                        voted = result.gmessages[i].voted_user_id;
                        for(var j=0;j<voted.length;j++){
                            if(voted[j].voter_id == req.body.user_id){
                                flag_new = 1;
                            }
                        }
                    }
                }
                console.log("disagree = "+disagreecount);
            });

    if(flag_new == 1){
        res.json({'message':'Already Voted','result':disagreecount.toString()});
    }else{
    
            disagreecount++;

            var newvoter = new grp_messages.Voter({
                voter_id:req.body.user_id
            });

            voted.push(newvoter);
            
            grp_messages.GroupChat.findOneAndUpdate(
                {'gmessages.id':req.body.msg_id},
                { '$set':{'gmessages.$.disagree':disagreecount,'gmessages.$.voted_user_id':voted}},
                    async function(err,result){
                        if(err)
                            throw err;
                        res.json({'message':'Message Disagreed','result':disagreecount});

                    });
    }
    });

routes.post('/setNeutral',async(req,res)=>{
    
    var neutral; 
    var voted;
    var flag_new=0;
    await grp_messages.GroupChat.findOne({'gmessages.id':req.body.msg_id},async function(err,result){
                if(err)
                    throw err;
                for(var i=0;i<result.gmessages.length;i++){
                    if(result.gmessages[i].id == req.body.msg_id){
                        neutral = result.gmessages[i].neutral;
                        voted = result.gmessages[i].voted_user_id;
                        for(var j=0;j<voted.length;j++){
                            if(voted[j].voter_id == req.body.user_id){
                                flag_new = 1;
                            }
                        }
                    }
                }
                
            console.log("neutral = "+neutral);


    });

    if(flag_new == 1){
        res.json({'message':'Already Voted','result':neutral.toString()});
    }else{

            neutral++;

            var newvoter = new grp_messages.Voter({
                voter_id:req.body.user_id
            });

            voted.push(newvoter);

            grp_messages.GroupChat.findOneAndUpdate(
                {'gmessages.id':req.body.msg_id},
                { '$set':{'gmessages.$.neutral':neutral,'gmessages.$.voted_user_id':voted}},
                    async function(err,result){
                        if(err)
                            throw err;
                        res.json({'message':'Message Neutraled','result':neutral});

                    });
    }
    });

module.exports = routes;
