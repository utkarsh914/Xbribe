const express = require('express')
const router = express.Router()
//for authentication
const jwt = require('jsonwebtoken')
const bcrypt = require('bcryptjs')
// helper functions
const helpers = require('../helpers/helpers')
const transporter = require('../configs/transporter')
// auth middleware
const { adminAuth } = require('../middleware/auth')
// cookie config
const cookieConfig = require('../configs/cookieConfig')
// models
const Case = require('../models/Case')
const Admin = require('../models/Admin')
const Ministry = require('../models/Ministry')
const DailyStats = require('../models/DailyStats')
// const User = require('../models/User')




// routes
router.get('/', adminAuth, (req, res) => {
  res.redirect('/admin/dashboard')
})




// admin login page
router.get('/login', (req, res) => {
  res.render('admin/admin-login')
})




// admin sign up page
router.get('/signup', (req, res) => {
  res.render('admin/admin-signup')
})




// handle admin sign up
router.post('/signup', (req, res) => {
  bcrypt.hash(req.body.password, 10, (err, hash) => {
    if (err) throw err

    var adminData = new Admin({
      adminId: req.body.adminId,
      password: hash
    })
    adminData.save((err, datasaved) => {
      if (err) throw err
      res.redirect('/admin/login')
    })
  })
})




// handle admin login
router.post('/login', async (req, res) => {
  try {
    const { adminId, password } = req.body
    //find user in db
    const admin = await Admin.findOne({ adminId })
    //if not found in db
    if (!admin) {
      req.flash('error_message', 'No user found!')
      return res.redirect('/admin/login')
    }
    //compare password
    const isMatch = await bcrypt.compare(password, admin.password)
    if (!isMatch) {
      req.flash('error_message', 'Incorrect password!')
      return res.redirect('/admin/login')
    }
    //create payload
    const payload = {
      user: {
        role: 'admin',
        id: admin.id,
        adminId: admin.adminId
      }
    }
    //generate jwt
    jwt.sign( payload, process.env.JWT_SECRET, { expiresIn: 12*60*60 }, (err, token) => {
      if (err) throw err
      res.cookie('jwt', token, cookieConfig )
      .redirect('/admin/dashboard')
    })

  }
  catch (e) {
    console.log(e)
    res.redirect('/admin/login')
  }
})




// admin dashboard having filter sort functions too
router.get('/cases', adminAuth, (req, res) => {
  helpers.filter('admin', req, res)
})




// admin dashboard having filter sort functions too
router.get('/dashboard', adminAuth, async (req, res) => {
  let ministries = await Ministry.find({})
  res.render('admin/admin-dashboard', { error: false, ministries: ministries })
})




// display case detail
router.get('/cases/case', adminAuth, (req, res) => {
  helpers.getcase('admin', req, res)
})




// for faker
router.get('/manage-database', adminAuth, (req, res) => {
  res.redirect('/faker')
})




// generate case data archive and send the link
router.post('/savefcmtoken', async (req, res) => {
  const fcmToken = req.body.fcmToken
  console.log('Received token: ', fcmToken)
  const admin = await Admin.findOne({ adminId: 'admin' })
  if (admin) {
    admin.fcmToken = fcmToken
    await admin.save()
    console.log('Saved token')
    res.status(200).send('Token Saved')
  }
})




// log out the admin
router.get('/logout', adminAuth, function (req, res) {
  req.flash('success_message', 'You are logged out')
  res.clearCookie('jwt')
  .redirect('/admin/login')
})




// manage ministries
router.get('/manage-ministry', adminAuth, (req, res) => {
  Ministry.find({}, (err, found) => {
    if (err) throw err
    res.render('admin/manage-ministry', { ministries: found })
  })
})




// add ministry
router.post('/add', adminAuth, (req, res) => {
  bcrypt.hash(req.body.password, 10, async (err, hash) => {
    if (err) throw err

    let flag = true
    var ministryId
    while (flag) {
      ministryId = (req.body.ministryName[0] + req.body.ministryName.slice(-1) + '_' + Math.random().toString(36).substr(4, 8)).toUpperCase()
      await Ministry.findOne({ ministryId: ministryId }, (err, found) => {
        if (err) return err
        if (!found) flag = false
      })
    }

    var ministryData = new Ministry({
      ministryId: ministryId,
      ministryName: req.body.ministryName,
      password: hash
    })

    ministryData.save((err, datasaved) => {
      if (err) throw err
      res.redirect('/admin/manage-ministry')
    })
  })
})




// delete a ministry
router.post('/manage-ministry/del/:id', adminAuth, async (req, res) => {
  try {
    // delete that ministry
    const a = await Ministry.deleteOne({ ministryId: req.params.id })
    if (!a.deletedCount) throw new Error('No ministry exists with provided ministry ID!')
    // find all cases of that ministry
    const cases = await Case.find({ ministryId: req.params.id })
    if (cases) {
      // for ministry's each case, find its user and remove the case from user's casesArray
      for (let i=0; i < cases.length; i++) {
        let c = cases[i]
        User.findOneAndUpdate({ _id: c.userId }, { $pull: { casesArray: c.caseId } })
          .catch(err => { if (err) throw err })
      }
    }

    console.log('removed the ministry and pulled from casesArray of users')
    // then delete all cases of that ministry
    await Case.deleteMany({ ministryId: req.params.id })
    console.log('Removed all ministries too containing this ministryId')

    res.redirect('/admin/manage-ministry')
  }
  catch (e) {
    console.log(e)
    res.redirect('/admin/manage-ministry')
  }
})




module.exports = router