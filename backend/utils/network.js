exports.getMessage = function(code, message) {
    return {code: code, message: message, result: {}};
}

exports.getPayloadMessage = function(payload) {
    return {code: "200", message: "", result: payload}
}

exports.getErrorMessage = function() {
    return {code: 500, message: "unsuccess", result: {}}
}

exports.getErrorMessage = function(payload) {
    return {code: 500, message: "unsuccess", result: payload}
}

exports.getErrorMessage = function(code, message) {
    return {code: code, message: message, result: {}}
}

exports.statusSuccess = 1
exports.statusFailed = 0
exports.getServiceMessage = function(statusCode) {
    return {status: statusCode, message: ""}
}