const TIMEOUT = 60000;
const { resolve } = require('path/posix');
var pool = require('../database');
var util = require('../utils/network')
var converter = require('../utils/converters/converter')

exports.getSpecific = function(req, res) {
    return new Promise( (resolve) => {
        pool.getConnection(function(err, connection) {
            console.log("req.params.id: " + pool.escape(req.query.id))
            connection.query(
                {sql: 'SELECT trips.id as tripId, trips.locationFrom, trips.locationTo, trips.date, trips.availableSeats, trips.driverId,  drivers.id as driverId, drivers.name as driverName, drivers.cell, drivers.tripsCount FROM trips INNER JOIN drivers  ON trips.driverId = drivers.id WHERE trips.id = ?', TIMEOUT},
                [req.query.id],
                function(error, rows, fields) {
                    var response
                    if (error != null) {
                        resolve(JSON.stringify(util.getErrorMessage([])))
                    } else {
                        if (rows.length === 1) {
                            response = JSON.stringify(
                                util.getPayloadMessage(
                                    converter.dbToBusinessTripsByDriver(rows)
                                )
                            )
                        } else {
                            response = JSON.stringify(
                                util.getErrorMessage(401, "There is no trip match for this request, find rows count is " + rows.length, [])
                            ) 
                        }
                        console.log("[trips] get specific trip response " + response)
                        resolve(response)
                    }
                    connection.release()
                }
            )
        })
    })
}

exports.all = function(req, res) {
    console.log("[trips] get all - " + JSON.stringify(req.query))
    return new Promise( (resolve) => {
        pool.getConnection(function(err, connection) {
            connection.query(
                {sql: 'SELECT * FROM trips WHERE locationFrom = ? AND locationTo = ? AND date >= ?', TIMEOUT},
                [req.query.locationFrom, req.query.locationTo, req.query.time],
                function(error, rows, fields) {
                    if (error != null) {
                        var response = util.getErrorMessage(500, JSON.stringify(error), [])
                        console.log("[trip] response: " + JSON.stringify(response))
                        resolve(JSON.stringify(response))
                    } else {
                        console.log("[trip] response: " + JSON.stringify(rows))
                        var payload = converter.dbToBusinessTrips(rows)
                        var response = JSON.stringify(util.getPayloadMessage(payload))
                        console.log("[trip] response: " + response)
                        resolve(response)
                    }
                    connection.release()
                }
            )
        })
    })
}

exports.tripsByDriver = async function(req, res, authAsTokens) {
    return new Promise( (resolve) => {
        pool.getConnection(function(err, connection) {
            console.log("[action] trips by driver call")
            connection.query(
                {sql: 'SELECT trips.id as tripId, trips.locationFrom, trips.locationTo, trips.date, trips.availableSeats, trips.driverId,  drivers.id as driverId, drivers.name as driverName, drivers.cell, drivers.tripsCount FROM trips INNER JOIN drivers  ON trips.driverId = drivers.id WHERE drivers.cell = ? AND drivers.secret = ?', TIMEOUT},
                [authAsTokens[0], authAsTokens[1]],
                function(error, rows, fields) {
                    if (error != null) {
                        console.log("[action] trips by driver sql response, there is an error")
                        var response = util.getErrorMessage(500, JSON.stringify(error), [])
                        console.log("[trip] response: " + JSON.stringify(response))
                        resolve(JSON.stringify(response))
                    } else {
                        console.log("[action] trips by driver sql response, there is no error")
                        var response = util.getPayloadMessage(converter.dbToBusinessTripsByDriver(rows))
                        console.log(response)
                        resolve(JSON.stringify(response))
                    }
                    connection.release()
                }
            )
        })
    })
}

exports.book = async function(req, res) {
    return new Promise( (resolve) => {
        pool.getConnection(function(err, connection) {
            throw Exception("Method trips/book is not supported")
        })
    })
}

exports.create = async function(req, res) {
    return new Promise( (resolve) => {
        pool.getConnection(function(err, connection) {
            var body = req.body
            connection.query(
                {sql: 'INSERT INTO trips SET locationFrom = ?, locationTo = ?, date = ?, availableSeats = ?, driverId = ?;', timeout: TIMEOUT}, 
                [body.locationFrom, body.locationTo, body.date, body.availableSeats, body.driverId], 
                function(error, rows, fields) {
                    console.log("[trip] create trip result")
                    if (error != null) {
                        console.log("[trip] there is an error")
                        console.log(JSON.stringify(error))
                        var payload = util.getServiceMessage(util.statusFailed, JSON.stringify(error))
                        var response = util.getErrorMessage(500, error.code, payload)
                        console.log("[trip] response: " + JSON.stringify(response))
                        resolve(JSON.stringify(response))
                    } else {
                        console.log("[trip] there is no error")
                        var payload = util.getServiceMessage(util.statusSuccess, "")
                        var response = util.getPayloadMessage(payload)
                        console.log("[trip] response: " + JSON.stringify(response)) 
                        resolve(JSON.stringify(response))
                    }
                    connection.release()
                }
            )
        })
    })
}

exports.delete = async function(req, res, authAsTokens) {
    return new Promise( (resolve) => {
        pool.getConnection(function(err, connection) {
            console.log("[trip] delete - start")
            connection.beginTransaction(function(error) {
                if (error) { throw err; }
                connection.query(
                    {sql: "SELECT count(*) as cnt FROM trips INNER JOIN drivers ON trips.driverId = drivers.id WHERE drivers.cell = ? AND drivers.secret = ?"},
                    [authAsTokens[0], authAsTokens[1]], 
                    function (error, rows, fields) {
                        console.log("[trip] delete - get trips count for driver")
                        if (error) {
                            return connection.rollback(function() {
                                throw error;
                            })
                        }
                        console.log(JSON.stringify(rows))

                        var isUserAuthrorizedToOperateWithThisTrip = rows[0].cnt > 0
                        console.log(isUserAuthrorizedToOperateWithThisTrip)
                        if (isUserAuthrorizedToOperateWithThisTrip) {
                            console.log("[trip] delete - start delete query")
                            connection.query(
                                {sql: 'DELETE FROM trips WHERE trips.id = ?'},
                                [req.query.id],
                                function(errorSecond, rows, fields) {
                                    if (errorSecond) {
                                        return connection.rollback(function() {
                                            throw errorSecond;
                                        })
                                    }
                                    console.log("[trip] delete - attempt to confirm result ")
                                    connection.commit(function(errorThird) {
                                        if (errorThird) {
                                            console.log("[trip] delete - rollback")
                                            return connection.rollback(function() {
                                                throw errorThird
                                            })
                                        } else {
                                            console.log("[trip] delete - prepare and send the response")
                                            var payload = util.getServiceMessage(util.statusSuccess, "")
                                            var response = util.getPayloadMessage(payload)
                                            console.log(JSON.stringify(response))
                                            resolve(JSON.stringify(response))
                                        }
                                        if (err) {
                                            return connection.rollback(function() {
                                                throw err;
                                            });
                                        }
                                      })
                                })
                        } else {
                            return connection.rollback(function() {
                                var response = util.getMessage(500, "Unsucceed. Does this user autorized to cancel this trip? Or is it canceled already?")
                                resolve(response)
                            })
                        }                       
                    }
                    ) 
            })
        })
    })
}