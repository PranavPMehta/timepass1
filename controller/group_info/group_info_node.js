const routes = require('express').Router();
const express = require('express');

//Importing models
var group = require('../../model/group_text_chat/group_information'); 
var user = require('../../model/user_info/User');
 
  
//Connect to mongodb
var mongoose=require('mongoose');
mongoose.connect('mongodb://localhost:27017/Arishti');


routes.post('/createGroup',(request,response,next)=>{
        var post_data=request.body;
        var member = JSON.parse(post_data.members);
        var group_id = post_data.group_id;
        var group_name = post_data.group_name;
        var objective = post_data.objective;
        var admin = post_data.admin;
        var creation_timestamp = post_data.creation_time;
        var url = post_data.url;
        var display_picture = {"url":url};   
    
        var newgroup = new group({
            group_id:group_id,
            group_name:group_name,
            display_picture:display_picture,
            objective:objective,
            members:member,
            admin:admin,
            creation_timestamp:creation_timestamp
        });
        
        console.log(newgroup);
    
        newgroup.save(async function(err){
            if(err) {
                throw err;
            }else{
                console.log("group created");
                response.json({'message':"Group Created Successfully",'result':'true'});
            }

        });
    
});

routes.post('/getGroupInfo',(request,response,next)=>{
        var post_data=request.body;
        
        var group_id=post_data.group_id;
        
        console.log(group_id);
        
        group.findOne({"group_id":group_id},async function(err,result){
            if(err){
                response.json({'message':"Some error occured",'result':'false'});
                }
       
        var member_ids = result.members;
        var members_info = [];
        
        for(var i=0;i<member_ids.length;i++){
            await user.findOne({'user_id':member_ids[i].user_id},{"Username":1,"Designation":1,"user_id":1,"display_picture":1,"admin":1},function(err,u_info){
                if(err){
                    throw err;
                }

                members_info.push(u_info);

            });
        }
        var final_result = JSON.stringify([result,members_info]);
        console.log(final_result);
        response.json({'message':final_result,'result':'true'});
    
        });
});

routes.post('/modifyGroupUrl',(request,response,next)=>{
        var post_data=request.body;
        console.log(post_data);
        
        var group_id=post_data.group_id;
        
        console.log(group_id);
        
        group.updateOne({'group_id':group_id},{$set:{'display_picture.url':post_data.url}},function(err,result){
            if(err){
                response.json({'message':"Some error occured",'result':'false'});
                }
       
        console.log(result);
        response.json({'message':'Updated successfully','result':'true'});
    
        });
});

routes.post('/modifyGroupInfo',(request,response,next)=>{
        var post_data=request.body;
        console.log(post_data);
        
        var group_id=post_data.group_id;
        
        console.log(group_id);
        
        group.updateOne({'group_id':group_id},{$set:{'group_name':post_data.name,'objective':post_data.objective}},function(err,result){
            if(err){
                response.json({'message':"Some error occured",'result':'false'});
                }
       
        console.log(result);
        response.json({'message':'Updated successfully','result':'true'});
    
        });
});

routes.post('/removeMembers',async(request,response,next)=>{
        var post_data=request.body;
        console.log(post_data);
        var group_id = post_data.group_id;
        var member_ids=JSON.parse(post_data.member_ids);
        var prev_members;
    
        await group.findOne({'group_id':group_id},function(err,result){
            if(err){
                console("Some error occured");
                response.json({'message':"Some error occured",'result':'false'});
                }
        prev_members = result.members;
        
        });
    
        for (var i = 0; i < member_ids.length; i++) {
            var key = member_ids[i].user_id; 
            prev_members.filter(x => x.user_id === key).forEach(x => prev_members.splice(prev_members.indexOf(x), 1));
        }
    
        console.log(prev_members);
        await group.updateOne({'group_id':group_id},{$set:{'members':prev_members}},function(err,result){
            if(err){
                response.json({'message':"Some error occured",'result':'false'});
                }
       
        console.log(result);
        response.json({'message':'Deletion successfully','result':'true'});
        });
        
});

routes.post('/addMembers',async(request,response,next)=>{
        var post_data=request.body;
        console.log(post_data);
        var group_id = post_data.group_id;
        var member_add=JSON.parse(post_data.member_add);
        var prev_members;
        var new_members;
        await group.findOne({'group_id':group_id},function(err,result){
            if(err){
                console("Some error occured");
                }
       
        prev_members = result.members;
        });
        console.log(prev_members);
        console.log(member_add);
        for(var i = 0;i<member_add.length;i++){
            var mem = member_add[i];
            prev_members.push(mem);
        }

        console.log(prev_members);
    
        group.updateOne({'group_id':group_id},{$set:{'members':prev_members}},function(err,result){
            if(err){
                response.json({'message':"Some error occured",'result':'false'});
                }
       
        console.log(result);
        response.json({'message':'Addition successfully','result':'true'});
        });
    
        
});


module.exports = routes;
