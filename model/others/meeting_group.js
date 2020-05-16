
var mongoose = require('mongoose');
var Schema = mongoose.Schema;

var gUsrSchema = new Schema({
    user_id: String,
    username: String
});
var pMessageSchema = new Schema({
    id: String,
    timestamp: String,
    user_id: String,
    status: String,
    location: String,
    reference_id: String,
    data: [String | Buffer]
});
var meetingGroupSchema = new Schema({
    presentation_id : String,
    display_picture:{
        url : String
    },
    description: String,
    agenda: String,
    members: [gUsrSchema],
    organizer: String,
    presentor: String,
    creation_timestamp:String,
    chat: [pMessageSchema]
});
var MeetingGroup = mongoose.model('MeetingGroup',meetingGroupSchema);
var PersonalMessage = mongoose.model('PersonalMessage',pMessageSchema);
var GroupMember = mongoose.model('GroupMember',gUsrSchema);
module.exports = {
    MeetingGroup: MeetingGroup,
    PersonalMessage: PersonalMessage,
    GroupMember: GroupMember
};