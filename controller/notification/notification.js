var mongodb = require("mongodb");
var express = require("express");
const routes = require('express').Router();
var bodyparser = require("body-parser");

//Importing models
var user = require('../../model/user_info/User'); 


//Connect to mongodb
var mongoose=require('mongoose');
mongoose.connect('mongodb://localhost:27017/Arishti');


routes.post('/saveNotificationData', (request, response, next)=>{
            var post_data = request.body;

            var notify_message_receipts = post_data.notify_message_receipts;
            var notify_new_message = post_data.notify_new_message;
            var notify_scheduled_meeting = post_data.notify_scheduled_meeting;
            var notify_task_allocation = post_data.notify_task_allocation;
            var notify_app_update = post_data.notify_app_update;
            var user_id = post_data.user_id;
    
            user.updateOne({'user_id':user_id},{$set:{'notify_message_receipts':notify_message_receipts,'notify_new_message':notify_new_message,'notify_scheduled_meeting':notify_scheduled_meeting,'notify_task_allocation':notify_task_allocation,'notify_app_update':notify_app_update}},function(err,result){
                if(err){
                    response.json({'message':"some error occured",'result':"true"});
                }
                
                console.log(result);
                response.json({'message':"Notification/s updated",'result':"true"});
            });
    

});

module.exports = routes;