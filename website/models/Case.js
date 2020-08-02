const mongoose = require('mongoose')
mongoose.promise = Promise
const { ObjectId } = mongoose.Schema.Types;

const caseSchema = mongoose.Schema({
  caseId: {
    type: String,
    required: true
  },
  userId: {
    type: ObjectId,
    ref: 'users',
    required: true
  },
  email: {
    type: String,
    required: true
  },
  ministryId: {
    type: String,
    required: true
  },
  department: {
    type: String
  },
  name: {
    type: String,
    required: true
  },
  officialName: {
    type: String
  },
  place: {
    type: String,
    required: true
  },
  coordinates: {
    type: { type: String },
    coordinates: []
  },
  location: {
    address: { type: String },
    pin: { type: String },
    district: { type: String }
  },
  description: {
    type: String,
    required: true
  },
  picsArray: {
    type: Array
  },
  audiosArray: {
    type: Array
  },
  videosArray: {
    type: Array
  },
  status: {
    type: String,
    default: 'submitted'
  },
  resolvedAt: {
    type: Date
  },
  remindedAt: {
    type: Date,
    required: false,
    index: 1
  },
  priority: {
    type: String,
    default: 'Medium'
  },
  folder: {
    type: String,
    default: 'none'
  },
  notifications: [ {
    title: { type: String },
    message: { type: String, required: true },
    date: { type: Date, default: Date.now() }
  }],
  date: {
    type: Date,
    required: true,
    index: 1
  },
  agent: {
    type: String,
    enum: ['app', 'web'],
    required: true
  },
  spam: {
    type: Boolean,
    default: false
  }
})

caseSchema.index({
  coordinates: "2dsphere"
})

const Case = mongoose.model('cases', caseSchema)
module.exports = Case
