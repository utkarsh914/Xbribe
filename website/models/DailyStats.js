const mongoose = require('mongoose')
mongoose.promise = Promise

const statsSchema = mongoose.Schema({
  date: { type: Number, required: true, index: 1 },
  received: { type: Number, default: 0 },
  resolved: { type: Number, default: 0 }
})

module.exports = mongoose.model('daily_stats', statsSchema)
