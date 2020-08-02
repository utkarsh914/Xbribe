const request = require('request')

const geocode = (address, callback) => {
    const url = 'https://api.mapbox.com/geocoding/v5/mapbox.places/' + address + '.json?access_token=pk.eyJ1IjoicHJhdGlrc3IiLCJhIjoiY2s5NXFpOXRwMDBwdTNrb2NiYmdkNXNsdyJ9.nCXeBQHiT3GiYCqupX9fcg&limit=1'

    request({ url, json: true }, (error, { body }) => {
        //console.log(body.features[0].place_name)
        if (error) {
            callback(undefined, undefined)
        } else if (body.features.length === 0) {
            callback(undefined, undefined)
        } else {
            callback(undefined, {
                latitude: body.features[0].center[1],
                longitude: body.features[0].center[0],
                //location: body.features[0].place_name
            })
        }
    })
}

module.exports = geocode