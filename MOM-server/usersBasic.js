var mongoose = require('mongoose');
var Schema = mongoose.Schema;

var UsersBasicSchema = new Schema({
    nickname: String,
    name: String,
    photo: String
});

UsersBasicSchema.virtual('id').get(function(){
    return this._id.toHexString();
});

UsersBasicSchema.set('toJSON', {
    virtuals: true
});

module.exports = mongoose.model('UsersBasic', UsersBasicSchema);
