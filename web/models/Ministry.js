const mongoose = require('mongoose')
// const bcrypt = require('bcryptjs');
mongoose.promise = Promise

const ministrySchema = mongoose.Schema({
	ministryId: {
		type: String,
		unique: true,
		required: true
	},
	ministryName: {
		type: String,
		required: true
	},
	departments: {
		type: Array,
		default: []
	},
	fcmToken: {
		type: String
	},
	password: {
		type: String,
		required: true
	},
	signUpDate: {
		type: Date,
		default: Date.now()
	}
})

module.exports = mongoose.model('ministries', ministrySchema)
