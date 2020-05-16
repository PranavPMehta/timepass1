//To initalize and import Express
const app = require('express')();
var express = require('express');

// Bring in our dependencies
var bodyParser = require('body-parser');

//Load All Routes
const login_registration = require('./controller/login_registration/login_registration');
const user_info = require('./controller/user_info/user_info');
const otp = require('./controller/user_info/otp');
const personal_chat = require('./controller/personal_chat/personal_chat');
const other = require('./controller/other/other');
const faq = require('./controller/faq/faq_main');
const group = require('./controller/group_info/group_info_node');
const upload = require('./controller/upload_image/upload_image');
const savenotification = require('./controller/notification/notification');
const savesetting = require('./controller/setting/setting');
const chat_menu = require('./controller/personal_chat/chat_three_dots');
const honey_chat = require('./controller/honey/honey_chat');
const savefeedback = require('./controller/feedback/feedback');
const group_chat = require('./controller/group_info/group_chat');



//To make node never exit
process.on('uncaughtException', function (err) {
    console.error(err);
    console.log("Node NOT Exiting...");
  });


//Using some middle wares
app.use(bodyParser.json({limit: '100mb'}));
app.use(bodyParser.urlencoded({limit: '100mb', extended: true, parameterLimit:500000}));

app.use(express.static(__dirname));

//  Connect all our routes to our application
app.use('/', login_registration);

app.use('/', user_info);
app.use('/', otp);
app.use('/', personal_chat);
app.use('/', other);
app.use('/',faq);
app.use('/', group);
app.use('/', upload);
app.use('/', savenotification);
app.use('/', savesetting);
app.use('/', chat_menu);
app.use('/', honey_chat);
app.use('/', savefeedback);
app.use('/', group_chat);

//app.use('/', route2);


// Turning on server!
app.listen(3000, () => {
  console.log('App listening on port 3000');
});