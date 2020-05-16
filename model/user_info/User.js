var mongoose = require('mongoose');
var Schema = mongoose.Schema;

const validator = require('validator')
const bcrypt = require('bcryptjs')
const jwt = require('jsonwebtoken')

const JWT_KEY = "arishti_rohit";

// create a schema
var userSchema = new Schema({
    Name: {
        type: String,
        required: true
    },
    user_id: {
        type: String,
        required: true
    },
    Username: {
        type: String,
        required: true
    },
    Email: {
        type: String,
        required: true
    },
    Mobile: {
        type: String,
        required: true
    },
    Designation: {
        type: String,
        required: true
    },
    vault_password: {
        type: String,
        required: false
    },
    salt:{
    type:String,
        required: true
    },
    Password: {
        type: String,
        required: true,
        //validate: [arrayLimit,'Size reached']
    },
    Photos: {
        url: [{
            type: String
        }],
        required: false
    },
    
    IMEI:{
        type : String
    },
    
    RegisterTS:{
        type: String,
        require:true
    },
    
    verified:{
        type : String,
        default: "false"
    },

    isonline:{
        type: String,
        default: "false"
    },
    
    notify_islogin:{
        type:Boolean,
        default:false
    },
    
    notification_token : String,
    
    token: {
        type: String,
    },
    
    blocked_users : [{
        user:{
            type: String
        }
    }], 
    
    display_picture: {
        url: {
            type: String,
            default:"null"
        },
        required: false,

        
    },
    
    user_org_id:{ 
        
        type: String 
    },
    
    org_token:{
        type:String
    },

    profile_picture_visibility: {
        type: Number, //(2=Self Organization; 1=Department; 0=None),
        default: 1
    },
    
    org_details_visibility: {
        type: Number, //(2=Self Organization; 1=Department; 0=None),
        default: 0
    },
    
    email_visibility: {
        type: Number, //(2=Self Organization; 1=Department; 0=None),
        default: 0
    },
    
    contact_details_visibility: {
        type: Number, //(2=Self Organization; 1=Department; 0=None),
        default: 0
    },
    
    designation_visibility: {
        type: Number, //(2=Self Organization; 1=Department; 0=None),
        default: 2
    },
    
    notify_message_receipts: {
        type:Boolean, //(1=Yes; 0=No),
        default: true
    },
    
    notify_new_message: {
        type: Boolean,
        default: true
    },
    
    notify_scheduled_meeting: {
        type: Boolean,
        default: true
    },
    
    notify_task_allocation: {
        type: Boolean,
        default: true
    },
                            
    notify_app_update: {
        type: Boolean,
        default: true
    },

    like_application: {
        type:String     // Satisfied, Happy, Fine, Not Good
    },

    like_features: {
        type:String     // Satisfied, Happy, Fine, Not Good
    }, 

    suggestions: {
        type:String
    },

    feedbackImage: {
        type:String
    }

});


function arrayLimit(val) {
    return val.length <= 5;

}

userSchema.pre('save', async function (next) {
    // Hash the password before saving the user model
    const user = this
    if (user.isModified('password')) {
        //user.password = await bcrypt.hash(user.password, 8)
    }
    next()
})

userSchema.methods.generateAuthToken = async function() {
    // Generate an auth token for the user
    console.log("in generat");
    const user = this
    const token = jwt.sign({_id: user._id}, JWT_KEY)
    user.token =token
    await user.save()
    return token
}

userSchema.statics.findByCredentials = async (email, password) => {
    // Search for a user by email and password.
    const user = await User.findOne({ 'Username': email} )
    if (!user) {
        throw new Error({ error: 'Invalid login credentials' })
    }
    //const isPasswordMatch = await bcrypt.compare(password, user.password)
    
    if (!password == user.password) {
        throw new Error({ error: 'Invalid login credentials' })
    }
    return user
}


var User = mongoose.model('User', userSchema);

module.exports = User;