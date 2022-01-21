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
app.use(express.json())
app.use(express.urlencoded({extended: true}))

app.use(function(req, res, next) {
    console.log("[REQUEST] " + JSON.stringify(req.path) + " at " + new Date().toISOString());
    res.setHeader('Access-Control-Allow-Origin', '*');
    res.setHeader("Content-Type", "application/json; charset=utf-8");
    res.set('Access-Control-Allow-Headers', 'Origin, X-Requested-With, Content-Type, Accept')
    next()
})

app.get('/api/v1', function(req, res, next) {
    res.send('Hello to open car share server!')
})

app.post('/api/v1/driver', function(req, res, next) {
    pool.status()
    drivers.create(req, res)
}) 

app.get('/api/v1/driver', function(req, res, next) {
    pool.status()
    drivers.specific(req, res)
})

app.get('/api/v1/driver/trips', function(req, res, next) {
    pool.status()
    drivers.driverWithTrips(req, res)
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
    console.log(req.body)
    trips.create(req, res)
})

app.delete('/api/v1/trips', function(req, res) {
    pool.status()
    trips.delete(req, res)
})

// The error handler must be before any other error middleware and after all controllers
// app.use(Sentry.Handlers.errorHandler());

app.listen(3000)

