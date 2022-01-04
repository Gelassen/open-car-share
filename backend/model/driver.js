const TIMEOUT = 60000;
const { resolve } = require('path/posix');
var pool = require('../database');
var util = require('../utils/network')

exports.getSpecific = function(req, res) {
    return new Promise( (resolve) => {
        pool.getConnection(function(err, connection) {
            console.log("req.params.id: " + pool.escape(req.query.id))
            connection.query(
                {sql: 'SELECT * FROM drivers WHERE id = ?', TIMEOUT},
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
                        var payload = []
                        payload.push(req.body)
                        var response = util.getPayloadMessage(payload)
                        resolve(response)
                    }
                    connection.release()
                }
            )
        });
    })
}
