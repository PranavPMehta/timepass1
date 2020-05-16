
var mongoose = require('mongoose');
var Schema = mongoose.Schema;

var voters = new Schema({
   voter_id:{
            type:String,
            default:"null"
        }
});

var gMessageSchema = new Schema({
    id: String,
    timestamp: String,
    user_id: String,
    status: String,
    location: String,
    reference_id:{
        type: String,
        default:"null"
    } ,
    //data: [String | Buffer],
    is_poll:{
        type:Boolean,
        default:false
    },
    agree:{
        type:Number,
        default:0
    },
    disagree:{
        type:Number,
        default:0
    },
    neutral:{
        type:Number,
        default:0
    },
    msg :{
        data_type:String,
        data:String,
        key_1:String,
        key_2:String,
        
    },
    voted_user_id: [voters],
    viewed_user_id: [{type:String,default:' '}]
});

// create a schema
var groupChatSchema = new Schema({
    group_id : String,
    group_name : String,
    seqno : Number,
    gmessages: [gMessageSchema]

});
var GroupChat = mongoose.model('GroupChat', groupChatSchema);
var GroupMessage = mongoose.model('GroupMessage', gMessageSchema);
var Voter = mongoose.model('Voter', voters);
module.exports = {GroupChat:GroupChat,GroupMessage:GroupMessage,Voter:Voter};
