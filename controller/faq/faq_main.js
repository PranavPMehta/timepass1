var express = require('express');
const routes = require('express').Router();


const auth = require('..//middleware/auth')
var FAQ = require('../../model/faq/faq');



//var insertFAQ = new FAQ({
//            title:"who is the Flash6",
//            description:"Barry Allen is a reinvention of a previous character called the Flash, who appeared in 1940s comic books as the character Jay Garrick. Because he is a speedster, his power consists mainly of superhuman speed.",
//              url:"res/flash.webp",
//              vote:"15"
//});
//                        
//insertFAQ.save(function(err){
//    if(err) throw err;
//    console.log("FAQ saved successfully");
//})



//Demo Route to Check Connection
routes.post('/getFAQs', (req, res) => {
    console.log("get faq called");
    FAQ.find({},function(err,result){
        var resp=JSON.stringify(result);
        res.json({'result':'true','message':resp});
    })
});

module.exports = routes; 