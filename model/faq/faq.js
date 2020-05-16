var mongoose = require('mongoose');
var Schema = mongoose.Schema;

var FAQSchema = new Schema({

            title: {
                type: String,
                required: true
            },

            description: {
                type: String,
                required: true
            },
            
            url: {
                type: String       
            },
            
            vote: {
                type: String
            } 
});

var FAQs = mongoose.model('FAQ', FAQSchema);
module.exports = FAQs;