//call the packages
var express = require('express'),
    app = express(),
    bodyParser = require('body-parser'),
    mongoose = require('mongoose'),
    fs   = require('fs');

var UsersAuth = require('./usersAuth'),
    UsersBasic = require('./usersBasic'),
    Users = require('./users');

mongoose.connect('mongodb://katya:katya1@ds145369.mlab.com:45369/momserver');

//configure app to use bodyParser to get data from a POST
app.use(bodyParser.urlencoded({extended: true}));
app.use(bodyParser.json());

var port = process.env.PORT || 8080;

//routes for API

var router = express.Router();
//Log after every call
router.use(function(req, res, next) {
    console.log('Something is happening.');
    next();
});

//test route
router.get('/', function(req, res) {
    res.json({message: 'welcome to our API!'});
});

//routes for UsersAuth
router.route('/usersAuth')
    .post(function (req, res) {
        var user = new UsersAuth();
        user.email = req.body.email;
        user.password = req.body.password;

        console.log('Location: ' + user);

        user.save(function (err) {
            if (err)
                res.send(err);

            res.json(user);
        });
    })

    .get(function (req, res) {
        UsersAuth.find(function (err, users) {
            if (err)
                res.send(err);

            res.json(users);

        });
    });


router.route('/usersAuth/:user_id')
    .delete(function(req, res) {
        UsersAuth.remove({
            _id: req.params.user_id
        }, function(err, user) {
            if (err)
                res.send(err);

            res.json({ message: 'Successfully deleted' });
        });
    });


//routes for UsersBasic
router.route('/usersBasic')
    .post(function (req, res) {
        var user = new UsersBasic();
        user.nickname = req.body.nickname;
        user.name = req.body.name;
            user.photo = req.body.photo;

        console.log('Location: ' + user);

        user.save(function (err) {
            if (err)
                res.send(err);

            res.json({message: 'UserBasic created'});
        });
    })

    .get(function (req, res) {
        UsersBasic.find(function (err, users) {
            if (err)
                res.send(err);

            res.json(users);

        });
    });


router.route('/usersBasic/:user_id')
    .get(function(req, res) {
        UsersBasic.findById(req.params.user_id, function(err, user) {
            if (err)
                res.send(err);
            res.json(user);
        });
    })

    .put(function(req, res) {
        UsersBasic.findById(req.params.user_id, function(err, user) {
            if (err)
                res.send(err);

            if (user == null) {
                user = new UsersBasic();
                user._id = req.params.user_id;
            }
            user.nickname = req.body.nickname;
            user.name = req.body.name;
            user.photo = req.body.photo;

            user.save(function(err) {
                if (err)
                    res.send(err);

                res.json({ message: 'User updated!' });
            });

        });
    })

    .delete(function(req, res) {
        UsersBasic.remove({
            _id: req.params.user_id
        }, function(err, user) {
            if (err)
                res.send(err);

            res.json({ message: 'Successfully deleted' });
        });
    });

//routes for User (full info)
router.route('/users')
    .post(function (req, res) {
        var user = new Users();
        user.nickname = req.body.nickname;
        user.name = req.body.name;
        user.bio = req.body.bio;
        user.about = req.body.about;
        user.birth = req.body.birth;
        user.notes = req.body.notes;
        user.trips = req.body.trips;
        user.movies = req.body.movies;
        user.books = req.body.books;
        user.friends = req.body.friends;
        user.photo = req.body.photo;
        user.header = req.body.header;

        if (user.bio == null) { user.bio = ""; }
        if (user.about == null) { user.about = ""; }
        if (user.birth == null) { user.birth = ""; }
        if (user.notes == null) { user.notes = {}; }
        if (user.trips == null) { user.trips = {}; }
        if (user.movies == null) { user.movies = {}; }
        if (user.books == null) { user.books = {}; }
        if (user.friends == null) { user.friends = []; }

        console.log('Location: ' + user);

        user.save(function (err) {
            if (err)
                res.send(err);

            res.json({message: 'User created'});
        });
    })

    .get(function (req, res) {
        Users.find(function (err, users) {
            if (err)
                res.send(err);

            res.json(users);
        });
    });

router.route('/users/:user_id')
    .get(function(req, res) {
        Users.findById(req.params.user_id, function(err, user) {
            if (err)
                res.send(err);
            res.json(user);
        });
    })

    .put(function(req, res) {
        Users.findById(req.params.user_id, function(err, user) {
            if (err)
                res.send(err);

            if (user == null) {
                user = new Users();
                user._id = req.params.user_id;
            }
            user.nickname = req.body.nickname;
            user.name = req.body.name;
            user.bio = req.body.bio;
            user.about = req.body.about;
            user.birth = req.body.birth;
            user.notes = req.body.notes;
            user.trips = req.body.trips;
            user.movies = req.body.movies;
            user.books = req.body.books;
            user.friends = req.body.friends;
            user.photo = req.body.photo;
            user.header = req.body.header;

            if (user.bio == null) { user.bio = ""; }
            if (user.about == null) { user.about = ""; }
            if (user.birth == null) { user.birth = ""; }
            if (user.notes == null) { user.notes = {}; }
            if (user.trips == null) { user.trips = {}; }
            if (user.movies == null) { user.movies = {}; }
            if (user.books == null) { user.books = {}; }
            if (user.friends == null) { user.friends = []; }

            user.save(function(err) {
                if (err)
                    res.send(err);

                res.json({ message: 'User updated!' });
            });

        });
    })

    .delete(function(req, res) {
        Users.remove({
            _id: req.params.user_id
        }, function(err, user) {
            if (err)
                res.send(err);

            res.json({ message: 'Successfully deleted' });
        });
    });

app.use('/api', router);

//start the server
app.listen(port);
console.log('Server running on port ' + port);