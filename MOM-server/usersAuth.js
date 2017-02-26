var mongoose = require('mongoose');
var Schema = mongoose.Schema;

var UsersAuthSchema = new Schema({
    email: String,
    password: String
});

UsersAuthSchema.virtual('id').get(function(){
    return this._id.toHexString();
});

UsersAuthSchema.set('toJSON', {
    virtuals: true
});

module.exports = mongoose.model('UsersAuth', UsersAuthSchema);
