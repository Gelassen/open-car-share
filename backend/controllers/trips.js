var trips = require('../model/trips.js')
var driver = require('../controllers/driver')
var network = require('../utils/network')
var auth = require('../utils/auth.js')
var stubs = require('../utils/stubs.js')

exports.specific = async function(req, res) {
    let result = await trips.getSpecific(req)
    res.send(result)
    res.end()
}

exports.all = async function(req, res) {
    let result = await trips.all(req)
    res.send(result)
    res.end()
}

exports.tripsByDriver = async function(req, res) {
    if (req.get("Authorization") === undefined) {
        res.send(network.getErrorMessage(401, "There is no auth header."))
    } else {
        let authResult = auth.parse(req.get("Authorization"))
        if (authResult.error) {
            res.send(network.getErrorMessage(401, authResult.result))
        } else {
            let result = await trips.tripsByDriver(req, res, authResult.result.split(":"))
                .catch(e => network.getErrorMessage(e))
            
            res.send(result)
        }
    }
    res.end()
}

exports.book = async function(req, res) {
    // let result = await trips.book(req, res)
    network.getErrorMessage(403, "Method trips/book is not supported")
    res.send(result)
    res.end()
}

exports.create = async function(req, res) {
    let result = await trips.create(req, res) 
    res.send(result)
    res.end()
}

exports.delete = async function(req, res) {
    if (req.get("Authorization") === undefined) {
        res.send(network.getErrorMessage(401, "There is no auth header."))
    } else {
        let authResult = auth.parse(req.get("Authorization"))
        if (authResult.error) {
            res.send(network.getErrorMessage(401, authResult.result))
        } else {
            console.log("[trips_delete] " 
            + JSON.stringify(req.body) + " " 
            + JSON.stringify(authResult) + " " 
            + JSON.stringify(req.query))
            let result = await trips.delete(req, res, authResult.result.split(":"))
                .catch(e => JSON.stringify(err, Object.getOwnPropertyNames(e)))
            
            res.send(result)
        }
    }
    res.end()
}