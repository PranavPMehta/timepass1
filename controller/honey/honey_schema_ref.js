var mongoose = require('mongoose');
mongoose.connect('mongodb://localhost:27017/Arishti');


var db=mongoose.connection;
db.on('error', console.log.bind(console, "connection error"));
db.once('open', function(callback){
    console.log("connection succeeded");
});



var HoneyChat = require('../../model/honey_chat/honey_chat')


var hchat = new HoneyChat.HoneyChat({
    user_name: 'xyz',
    data: [{msg: 'HoneyChat2',
        timestamp: '12332',
        location: 'Pune22',
        read_receipt: 'Samruddhi2',
	side : 'left'},
        {msg: 'HoneyChat122',
        timestamp: '12312',
        location: 'Pune12',
        read_receipt: 'Samruddhi122',
	side : 'left2'}]
});



hchat.save(function(err) {
    if (err) throw err;
    console.log('HoneyChat executed successfully!');
})
