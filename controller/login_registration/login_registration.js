const bcrypt = require('bcryptjs');
var mongodb = require('mongodb');
var ObjectID = mongodb.ObjectID;
var crypto = require('crypto');


//store the data came from client temporary
    var password;
    var salt;
    var name;
    var email;
    var username;
    var mobileno;
    var designation;
    var uid;
    var registerts;
    var imei;
    var insertJson;
    var org_token;
    var Registertoken;


const routes = require('express').Router();



//Importing models
var User = require('../../model/user_info/User');

//Connect to mongodb
var mongoose=require('mongoose');
mongoose.connect('mongodb://localhost:27017/Arishti');


//Demo Route to check Connection
routes.get('/getLoginRegistration', (req, res) => {
    res.status(200).json({ message: 'Login and Registration Connected!' });
});

// Accepts details from user and saves in mongodb
routes.post('/register',(request,response,next)=>{
    //console.log("Request Generated");
    var post_data=request.body;

    var plaint_password=post_data.password;
        //console.log("Password   "+ plaint_password);
    var hash_data=saltHashPassword(plaint_password);

    password=hash_data.passwordHash;
    salt=hash_data.salt;
    name=post_data.name;
    email=post_data.email;
    username=post_data.username;
    mobileno=post_data.mobileno;
    designation=post_data.designation;
    uid =post_data.uid;
    registerts=post_data.TS;
    imei=post_data.IMEI;
    org_token=post_data.org_token;
    
    var date = new Date();
    var ts=date.getTime();
   
    var diff=ts-registerts;
    diff=diff/1000;

    console.log("diff "+diff);
            
    if(diff<40){
        Registertoken=uid +"_"+ imei +"_" + registerts;
        response.json({'message':Registertoken,'result':'ack'});
        console.log("Registertoken "+Registertoken);
    }else{
        response.json({'message':'Time Limit Exceeded','result':'ack'});
        console.log("Time limit of response exceeded");
        
    }
    //console.log(name+" " +email);
    
});


//save the details after the acknolagement recieved 

routes.post('/register/store',(request,response,next)=>{
    
    if(request.body.result == "true"){
    
            console.log("IMEI Recieved "+imei);
            insertJson = new User({
                Name: name,
                user_id: Registertoken,
                Username: username,
                Email: email,
                Mobile: mobileno,
                Designation: designation,
                Password: password,
                salt : salt,
                IMEI : imei,
                RegisterTS : registerts,
                org_token:org_token,
                user_org_id:uid
            });

            User.find({'Email':email}).count(function(err,number){
                if(number!=0)
                {
                    //response.json('Email id already exists');
                    response.json({'message':'Email id already exists','result':'false'});
                    console.log('Email id already exists');
                }
                else
                {
                    insertJson.save(async function(err) {
                        if (err) 
                        {
                            console.log("Errror"+err+"Regi "+Registertoken+"    "+org_token);
                            throw err;
                        }
                        //response.json('Registeration successful');
                    const token = insertJson.generateAuthToken();    
                        response.json({'message':'Registeration successful','result':'true','token':token});
                        console.log('Registeration successful');
                    })
                }
            })
        
    }else{
            response.json({'message':'Missmatch in IMEI number','result':'false',
                           'token':token});
            console.log('IMEI mismatch');
    }
});

//Accepts email and password and checks if user is present and the user is verified
routes.post('/login',(request,response,next)=>{
    console.log("In;")
    var post_data=request.body;
    console.log("Post Data",post_data);

    var email=post_data.email;
    var userPassword=post_data.password;
    var noti_token = post_data.token;
    console.log(email+ " " + userPassword+"   "+noti_token);

    //User.find({'Email':email}).count(function(err,number){
    User.find({'Username':email}).count(function(err,number){
    if(number==0)
    {
        response.json({'message':'User not exists','result':'false','verified':'false','token':123});
        console.log('Email id not exists');
    }
    else
    {
        User.findOne({'Username':email},async function(err,user){
            
                {
                    var salt=user.salt;
            //console.log("Printing User Password",userPassword);
            var hashed_password=chechHashPassword(userPassword,salt).passwordHash;
            var encrypted_password=user.Password;
        if(hashed_password===encrypted_password)
            {
                if(user.verified == "true"){
                    
                    const user = await User.findByCredentials(email, hashed_password)
                    if (!user) {
              //          console.log("sdf");
                        return response.status(401).json({'message':"Login failed! Check authentication credentials","result":"false","verified":"false",'token':123});
                        //return response.status(401).send({error: 'Login failed! Check authentication credentials'})
                    }
                    const token = await user.generateAuthToken()
                    response.json({'message':"Login Success","result":"true","verified":"true","token":token});
                    console.log('Login Success');
                    user.notification_token = noti_token;
                    User.update({'Email':user.Email},{$set: { 'notify_islogin': true}},function(err,result){
                        console.log(user.Email,result);
                    })
   
                    await user.save();
                //    console.log(user)
                }
                else{
                    response.json({'message':"Login Success but User not verified","result":"true","verified":"false",'token':123});
                    console.log('Login Success but User not verified');
                }
            }
            else
            {
                response.json({'message':'Wrong Password','result':'false',"verified":"false",'token':123});
                console.log('Wrong Password');
            }        
        }
            
        })
    }
    })
});

routes.post('/loginsessionend',async (request,response)=>{
    console.log("Post Data1 Session End",request.body.email);
    var post_data=request.body;
    
    var email=post_data.email;
    console.log("email"+email);
    await User.findOne({'Username':email},async function(err,user){
        //console.log("result"+user);
        response.json({"result":true});
        User.update({'Email':user.Email},{$set: { 'notify_islogin': false}},function(err,result)
                    {
            console.log("Result"+user.Email,result);
    })
    })
});

routes.post('/checklogin',async (request,response)=>{
    console.log("Post Data1",request.body.email);
    var post_data=request.body;
    
    var email=post_data.email;
    console.log("email"+email);
    await User.findOne({'Username':email},async function(err,user){
        if(user==null)
            {
                response.json({"result":"User does not exist"});
            }
        else
            {
                console.log("ILogin"+user.notify_islogin);  
                if(user.notify_islogin==true)
                    {
                        response.json({"result":true});
                    }
                else
                    response.json({"result":false});        
            }
    })
});

module.exports = routes;


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