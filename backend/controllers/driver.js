var driver = require('../model/driver')
var auth = require('../utils/auth')
var network = require('../utils/network')
var validator = require('../utils/validator')
var stubs = require('../utils/stubs.js')
const { response } = require('express')

exports.specific = async function(req, res) {
    if (req.get("Authorization") === undefined) {
        res.send(network.getErrorMessage(401, "There is no auth header."))
    } else {
        let authResult = auth.parse(req.get("Authorization"))
        if (authResult.error) {
            let result = network.getErrorMessage(401, authResult.result)
            console.log("[1] Driver response: " + result)
            res.send(result)
        } else {
            let result = await driver.getSpecific(req, res, authResult.result.split(":"))
                .catch(e => network.getErrorMessage(e))
            console.log("[2] Driver response: " + result)
            res.send(result)
        }
    }
    res.end()
}


exports.create = async function(req, res) {
    var result
    if (req.get("Authorization") === undefined) {
        result = JSON.stringify(
            network.getErrorMessage(401, "There is no auth header."))
    } else if (validator.validateDriverPayload(req.body)) {
        // TODO add server error handler or add check for uniqueness and Error [ERR_HTTP_HEADERS_SENT] issue
        let authResult = auth.parse(req.get("Authorization"))
        if (authResult.error) {
            result = JSON.stringify(
                network.getErrorMessage(401, authResult.result, {}))
        } else {
            req.body.secret = authResult.result.split(":")[1]
            result = await driver.create(req)
        }
    } else {
        result = JSON.stringify(
            network.getErrorMessage(400, "Did you forget to add driver as a payload?", {})
        )
    }
    console.log("[driver] create - response " + result)
    res.send(result)
    res.end()
}

exports.driverWithTrips = async function(req, res) {
    if (req.get("Authorization") === undefined) {
        res.send(network.getErrorMessage(401, "There is no auth header."))
    } else {
        let authResult = auth.parse(req.get("Authorization"))
        if (authResult.error) {
            let result = network.getErrorMessage(401, authResult.result)
            console.log("[1] Driver with trips response: " + result)
            res.send(result)
        } else {
            let result = await driver.driverWithTrips(req, res, authResult.result.split(":"))
                .catch(e => network.getErrorMessage(e))
            console.log("[2] Driver with trips response: " + result)
            res.send(result)
        }
    }
    res.end()
} 