var driver = require('../model/driver')
var auth = require('../utils/auth')
var network = require('../utils/network')
var validator = require('../utils/validator')

exports.specific = async function(req, res) {
    if (req.get("Authorization") === undefined) {
        res.send(network.getErrorMessage(401, "There is no auth header."))
    } else {
        let authResult = auth.parse(req.get("Authorization"))
        if (authResult.error) {
            res.send(network.getErrorMessage(401, authResult.result))
        } else {
            let result = await driver.getSpecific(req)
                .catch(e => network.getErrorMessage(e))
            
            res.send(result)
        }
    }
    res.end()
}


exports.create = async function(req, res) {
    // test only
    req.body.name = "Joe Dow"
    req.body.cell = "+79808007090"
    req.body.secret = "clean_blood"
    req.body.tripsCount = "0"

    if (validator.validateDriverPayload(req.body)) {
        // TODO add server error handler or add check for uniqueness and Error [ERR_HTTP_HEADERS_SENT] issue
        result = await driver.create(req)
    } else {
        result = network.getErrorMessage(400, "Did you forget to add driver as payload?")
    }
    res.send(result)
    res.end()
}