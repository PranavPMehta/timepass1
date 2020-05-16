var mongodb = require("mongodb");
var express = require("express");
const routes = require('express').Router();
var bodyparser = require("body-parser");

//Importing models
var user = require('../../model/user_info/User'); 


//Connect to mongodb
var mongoose=require('mongoose');
mongoose.connect('mongodb://localhost:27017/Arishti');


routes.post('/saveSettingsData', (request, response, next)=>{
            var post_data = request.body;

            var profile_picture_visibility = post_data.profile_picture_visibility;
            var org_details_visibility = post_data.org_details_visibility;
            var email_visibility = post_data.email_visibility;
            var contact_details_visibility = post_data.contact_details_visibility;
            var designation_visibility = post_data.designation_visibility;
            var user_id = post_data.user_id;
    
            user.updateOne({'user_id':user_id},{$set:{'profile_picture_visibility':profile_picture_visibility,'org_details_visibility':org_details_visibility,'email_visibility':email_visibility,'contact_details_visibility':contact_details_visibility,'designation_visibility':designation_visibility}},function(err,result){
                if(err){
                    response.json({'message':"some error occured",'result':"true"});
                }
                
                console.log(result);
                response.json({'message':"changes saved successfully",'result':"true"});
            });


});

module.exports = routes; 