const Ministry = require('../models/Ministry')

module.exports = async () => {
	try {
		var ministries = {}
		var found = await Ministry.find({})
		found.forEach(element => {
			ministries[element.ministryId] = element.ministryName
		})
		return ministries
	} catch (err) {
		return err
	}
}
