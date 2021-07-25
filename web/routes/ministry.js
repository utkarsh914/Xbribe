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




// generate case data archive and send the link
router.post('/savefcmtoken', ministryAuth, async (req, res) => {
	const fcmToken = req.body.fcmToken
	console.log('Received token: ', fcmToken)
	
	if (!req.user) {
		return res.status(400).send('Error')
	}

	const m = await Ministry.findOne({ ministryId: req.user.ministryId })
	if (m) {
		m.fcmToken = fcmToken
		await m.save()
		console.log('Saved token')
		res.status(200).send('Token Saved')
	}
})




// login form
router.get('/login', (req, res) => {
	res.render('ministry/ministry-login')
})
	
	
	
	
// handle login
router.post('/login', async (req, res) => {
	try {
		const { ministryId, password } = req.body
		//find user in db
		const ministry = await Ministry.findOne({ ministryId })
		//if not found in db
		if (!ministry) {
			req.flash('error_message', 'No user found!')
			return res.redirect('/ministry/login')
		}
		//compare password
		const isMatch = await bcrypt.compare(password, ministry.password)
		if (!isMatch) {
			req.flash('error_message', 'Incorrect password!')
			return res.redirect('/ministry/login')
		}
		//create payload
		const payload = {
			user: {
				role: 'ministry',
				id: ministry.id,
				ministryId: ministry.ministryId,
				ministryName: ministry.ministryName
			}
		}
		//generate jwt
		jwt.sign( payload, process.env.JWT_SECRET, { expiresIn: 12*60*60 }, (err, token) => {
			if (err) throw err
			res.cookie('jwt', token, cookieConfig )
			.redirect('/ministry/dashboard')
		})

	}
	catch (e) {
		console.log(e)
		res.redirect('/ministry/login')
	}
})




// display case detail
router.get('/dashboard/case', ministryAuth, (req, res) => {
	// use helper fn
	helpers.getcase('ministry', req, res)
})




// logout
router.get('/logout', ministryAuth, function (req, res) {
	req.flash('success_message', 'You are logged out!')
	res.clearCookie('jwt')
	.redirect('/ministry/login')
})




module.exports = router
