exports.validateDriverPayload = function(reqBody) {
    return this.validateString(reqBody.name) 
        && this.validateString(reqBody.cell) 
        // && this.validateString(reqBody.secret)
        && this.validateString(reqBody.tripsCount) 
}

exports.validateString = function(str) {
    return str !== '' && str !== null && str !== undefined
}