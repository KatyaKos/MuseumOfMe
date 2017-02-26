var mongoose = require('mongoose');
var Schema = mongoose.Schema;

var UsersSchema = new Schema({
    nickname: String,
    name: String,
    bio: String,
    about: String,
    birth: String,
    notes: {},
    trips: {},
    movies: {},
    books: {},
    friends: [],
    photo: String,
    header: String
});

UsersSchema.virtual('id').get(function(){
    return this._id.toHexString();
});

UsersSchema.set('toJSON', {
    virtuals: true
});

module.exports = mongoose.model('Users', UsersSchema);
