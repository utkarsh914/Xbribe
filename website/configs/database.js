const mongoose = require('mongoose')
// mongoose doesn't use promises by default
mongoose.Promise = global.Promise

var uristring =
    process.env.MONGODB_URI ||
    process.env.MONGOHQ_URL ||
    'mongodb://localhost:27017/bribery'

const InitiateMongoServer = () => {
  mongoose.connect(uristring, {
    useCreateIndex: true,
    useNewUrlParser: true,
    useUnifiedTopology: true,
    useFindAndModify: false
  })
  mongoose.connection
    .once('open', () => { console.log('Mongoose Connected!') })
    .on('error', (err) => { console.log('Mongoose Couldnt connect:', err) })
}

module.exports = InitiateMongoServer
