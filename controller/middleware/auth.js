const jwt = require('jsonwebtoken')
const User = require('../../model/user_info/User')
const JWT_KEY = "arishti_rohit";
const auth = async(req, res, next) => {
    const token = req.header('Authorization').replace('Bearer ', '')
    console.log("Token",token);
    try{
        const data = jwt.verify(token, JWT_KEY)
        }
    catch(error){
        res.json({ "message":'error'});
    }
    try {
        //const user = await User.findOne({ _id: data._id, 'tokens.token': token })
        const user = await User.findOne({'token': token })
        if (!user) {
            console.log("Didnt get User");
            res.json({ "message":'error'});
            throw new Error()
        }
        console.log("Got User");
        req.user = user
        req.token = token
        next()
    } catch (error) {
        console.log("wIn"+error);
        //res.status(401).send({ error: 'Not authorized to access this resource' })
    }

}
module.exports = auth