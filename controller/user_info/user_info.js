const routes = require('express').Router();
var crypto = require('crypto');

const auth = require('..//middleware/auth')
var User = require('../../model/user_info/User');
var messages=require('../..//model/one_to_one_text_chat/personal_chat');
var group_messages=require('../..//model/group_text_chat/group_messages');
var group_info=require('../..//model/group_text_chat/group_information');

//Demo Route to Check Connection
routes.get('/CheckUserInfo', (req, res) => {
    res.status(200).json({ message: 'User Info Connected!' });
});

//Get User Profile Information
routes.get('/users/me', auth, async(req, res) => {
    // View logged in user profile
    try{
        console.log("Req user "+req.user);
        res.json({"message":JSON.stringify(req.user)})
    }
    catch(error){
        console.log("Inqwert");
        res.json({"message":error})
    }
});

//Logout current user from current device
routes.get('/users/me/logout', auth, async (req, res) => {
    // Log user out of the application
    try {
        /*req.user.tokens = req.user.tokens.filter((token) => {
            return token.token != req.token
        })*/
        console.log("Post Data1 Session End",req.user.Email);
        await User.findOne({'Email':req.user.Email},async function(err,user){
            User.update({'Email':user.Email},{$set: { 'notify_islogin': false,'token':''}},function(err,result)
                        {console.log("Result"+user.Email,result);})
        })
        await req.user.save()
        res.json({"message":"Logout successfully"})
    } catch (error) {
        res.status(500).json({"message":error})
    }
});

//Logout current user from all devices
routes.post('/users/me/logoutall', auth, async(req, res) => {
    // Log user out of all devices
    try {
        req.user.tokens.splice(0, req.user.tokens.length)
        console.log("Post Data1 Session End",req.user.Email);
        await User.findOne({'Username':req.user.Email},async function(err,user){
            User.update({'Email':user.Email},{$set: { 'notify_islogin': false}},function(err,result)
                        {console.log("Result"+user.Email,result);})
        })
        await req.user.save()
        res.send()
    } catch (error) {
        res.status(500).send(error)
    }
})

//Get List of Users from the server
routes.get('/users/list',async (req,res)=>{
    await messages.PersonalChat.find({
        "$or": [{
            "user_1": req.query.user_id
        }, {
            "user_2": req.query.user_id
        }]
    },async function(err,docs){
        let user_group_id = []
        let u_id = []
        let result = []
        if(err) throw err;
        if(docs)
        {
            for( pid in docs) {
                u_id.indexOf(docs[pid].user_1) === -1 ? u_id.push(docs[pid].user_1) : ''
                u_id.indexOf(docs[pid].user_2) === -1 ? u_id.push(docs[pid].user_2) : ''
            }
            result = await User.find({ "user_id": { "$in": u_id }},{"Username":1,"user_id":1,"Designation":1, "display_picture":1})
            group_info.find({'members':{$elemMatch :{user_id:req.query.user_id}}},async function(err,grp_ids){
                if(err) throw err;
                
                if(grp_ids) {
                    for( gid in grp_ids) {
                        console.log("Grpids:",grp_ids[gid].group_id,grp_ids[gid].group_name)
                        grp = {group_id:grp_ids[gid].group_id, group_name:grp_ids[gid].group_name, display_picture:grp_ids[gid].display_picture}
                        result.push(grp);
                    }

                }
                console.log(result)
                res.json({"message":JSON.stringify(result)});
            })
        }


    });

});

routes.post('/updateVaultPassword', auth, async(req, res) => {
    console.log("Starting Vault")
    // View logged in user profile
    try{
        var vault_password = req.body.vault_password;
        
        req.user.vault_password = vault_password;
        
        console.log("Vault_Password",vault_password);
        req.user.save(async function(err) {
            if (err) throw err;
            //response.json('Registeration successful');
            res.json({'message':'Vault Registeration successful','result':'true'});
            console.log('Vault Registeration successful');
        })
        //res.json({"message":JSON.stringify(req.user)})
    }
    catch(error){
        res.json({"message":error})
    }
})

routes.post('/users/orglist',(req,res) =>{
    var otoken=req.body.org_token;
    console.log(otoken);
    User.find({"org_token":otoken,"verified":true},{"Name":1,"Username":1,"Designation":1,"user_id":1,"display_picture":1},function(err, org_list) {
        if(err)
            throw err;
        console.log(org_list);
        res.json({"message":JSON.stringify(org_list)});
    });
});

//To send the personal info
routes.post('/getUserInfo',(request,response,next)=>{
        var post_data=request.body;
        console.log(post_data);

        var userid=post_data.user_id;
        console.log(userid);
        User.findOne({'user_id':userid},function(err,user){
            if(err){
                response.json({'message':"Some error occured",'result':'false'});
            }
            response.json({'message':JSON.stringify(user),'result':'true'});
            console.log(user);
        })
    });

//To send the personal info
routes.post('/getPartialUserInfo',(request,response,next)=>{
        var post_data=request.body;
        console.log(post_data);

        var userid=post_data.user_id;
        console.log(userid);
        User.findOne({'user_id':userid},{"Name":1,"Username":1,"Designation":1,"display_picture":1},function(err,user){
            if(err){
                response.json({'message':"Some error occured",'result':'false'});
            }
            response.json({'message':JSON.stringify(user),'result':'true'});
            console.log(user);
        })
    });

//To modify the personal info
routes.post('/modifyUserInfo',(request,response,next)=>{
        var post_data=request.body;
        console.log(post_data);

        var username1=post_data.username1;
        var url = post_data.url;
        var display_url = {"url":url};

        console.log(username1);
        User.updateOne({'user_id':username1},
                   {$set:{'Name':post_data.name,'user_org_id':post_data.id,'Designation':post_data.designation,'Email':post_data.email,'Mobile':post_data.mobile,'display_picture':display_url}},
                   function(err,user){
                   if(err){
                        response.json({'message':"Some error occured",'result':'false'});
                    }
                   response.json({'message':'Updated Successfully','result':'true'});
                    console.log("Update successful");
                    console.log(user);
                })
    });

//function for modify password
var genRandomString = function (length) {
    return crypto.randomBytes(Math.ceil(length / 2))
    .toString('hex')
    .slice(0, length);
};

var sha512 = function (password, salt) {
    var hash = crypto.createHmac('sha512', salt);
    hash.update(password);
    var value = hash.digest('hex');
    return {
        salt: salt,
        passwordHash: value
    };
};

function saltHashPassword(userPassword) {
    var salt = genRandomString(16);
    var passwordData = sha512(userPassword, salt);
    return passwordData;
}

function chechHashPassword(userPassword, salt) {
    var passwordData = sha512(userPassword, salt);
    return passwordData;
}

//To change the password
routes.post('/checkPreviousPassword',(request,response,next)=>{
        var post_data=request.body;
        var userId=post_data.userId;
        var userPassword=post_data.password;

        User.findOne({'user_id':userId},async function(err,user){

            if(err || user==null){
                    response.json({'result' : 'false', 'message' : 'User not found'});
                    console.log(err);
            }else{
                    var salt=user.salt;
                    var hashed_password=chechHashPassword(userPassword,salt).passwordHash;
                    var encrypted_password=user.Password;
                    if(hashed_password != encrypted_password){
                            response.json({'result' : 'false', 'message' : 'wrong previous password'});
                    }else{
                            response.json({'result' : 'true', 'message' : 'right previous password'});
                    }
            
            }
        })
});


//To change the password
routes.post('/changePassword',(request,response,next)=>{
        var post_data=request.body;
        var userId=post_data.userId;
        var userPassword=post_data.password;

        User.findOne({'user_id':userId},async function(err,user){

            if(err || user==null){
                    response.json({'result' : 'false', 'message' : 'User not found'});
                    console.log(err);
            }else{

                    var salt=user.salt;
                    var hashed_password=chechHashPassword(userPassword,salt).passwordHash;
                    var encrypted_password=user.Password;
                    if(hashed_password === encrypted_password){

                            const newPassword = post_data.newPassword;
                            var hash_data=saltHashPassword(newPassword);
                            var password=hash_data.passwordHash;
                            var salt=hash_data.salt;
                            console.log(salt)
                            console.log(newPassword)
                            console.log(password)

                            user.Password = password,
                            user.salt = salt
                            user.save()
                            response.json({'result' : 'true', 'message' : 'Password Successfully Updated'});

                        }else{

                            response.json({'result' : 'false', 'message' : 'wrong previous password'});

                        }
            }
        })
    });


module.exports = routes;