
var mongoose = require('mongoose');
var Schema = mongoose.Schema;

var otp =  new Schema({
    "userId" : String,
    "otpEmail" : String,
    "otpMobile" : String,
    "email" : String,
    "mobile" : String,
    "timestamp" : Date
});

var OTP = mongoose.model('OTP',otp);

module.exports = OTP;