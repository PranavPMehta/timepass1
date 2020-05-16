var mongoose = require('mongoose');
var Schema = mongoose.Schema;

var inhoney = new Schema({
    msg: {
	type:String,
	required:true
    },
    timestamp: String,
    location: String,
    read_receipt: String,
    side : String
    
});

// create a schema
var HoneyChat = new Schema({
    user_name : String,
    data: [inhoney]

});
var HoneyChat = mongoose.model('HoneyChat', HoneyChat);
var HoneyPersonalMessage1 = mongoose.model('HoneyPersonalMessage1', inhoney);
module.exports = {HoneyChat:HoneyChat,HoneyPersonalMessage1:HoneyPersonalMessage1};