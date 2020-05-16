var mongodb = require("mongodb");
var express = require("express");
const routes = require('express').Router();
var bodyparser = require("body-parser");

//Importing models
var user = require('../../model/user_info/User'); 


//Connect to mongodb
var mongoose=require('mongoose');
mongoose.connect('mongodb://localhost:27017/Arishti');

routes.post('/saveFeedbackData', (request, response, next)=>{
        var post_data = request.body;

        var like_application = post_data.like_application;
        var like_features = post_data.like_features;
        var suggestions = post_data.suggestions;
        var feedbackImage = post_data.encodedImage;
        var user_id = post_data.user_id;

        user.updateOne({'user_id':user_id}, {$set:{'like_application':like_application, 'like_features':like_features, 'suggestions':suggestions, 'feedbackImage':feedbackImage}},function(err,result){
        if(err){
            response.json({'message':"some error occured",'result':"true"});
        }
        
        console.log(result);
        response.json({'message':"changes saved successfully",'result':"true"});

        });

});

module.exports = routes;