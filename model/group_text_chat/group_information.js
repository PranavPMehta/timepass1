
var mongoose = require('mongoose');
var Schema = mongoose.Schema;

var gUsrSchema = new Schema({
    user_id: String
});

var groupInformationSchema = new Schema({
    group_id : String,
    group_name : String,
    display_picture:{
        url :{  type: String,
                default:"null"
             },
        required:false,
    },
    objective: String,
    members: [gUsrSchema],
    admin: String,
    creation_timestamp:String
});

var GroupInformation = mongoose.model('GroupInformation', groupInformationSchema);
module.exports = GroupInformation;

