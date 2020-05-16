var app = require('express')();
var http = require('http').Server(app);
var io = require('socket.io')(http);
var routes = require('express').Router();
var messages=require('./model/group_text_chat/group_messages');
var mongoose=require('mongoose');
let ref;
let room;



mongoose.connect('mongodb://localhost:27017/Arishti');

io.on('connection',async function(socket){
    room = socket.handshake.query.group_id;
    console.log('one user connected '+socket.id);
    socket.join(room);    
        
    await socket.on('message',async function(data,callback){
      //  console.log("MSG! "+data.msg1)
        console.log("Data type:"+data.data_type)
        sender_id = data.sender_id,
        loc = data.location,
        n_msg = data.msg1,
        key_1 = data.msg2,
        key_2 = data.msg3,
        type1 = data.data_type
        ref = data.group_id
        chat_id = data.group_id
        
        await messages.GroupChat.findOne({
                "group_id": chat_id
        },async function(err,result){
             if(!result){  
                    //ref =   //check once reference
                    var insertJson = new messages.GroupChat({
                    group_id: chat_id,
                    seqno: 0,
                    gmessages: [{id: ref+'_0',
                        timestamp: new Date(),
                        user_id: sender_id,
                        status: 'success',
                        location: loc,
                        reference_id: '' ,
                            msg : {
                                data_type:type1,
                                data: n_msg, 
                                key_1: key_1, 
                                key_2: key_2
                            },
                                 
                           
                        }]
                });
                r = await insertJson.save();
             }
             else
             {
                counter = result.seqno+1;
                ref=ref+'_'+counter;
                await messages.GroupChat.update({_id:result._id}, {$set: { seqno: counter }})
                chat1 = new messages.GroupMessage({
                    id: ref,
                    timestamp: new Date(),
                    user_id: sender_id,
                    status: 'success',
                    location: loc,
                    reference_id: '' ,
                    msg : {
                        data_type:type1,
                        data: n_msg, 
                        key_1: key_1, 
                        key_2: key_2
                    }
                });                        
                result.gmessages.push(chat1);
                r = await result.save()
            } 
        });
        data.id=ref;
        data.time=new Date();
        //Send via socket
        let message={"message":data}        
        socket.broadcast.to(room).emit("message",message);
            
        callback(JSON.stringify(ref));
    });
               
    
     socket.on('disconnect',function(){
        console.log('one user disconnected '+socket.id);
        socket.leave(room);
    })
})



http.listen(3002,function(){
    console.log('server listening on port 3002');
})