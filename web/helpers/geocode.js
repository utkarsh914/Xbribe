const axios = require('axios')

const geocode = async (address) => {
    const url = 'https://api.mapbox.com/geocoding/v5/mapbox.places/' + address + '.json?access_token=pk.eyJ1IjoicHJhdGlrc3IiLCJhIjoiY2s5NXFpOXRwMDBwdTNrb2NiYmdkNXNsdyJ9.nCXeBQHiT3GiYCqupX9fcg&limit=1'

    try {
        const response = await axios.get(url)
        const coordinates = response.data.features[0]
        if (!coordinates)
            throw new Error('Coordinates not found')

        const points = {
            latitude: coordinates.center[1],
            longitude: coordinates.center[0]
        }
        return points
    }
    catch (e) {
        console.log(e)
        return e
    }
}

module.exports = geocode