var mongoose = require('mongoose');
var Schema = mongoose.Schema;

var pMessageSchema = new Schema({
    id: String,
    timestamp: String,
    user_id: String,
    status: String,
    location: String,
    reference_id: String,
    is_deleted : {type: String,default:false},
    is_important : {type: String,default:false},
    is_confidential : {type: String,default:false},
    isReceived:{
        type:Boolean,
        default:false
    },
    isVault:{
        type:Boolean,
        default:false
    },
    msg :{data_type:String,
        data:String,
        key_1:String,
        key_2:String
    }
    
});

// create a schema
var personalChatSchema = new Schema({
    user_1 : String,
    user_2 : String,
    chat_id : String,
    seqno : Number,
    chat: [pMessageSchema]

});
var PersonalChat = mongoose.model('PersonalChat', personalChatSchema);
var PersonalMessage1 = mongoose.model('PersonalMessage1', pMessageSchema);
module.exports = {PersonalChat:PersonalChat,PersonalMessage1:PersonalMessage1};