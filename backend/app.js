const Sentry = require('@sentry/node');
var express = require('express');
var bodyParser = require('body-parser');
var config = require('./config')
var pool = require('./database');
var app = express();

var drivers = require('./controllers/driver')
var trips = require('./controllers/trips')
var network = require('./utils/network')
var auth = require('./utils/auth')
const { pathToFileURL } = require('url');

const hostname = config.WEBSERVICE_HOST;
const port = config.WEBSERVICE_PORT;

// Sentry.init({ 
//     dsn: 'https://98190676bccf4e75be3f660fa2b19667@o400397.ingest.sentry.io/5258811',
//     attachStacktrace: true,
//     debug: true
// });

// app.use(Sentry.Handlers.requestHandler());
// app.use(bodyParser.json())
// app.use(bodyParser.urlencoded({extended: true}))
app.use(express.json())
app.use(express.urlencoded({extended: true}))

app.use(function(req, res, next) {
    console.log("[REQUEST] " + JSON.stringify(req.path) + " at " + new Date().toISOString());
    res.setHeader('Access-Control-Allow-Origin', '*');
    res.set('Access-Control-Allow-Headers', 'Origin, X-Requested-With, Content-Type, Accept')
    next()
})


// app.post('/api/v1/authTest', function(req, res, next) {
//     console.log('Authorization header: ' + req.get('Authorization'))
//     var authHederValue = req.get('Authorization') 
//     var result = auth.parse(authHederValue)
//     if (result.error) {
//         res.send(network.getErrorMessage(401, result.result))
//     } else {
//         res.send(network.getMessage(200, result))
//     }
// })

// app.get('test', function())

app.post('/api/v1/test', function(req, res) {
    console.log(req.body)
    res.send("complete")
})

app.get('/api/v1', function(req, res, next) {
    res.send('Hello to open car share server!')
})

// TODO there is an issue with post body (it is empty), might be the issue is in REST client
app.post('/api/v1/driver', function(req, res, next) {
    console.log(req.body)
    pool.status()
    drivers.create(req, res)
}) 

app.get('/api/v1/driver', function(req, res, next) {
    pool.status()
    drivers.specific(req, res)
})

app.get('/api/v1/trips', function(req, res) {
    pool.status()
    if (req.query.hasOwnProperty('id')) {
        trips.specific(req, res)
    } else if (req.query.hasOwnProperty('locationFrom')
        && req.query.hasOwnProperty('locationTo')
        && req.query.hasOwnProperty('time')) {
        trips.all(req, res)
    } else if (req.get("Authorization") !== undefined){
        trips.tripsByDriver(req, res)
    } else {
        res.send(network.getErrorMessage(404, "Unsuccess. URL is not found"))
    }
})

app.post('/api/v1/trips/book', function(req, res) {
    pool.status()
    trips.book(req, res)
})

app.post('/api/v1/trips/create', function(req, res) {
    pool.status()
    // TODO process authorization
    // test only
    console.log(req.body)
    req.body.locationFrom = "Kazan"
    req.body.locationTo = "Moscow"
    req.body.date = "Wed, 4 Jul 2001 12:08:56 -0700"
    req.body.availableSeats = "3"
    req.body.driverId = 11
    trips.create(req, res)
})

app.delete('/api/v1/trips', function(req, res) {
    // TODO process authorization
    pool.status()
    trips.delete(req, res)
})

// app.get('/api/v1/trips')

/*
app.get('/v1/category', function(req, res, next) {
    pool.status()
    tasks.all(req, res)
})

app.get('/v1/category/:id', function(req, res, next) {
    pool.status()
    tasks.specific(req, res)
})

app.post('/v1/category/create', function(req, res, next) {
    pool.status()
    tasks.create(req, res)
})

app.post('/v1/category/edit/:id', function(req, res, next) {
    tasks.edit(req, res)
})

app.post('/v1/category/delete/:id', function(req, res, next) {
    tasks.delete(req, res)
});

app.get('/v1/category/:id/course', function(req, res, next) {
    pool.status()
    courses.specific(req, res)
})

app.post('/v1/course/create', function(req, res, next) {
    pool.status()
    courses.create(req, res)
})

app.get('/v1/category/:id/course/:courseId/source', function(req, res, next) {
    pool.status()
    sources.specific(req, res) 
})

app.post('/v1/source/create', function(req, res, next) {
    pool.status()
    sources.create(req, res)
})

app.get('/v1/category/:id/course/:courseId/source/meta', function(req, res, next) {
    pool.status()
    sources.specificWithMeta(req, res) 
})

app.get('/v1/category/:id/course/:courseId/source/:sourceId/like', function(req, res, next) {
    pool.status()
    sources.specific(req, res) 
})

app.post('/v1/like/create', function(req, res, next) {
    console.log("hit /v1/like/create")
    likes.create(req, res)
})
*/

// The error handler must be before any other error middleware and after all controllers
// app.use(Sentry.Handlers.errorHandler());

app.listen(3000)
