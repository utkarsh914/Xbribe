const express = require('express')
const router = express.Router()
// helper fns
const helpers = require('../helpers/helpers')
const DailyStats = require('../models/DailyStats')

const Case = require('../models/Case')
const Admin = require('../models/Admin')
const Ministry = require('../models/Ministry')

var fs = require('fs')
const https = require('https')
const Pdfkit = require('pdfkit')
const transporter = require('../configs/transporter')




// homepage or landing page
router.get('/', async (req, res) => {
  try {
    const posts = await Case.find().sort({ date: -1 }).limit(10)
    const count = await helpers.getcasecount()
    const ministries = await helpers.getMinistries()
    res.render('public/landing-page', { posts: posts, ministries: ministries, count: count })
  }
  catch (e) {
    console.log(e)
    res.send('Some error occured on server side!')
  }
})




// public dashboard having filter sort functions too
router.get('/public', (req, res) => {
  helpers.filter('public', req, res)
})




// check case status
router.get('/status', (req, res) => {
  res.render('public/status')
})




// serve case status
router.post('/status', async (req, res) => {
  try {
    const foundCase = await Case.findOne({ caseId: req.body.caseId, email: req.body.email })

    if (!foundCase) return res.send('Case not found!\nEntered Wrong details!')

    res.send(foundCase.status)
  }
  catch (e) {
    console.log(e)
    res.send('Some error occured on server side. Please try again')
  }
})




// endpoint to send data for drawing the chart on public portal
router.get('/chartdata', async (req, res) => {

  data_received = []
  data_resolved = []

  await DailyStats.find({}).sort({ date: 1 })
    .then((stats) => {
      let received = 0
      let resolved = 0
      stats.forEach(stat => {
        received += stat.received
        resolved += stat.resolved
        data_received.push( { x: stat.date, y: received } )
        data_resolved.push( { x: stat.date, y: resolved } )
      })
    })
    .then(() => res.json( { data_received, data_resolved } ))
})



// process feedback form
router.post('/feedback', (req, res) => {
  const { name, email, message } = req.body
  //send mail
  var mailOptions = {
    from: process.env.NODEMAILER_EMAIL,
    to: process.env.OUR_EMAIL,
    subject: `XBribe: Feedback from a User (${name})`,
    html: `<p><b>Name: </b>${name}</p>
          <p><b>Email: </b>${email}</p>
          <p><b>Message: </b>${message}</p>
          <p><b>Dated: </b>${(new Date()).toLocaleString()}</p>
        `
  }

  transporter.sendMail( mailOptions, (error, info) => {
    if (error) {
      console.log(error)
      res.send('Some error occured on server side')
    }
    else {
      console.log('Email sent: ' + info.response)
      req.flash('success_message', 'Thank You for helping us improve.')
      res.redirect('/')
      // res.send('<h1>Thank You for helping us improve.</h1>')
    }
  })
})


module.exports = router