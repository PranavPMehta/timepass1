
var mongoose = require('mongoose');
var Schema = mongoose.Schema;

var TaskSchema = new Schema({
    meeting_id : {type:String,unique:true},
    for_whom : [{user : String}],
    title: String,
    description: String,
    file_upload: String,
});
var Task = mongoose.model('Task',TaskSchema);
module.exports = Task;