const mongoose = require('mongoose')

const UserSchema = new mongoose.Schema({
  email: {
    type: String,
    unique: true,
    required: true,
    dropDups: true
  },
  password: {
    type: String
    // required: true
  },
  fcmToken: {
    type: String,
    // required: true
  },
  casesArray: {
    type: Array
  },
  otp: {
    type: Number
  }
}, {
  timestamps: true
})

// export model user with UserSchema
module.exports = mongoose.model('users', UserSchema)
