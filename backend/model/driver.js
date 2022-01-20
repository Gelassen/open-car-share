const TIMEOUT = 60000;
const { resolve } = require('path/posix');
var pool = require('../database');
var util = require('../utils/network')
var converter = require('../utils/converters/converter');
const { response } = require('express');

exports.getSpecific = function(req, res, authAsTokens) {
    return new Promise( (resolve) => {
        pool.getConnection(function(err, connection) {
            console.log("req.params.id: " + pool.escape(req.query.id))
            connection.query(
                {sql: 'SELECT * FROM drivers WHERE cell = ? AND secret = ?', TIMEOUT},
                [authAsTokens[0], authAsTokens[1]],
                function(error, rows, fields) {
                    if (error != null) {
                        resolve(JSON.stringify(util.getErrorMessage()))
                    } else {
                        console.log("Driver from db: " + JSON.stringify(rows))
                        var response
                        if (rows.length === 1) {
                            response = JSON.stringify(
                                util.getPayloadMessage(
                                    converter.dbToBussinessDriverSingle(rows[0]))
                            )
                        } else {
                            response = JSON.stringify(
                                util.getErrorMessage(401, "There is no driver match for this request, find rows count is " + rows.length)
                            ) 
                        }

                        resolve(response)
                    }
                    connection.release()
                }
            )
        })
    })
}

exports.create = function(req) {
    return new Promise((resolve) => {
        pool.getConnection(function(err, connection) {
            var body = req.body;
            connection.query(
                {sql: 'INSERT INTO drivers SET name = ?, cell = ?, secret = ?, tripsCount = ?', timeout: TIMEOUT}, 
                [body.name, body.cell, body.secret, body.tripsCount], 
                function(error, rows, fields) {
                    if (error != null) {
                        console.log(JSON.stringify(error))
                        resolve(JSON.stringify(util.getErrorMessage()))
                    } else {
                        req.body.uid = rows.affectRows == 0 ? -1 : rows.insertId
                        var response = JSON.stringify(util.getPayloadMessage(req.body))
                        resolve(response)
                    }
                    connection.release()
                }
            )
        });
    })
}

exports.driverWithTrips = function(req, res, authAsTokens) {
    console.log(JSON.stringify(authAsTokens))
    return new Promise((resolve) => {
        pool.getConnection(function(err, connection) {
            connection.query(
                {sql: 'SELECT trips.id as tripId, trips.locationFrom, trips.locationTo, trips.date, trips.availableSeats, trips.driverId,  drivers.id as driverId, drivers.name as driverName, drivers.cell, drivers.tripsCount FROM drivers LEFT JOIN trips ON drivers.id = trips.driverId WHERE drivers.cell = ? AND drivers.secret = ?', timeout: TIMEOUT },
                [authAsTokens[0], authAsTokens[1]],
                function(error, rows, fields) {
                    if (error != null) {
                        resolve(JSON.stringify(util.getErrorMessage()))
                    } else {
                        console.log("Driver with trips from db: " + JSON.stringify(rows))
                        var payload = converter.dbToBusinessDriverWithTrips(rows)
                        var response = JSON.stringify(util.getPayloadMessage(payload))
                        resolve(response)
                    }
                    connection.release()
                }
            )
        })
    }

    )
}
