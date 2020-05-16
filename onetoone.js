var app = require('express')();
var http = require('http').Server(app);
var io = require('socket.io')(http);
var routes = require('express').Router();
var messages=require('./model/one_to_one_text_chat/personal_chat');
var mongoose=require('mongoose');
var request = require('request');
let url = require('./controller/constant');
let ref;
var users = [];
// {
//     user1_socket_id = socket.id
//     user2_socket_id = socket.id
//     chat_id = user_chat_id
// }

mongoose.connect('mongodb://localhost:27017/Arishti');

app.get('/',function(req,res){
    res.sendFile('index.html');
})
io.on('connection',async function(socket){

    console.log('one user connected '+socket.id);

    
    function disconnect_socket(socket_id){
        for(i=0;i<users.length;i++) {
            if(users[i].user1_socket_id === socket_id)  users[i].user1_socket_id = ''
            else if(users[i].user2_socket_id === socket_id) users[i].user2_socket_id = ''
        }
    }
    
    function alreadyPresent(){
        for(i=0;i<users.length;i++) { 
            if(users[i].chat_id === sock_chat_id1 || users[i].chat_id === sock_chat_id2) return true;
        }
    }
    
    //Set users with their socket id pairs
    socket.on('connected',async (sender_id,receiver_id,socket1)=>{
        sock_chat_id1 = sender_id+"_"+receiver_id;
        sock_chat_id2 = receiver_id+"_"+sender_id;
        socket.id = socket1
        console.log("Called:",socket1);
        if(users.length){
    
            users.filter((e) => {
            if (e.chat_id === sock_chat_id1 || e.chat_id === sock_chat_id2) {
                if(!e.hasOwnProperty('user2_socket_id')) e.user2_socket_id = socket.id;
                else if(e.user2_socket_id==='') e.user2_socket_id = socket.id;
                else if (e.user1_socket_id==='') e.user1_socket_id = socket.id;
                console.log("1:",users)
            }
            else if(!alreadyPresent()){users.push({chat_id:sock_chat_id1,user1_socket_id:socket.id});console.log("2:",users)}
        })}
        else{
            users.push({chat_id:sock_chat_id1,user1_socket_id:socket.id})   
            console.log("3:",users)     
        }
    }) 
    
    routes.post('/setReceived',(req,res)=>{
        messages.PersonalChat.findAndModify({query:{
            'chat.id':req.body.msg_id},update:{$set:{'chat.isReceived':true}},
            async function(err,result){
                if(err)
                    throw err;
                console.log(result);
            //    res.send("OK");
                response.json({'message':'OK','result':'true'});

            }});
    });
        
        
        
    await socket.on('message',async function(data,callback){
      //  console.log("MSG! "+data.msg1)
        console.log("Data type:"+data.data_type)
        sender_id = data.sender_id,
        receiver_id = data.receiver_id,
        loc = data.location,
        n_msg = data.msg1,
        key_1 = data.msg2,
        key_2 = data.msg3,
        type1 = data.data_type
        chat_id1 =sender_id+"_"+receiver_id
        chat_id2 =receiver_id+"_"+sender_id
        ref=sender_id+'_'+receiver_id
        data.id=ref+'_0';
        data.time=new Date();
        sock_chat_id1 = data.sender_id+"_"+data.receiver_id;
        sock_chat_id2 = data.receiver_id+"_"+data.sender_id;
   
        //Send via socket
        let user_index;

        let message={"message":data}
        users.filter((e,i) => {if(e.chat_id === sock_chat_id1 || e.chat_id === sock_chat_id2) user_index=i;})

        if(users[user_index].user1_socket_id!=null && users[user_index].user2_socket_id!=null)
        {
            if(users[user_index].user1_socket_id==socket.id)
            {
                console.log("Socket2:",users[user_index].user2_socket_id);
                socket.broadcast.to(users[user_index].user2_socket_id).emit('message',message)
            }
            else{
                console.log("Socket1:",users[user_index].user1_socket_id);
                socket.broadcast.to(users[user_index].user1_socket_id).emit('message',message)
            }    
        }
        
        await messages.PersonalChat.findOne({
            "$or": [{
                "chat_id": chat_id1
            }, {
                "chat_id": chat_id2
            }]
        },async function(err,result){
             if(!result){  
                    var insertJson = new messages.PersonalChat({
                    user_1: sender_id,
                    user_2: receiver_id,
                    chat_id: sender_id+'_'+receiver_id,
                    seqno: 0,
                    chat: [{id: ref+'_0',
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
                           }]
                });
                r = await insertJson.save();
             }
             else
             {
                counter = result.seqno+1;
                ref=ref+'_'+counter;
                data.id=ref;
                 data.time=new Date();
                await messages.PersonalChat.update({_id:result._id}, {$set: { seqno: counter }})
                chat1 = new messages.PersonalMessage1({
                    id: ref,
                    timestamp: new Date(),
                    user_id: sender_id,
                    status: 'success',
                    location: loc,
                    reference_id: '' ,
                    msg : {data_type:type1,data: n_msg, key_1: key_1, key_2: key_2}
                });                        
                result.chat.push(chat1);
                r = await result.save()
            } 
        });

            
                
        callback(JSON.stringify(ref));
    });
               
     socket.on('disconnect',function(){
        console.log('one user disconnected '+socket.id);
        disconnect_socket(socket.id);
    })
})

http.listen(3001,function(){
    console.log('server listening on port 3001');
})