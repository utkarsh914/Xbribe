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




module.exports = router