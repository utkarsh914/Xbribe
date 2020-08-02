// set the port of our application
// process.env.PORT lets the port be set by Heroku
var port = process.env.PORT || 4444
require('dotenv').config()

const cors = require('cors')

// load express
const path = require('path')
const express = require('express')
const InitiateMongoServer = require('./configs/database')
const bodyparser = require('body-parser')
var ejs = require('ejs')

const flash = require('connect-flash')
const session = require('cookie-session')

const cookieParser = require('cookie-parser')
const cookieEncrypter = require('cookie-encrypter')

var app = express()
// start mongodb server
InitiateMongoServer()

// app.use(cors());
app.use(cors({
  allowedHeaders: ['sessionId', 'Content-Type'],
  exposedHeaders: ['sessionId'],
  credentials: true,
  origin: ['http://localhost:3000', 'http://localhost:5000', 'https://xbribe-react.herokuapp.com'] // here goes Frontend IP
}))

// display these files statically as they are
app.use('/', express.static(__dirname + '/public'))

// set the view engine to ejs
app.set('view engine', 'ejs')
app.set('views', path.join(__dirname, '/views'));

app.use(bodyparser.json())
app.use(bodyparser.urlencoded({ extended: false }))

app.use(cookieParser(process.env.COOKIE_PARSER_SECRET))
app.use(cookieEncrypter(process.env.COOKIE_ENCRYPTER_SECRET))

app.use(session({
  secret: process.env.SESSION_SECRET,
  resave: false,
  saveUninitialized: true
}))
app.use(flash())

// middleware to add username in session and for flash messages
app.use((req, res, next) => {
  res.locals.success_message = req.flash('success_message')
  res.locals.error_message = req.flash('error_message')
  res.locals.error = req.flash('error')
  res.locals.user = req.user || null
  next()
})

// use routes
app.use('/', require('./routes/public'))
app.use('/admin', require('./routes/admin'))
app.use('/ministry', require('./routes/ministry'))
app.use('/user', require('./routes/user'))
app.use('/report', require('./routes/report'))
app.use('/getMinistries', require('./routes/getMinistries'))
app.use('/faker', require('./routes/faker'))

app.listen(port, () => {
  console.log(`Server up on port ${port}`)
})
