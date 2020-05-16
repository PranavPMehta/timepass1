//Importing Libraries
const routes = require('express').Router();
var crypto = require('crypto');
const bcrypt = require('bcryptjs');
const request = require("request");
const utf8 = require("utf8");
const emailjs = require("emailjs");

//Importing Models
var User = require('../../model/user_info/User');
var OTP = require('../../model/user_info/otp');


//Demo Route to Check Connection
routes.get('/CheckOTP', (req, res) => {
    res.status(200).json({ message: 'OTP Connected!' });
});

//Request for an Email OTP and save the userId and its otp in MongoDB
routes.post("/otp/requestEmailOTP", async (req, res) => {
    const userID = req.body.userid;
    //const email = req.body.email;
    //const mobile = req.body.mobile;
    //console.log(userID+" "+email+" "+mobile);
    const otpEmail = Math.floor(100000 + Math.random() * 900000);
    
    console.log("UserID"+userID)
    console.log("OTP Mail:"+otpEmail)
    User.findOne({user_id : userID}, (err, user) => {
        if(err) throw err;
        if(user) {
            //console.log('User '+ user);
            email = user.Email;
            const jsonObj = new OTP({
                "userId" : userID,
                "otpEmail" : otpEmail,
                "email" : email,
                "timestamp" : new Date()
            });
            
            OTP.findOne({userId : userID}, (err, result) => {
                if(err) throw err;
                if(result) {
                    res.json({'message': 'Wait for some time before requesting again!','result':'true'});
                } else {
                    jsonObj.save(function(err) {
                        if (err) throw err;
                        //sendSms(otpMobile, mobile);
                        //sendEmail(otpEmail, email);
                        res.json({'result' : 'true' ,'message' : 'OTP Send Successfully'});
                        console.log('OTP Requested by '+ userID);
                    })
                }
            });
            //res.json(user);
            
        } else {
            console.log("User not Found!");
            res.json({'message': 'User not Found!','result':'false'});
        }
    });
});


//Verify the otp sent by the user and then delete the document from MongoDB
routes.post("/otp/verifyEmailOTP", (req, res) => {
        const userID = req.body.userid;
        const otpEmail = req.body.otpEmail;
        OTP.findOne({userId : userID}, (err, result) => {
            if(err) {
                console.log(err);
                return;
            };
            console.log(result);
            if(!result) {
                res.json({'message' : 'Error in Retrieving OTP','result':'false'});
                console.log("Error in retrieving OTP");
                return;
            }
	var otppp = result.otpEmail;
	var o1 = otppp.split('').reverse().join('')
	
	var result;

            if(otpEmail == o1) {
                res.json({'result' : 'true', 'message' : 'OTP Verified'});
                console.log("OTP Verified");
                OTP.deleteOne({userId : userID}, () => {})
            } else {
                res.json({'result' : 'false', 'message' : 'Error in OTP'});
                console.log("Error in OTP");
            }
        });
    });


//Request for an OTP and save the userId and its otp in MongoDB
routes.post("/otp/request", async (req, res) => {
    const userID = req.body.userid;
    //const email = req.body.email;
    //const mobile = req.body.mobile;
    //console.log(userID+" "+email+" "+mobile);
    const otpMobile = Math.floor(100000 + Math.random() * 900000);
    const otpEmail = Math.floor(100000 + Math.random() * 900000);
    
    console.log("UserID"+userID)
    console.log("OTP Mob:"+otpMobile)
    console.log("OTP Mail:"+otpEmail)
    User.findOne({Username : userID}, (err, user) => {
        if(err) throw err;
        if(user) {
            //console.log('User '+ user);
            email = user.Email;
            mobile = user.Mobile;
            const jsonObj = new OTP({
                "userId" : userID,
                "otpEmail" : otpEmail,
                "otpMobile" : otpMobile,
                "email" : email,
                "mobile" : mobile,
                "timestamp" : new Date()
            });
            OTP.findOne({userId : userID}, (err, result) => {
                if(err) throw err;
                if(result) {
                    res.json({'message': 'Wait for some time before requesting again!','result':'true'});
                } else {
                    jsonObj.save(function(err) {
                        if (err) throw err;
                        //sendSms(otpMobile, mobile);
                        //sendEmail(otpEmail, email);
                        res.json({'result' : 'true' ,'message' : 'OTP Requested Successfully'});
                        console.log('OTP Requested by '+ userID);
                    })
                }
            });
            //res.json(user);
            
        } else {
            console.log("User not Found!");
            res.json({'message': 'User not Found!','result':'false'});
        }
    });
    


});


//Verify the otp sent by the user and then delete the document from MongoDB
routes.post("/otp/verify", (req, res) => {
        const userID = req.body.userid;
        const otpMobile = req.body.otpMobile;
        const otpEmail = req.body.otpEmail;
        OTP.findOne({userId : userID}, (err, result) => {
            if(err) {
                console.log(err);
                return;
            };
            console.log(result);
            if(!result) {
                res.json({'message' : 'Error in Retrieving OTP','result':'false'});
                console.log("Error in retrieving OTP");
                return;
            }
	var otppp = result.otpEmail;
	var o1 = otppp.split('').reverse().join('')
	

	var otm=result.otpMobile;
	var o2=otm.split('').reverse().join('')
	var result;
      //console.log("Reversee",result.otpEmail.reverse());
        console.log("Original ",otppp," Reverse",o1);

	console.log("Original ",otm," Reverse",o2);

            //if(otpEmail == o1 && otpMobile == o2) {
            if(otpEmail == o1) {
                res.json({'result' : 'true', 'message' : 'OTP Verified'});
                User.findOne({Username:userID}, function (err, user) {
                    if (err) throw err;
                    //console.log(user)
                    user.verified = 'true';
                    user.save();
                })
                console.log("OTP Verified");
                OTP.deleteOne({userId : userID}, () => {})
            } else {
                res.json({'result' : 'false', 'message' : 'Error in OTP'});
                console.log("Error in OTP");
            }
        });
    });

	

//Recieves newPassword and UserID, Checks if User is present and then saves the password to mongodb.
routes.post("/resetPassword", (req, res) => {
    const userID = req.body.userid;
    const newPassword = req.body.newPassword;
    var hash_data=saltHashPassword(newPassword);
    var password=hash_data.passwordHash;
    var salt=hash_data.salt;
    console.log(salt)
    console.log(newPassword)
    console.log(password)
    User.findOne({Username : userID}, (err, user) => {
        if(err) throw err;
        if(user) {
            user.Password = password,
            user.salt = salt
            user.save()
            res.json({'result' : 'true', 'message' : 'Password Successfully Updated'});
        }
        else{
            res.json({'result' : 'false', 'message' : 'Unable to update Password'});
        }
    });
    
});


module.exports = routes;

//Text Local Email Id -> pegido3561@mailseo.net
//Text Local API Key -> FxAaRdvNYys-ZPMxpsUsa0eHxOGn7jXL1P0gesHoNt
function sendSms(otp, mobile) {
    const apiKey = "apiKey=" + utf8.encode("XbGSBb4CEG8-p3We7iMqhjrgSq7OioEI2h4Osdy9wN");
    const message = "&message=" + utf8.encode("Your OTP is " + otp);
    const sender = "&sender=" + utf8.encode("TXTLCL");
    const number = "&numbers=" + utf8.encode(mobile);
    const data = "https://api.textlocal.in/send/?" + apiKey + number + message + sender;
    console.log("SMS");
    request(data, function (error, response, body) {
    console.error('error:', error); 
    console.log('statusCode:', response && response.statusCode); 
    console.log('body:', body);
    });
}

function sendEmail(otp, email) {
	var server = emailjs.server.connect({
		user : "mcdonaldsubway123@gmail.com",
		password : "subwaymcdonald123",
		host : "smtp.gmail.com",
		ssl : true
	})
	
	server.send({
		text : "Your OTP is " + otp + ".\n",
		from : "Arishti <mcdonaldsubway123@gmail.com>",
		to : "<" + email + ">",
		subject : "OTP for Registration"
	},(err,rest) => {
		if(err) console.log(err);
	});
}

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
