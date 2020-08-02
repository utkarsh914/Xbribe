const express = require('express')
const router = express.Router()
//for authentication
const jwt = require('jsonwebtoken')
const bcrypt = require('bcryptjs')
// helper functions
const helpers = require('../helpers/helpers')
const transporter = require('../configs/transporter')
// auth middleware
const { ministryAuth } = require('../middleware/auth')
// cookie config
const cookieConfig = require('../configs/cookieConfig')
// import models
const Case = require('../models/Case')
const DailyStats = require('../models/DailyStats')
const Ministry = require('../models/Ministry')


// routes
router.get('/', ministryAuth, (req, res) => {
    res.redirect('/ministry/dashboard')
  })
  
  
  
  
// ministry dashboard having filter sort functions too
  router.get('/dashboard', ministryAuth, (req, res) => {
    // use helper fn
    helpers.filter('ministry', req, res)
  })
  

module.exports = router
