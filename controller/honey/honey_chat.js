const routes = require('express').Router();

var messages=require('../..//model/honey_chat/honey_chat');

routes.post('/honeyChat',(req,res)=>{
    messages.HoneyChat.find({},function(err,docs){
	console.log(req.body)
     if(err) throw err;
     //console.log('sending previous messages:',docs);
     //let arr = docs
     var chats=JSON.stringify(docs);
         res.json({"message":chats,"result":"true"});
 });
 });

routes.post('/getHoneyUsers',(req,res)=>{
    messages.HoneyChat.find({},{user_name:1},function(err,docs){
	console.log(req.body)
     if(err) throw err;
     //console.log('sending previous messages:',docs);
     //let arr = docs
     var chats=JSON.stringify(docs);
         res.json({"message":chats,"result":"true"});
 });
 });

routes.post('/getHoneyMsg',(req,res)=>{
    messages.HoneyChat.find({user_name:req.body.user_name},{data:1},function(err,docs){
	console.log(req.body)
     if(err) throw err;
   console.log('sending previous messages:',docs);
console.log('sending previous messages:',docs.data);
     //let arr = docs
     var chats=JSON.stringify(docs[0].data);
     res.json({"message":chats,"result":"true"});
 });
 });


     
 module.exports = routes;
