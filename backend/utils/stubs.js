var util = require('../utils/network')

exports.mockEmptyResponse = function(res) {
    response = JSON.stringify(
        util.getErrorMessage(200, "There is no trip match for this request", [])
    )
    console.log(response)
    return response 
}

exports.mockErrorResponse = function(res) {
    response = JSON.stringify(
        util.getErrorMessage(401, "There is no trip match for this request", [])
    )
    console.log(response)
    return response 
}