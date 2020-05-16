const routes = require('express').Router();
var express = require("express");
var bodyParser = require("body-parser");
var multer = require('multer');
var app = express();
var fs = require('fs');

routes.use(bodyParser.json());

var resdir;
var filename;
var storage = multer.diskStorage({
  destination: function (req, file, callback) {
  	var name = req.body.description;
    resdir = '/res/'+`${name}`;
    dir ='.'+resdir;
  	console.log(name + "\t" + dir);
  	fs.exists(dir, exist => {
  		if(!exist) {
  			return fs.mkdir(dir, error => callback(error, dir));
  		}
  		return callback(null, dir);
  	});
  },
  filename: function (req, file, callback) {
  	var name = req.body.description;
  	console.log(file);
      filename = name+"_"+`${Date.now()}.jpg`;
    callback(null, filename);
  }
});

var upload = multer({ storage : storage }).array('userPhoto',1);

routes.post('/uploadImage',function(req,res){
    upload(req,res,function(err) {
        
        console.log(req.body);
        
        if(err) {
            return res.json({"message":"Error uploading file."+err,"result":"false"});
        }
        console.log(resdir+'/'+filename);
        res.json({"message":resdir+'/'+filename,"result":"true"});
    });
});
  
module.exports = routes;
