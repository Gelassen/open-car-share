exports.dbToBusinessDriver = function(rows) {
    let result = new Array(rows.length)
    for (id = 0; id < rows.length; id++) {
        result[id] = this.dbToBussinessDriverSingle(row[id])
    }
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