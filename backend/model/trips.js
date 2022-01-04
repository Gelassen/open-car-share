const TIMEOUT = 60000;
const { resolve } = require('path/posix');
var pool = require('../database');
var util = require('../utils/network')

exports.getSpecific = function(req, res) {
    return new Promise( (resolve) => {
        pool.getConnection(function(err, connection) {
            console.log("req.params.id: " + pool.escape(req.query.id))
            connection.query(
                {sql: 'SELECT * FROM trips WHERE id = ?', TIMEOUT},
                [req.query.id],
                function(error, rows, fields) {
                    if (error != null) {
                        resolve(JSON.stringify(util.getErrorMessage()))
                    } else {
                        resolve(JSON.stringify(util.getPayloadMessage(rows)))
                    }
                    connection.release()
                }
            )
        })
    })
}

exports.all = function(req, res) {
    return new Promise( (resolve) => {
        pool.getConnection(function(err, connection) {
            connection.query(
                {sql: 'SELECT * FROM trips WHERE locationFrom = ?, locationTo = ?, time = ?', TIMEOUT},
                [req.query.locationFrom, req.query.locationTo, req.query.time],
                function(error, rows, fields) {
                    if (error != null) {
                        resolve(JSON.stringify(util.getErrorMessage()))
                    } else {
                        resolve(JSON.stringify(util.getPayloadMessage(rows)))
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
            connection.query(
                {sql: 'SELECT * FROM trips INNER JOIN drivers ON trips.driverId = drivers.id WHERE drivers.name = ? AND drivers.secret = ?', TIMEOUT},
                [authAsTokens[0], authAsTokens[1]],
                function(error, rows, fields) {
                    if (error != null) {
                        resolve(JSON.stringify(util.getErrorMessage()))
                    } else {
                        resolve(JSON.stringify(util.getPayloadMessage(rows)))
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
                        var response = util.getErrorMessage(payload)
                        console.log("[trip] response: " + JSON.stringify(response))
                        resolve(JSON.stringify(response))
                    } else {
                        console.log("[trip] there is no error")
                        var payload = util.getServiceMessage(util.statusFailed)
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
            connection.beginTransaction(function(error) {
                if (error) { throw err; }
                connection.query(
                    {sql: "SELECT count(*) as cnt FROM trips INNER JOIN drivers ON trips.driverId = drivers.id WHERE drivers.name = ? AND drivers.secret = ?"},
                    [authAsTokens[0], authAsTokens[1]], 
                    function (error, rows, fields) {
                        if (error) {
                            return connection.rollback(function() {
                                throw error;
                            })
                        }
                        var isUserAuthrorizedToOperateWithThisTrip = rows[0].cnt == 1
                        console.log(isUserAuthrorizedToOperateWithThisTrip)
                        if (isUserAuthrorizedToOperateWithThisTrip) {
                            connection.query(
                                {sql: 'DELETE FROM trips WHERE trips.id = ?'},
                                [req.query.id],
                                function(errorSecond, rows, fields) {
                                    if (errorSecond) {
                                        return connection.rollback(function() {
                                            throw errorSecond;
                                        })
                                    }
                                    connection.commit(function(errorThird) {
                                        if (errorThird) {
                                            return connection.rollback(function() {
                                                throw errorThird
                                            })
                                        } else {
                                            var payload = util.getServiceMessage(util.statusSuccess)
                                            var response = util.getPayloadMessage(payload)
                                            resolve(response)
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