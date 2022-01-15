exports.dbToBusinessDriver = function(rows) {
    let result = new Array(rows.length)
    for (id = 0; id < rows.length; id++) {
        result[id] = this.dbToBussinessDriverSingle(row[id])
    }
    return result
}

exports.dbToBussinessDriverSingle = function(row) {
    return {
        id: row.id,
        name: row.name,
        tripsCount: row.tripsCount,
        cell: row.cell,
        secret: row.secret
    }
}

exports.dbToBusinessTripsByDriver = function(rows) {
    let result = new Array(rows.length)
    for (id = 0; id < rows.length; id++) {
        result[id] = this.dbToBusinessTripsByDriverSingle(rows[id])
    }
    return result
} 

exports.dbToBusinessTripsByDriverSingle = function(row) {
    return {
        id: row.tripId,
        locationFrom: row.locationFrom,
        locationTo: row.locationTo,
        date: row.date,
        availableSeats: row.availableSeats,
        driverId: row.driverId,
        driver: {
            id: row.driverId,
            name: row.driverName,
            cell: row.cell,
            tripsCount: row.tripsCount
        }
    }
}

exports.dbToBusinessTrips = function(rows) {
    let result = new Array(rows.length)
    for (id = 0; id < rows.length; id++) {
        result[id] = this.dbToBusinessTripSingle(rows[id])
    }
    return result
}

exports.dbToBusinessTripSingle = function(row) {
    return {
        id: row.id, 
        locationFrom: row.locationFrom,
        locationTo: row.locationTo, 
        date: row.date,
        availableSeats: row.availableSeats,
        driverId: row.driverId
    }
}

exports.dbToBusinessDriverWithTrips = function(rows) {
    if (rows.length === 0) {
        return {}
    } else {
        let driver = {
            id: rows[0].driverId,
            name: rows[0].driverName,
            cell: rows[0].cell,
            tripsCount: rows[0].tripsCount,
        }
        let trips = new Array(rows.length)
        for (id = 0; id < rows.length; id++) {
            trips[id] = {
                id: rows[id].tripId, 
                locationFrom: rows[id].locationFrom,
                locationTo: rows[id].locationTo, 
                date: rows[id].date,
                availableSeats: rows[id].availableSeats,
                driverId: rows[id].driverId
            }
        }
        driver.trips = trips
        return driver
    }
}