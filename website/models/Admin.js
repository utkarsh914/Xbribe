const mongoose = require('mongoose')
const bcrypt = require('bcryptjs')
mongoose.promise = Promise

const adminSchema = mongoose.Schema({
  adminId: {
    type: String,
    required: true
  },
  password: {
    type: String,
    required: true
  },
  signUpDate: {
    type: Date,
    default: Date.now()
  },
  fcmToken: {
    type: String
  }
})

module.exports = mongoose.model('admins', adminSchema)
