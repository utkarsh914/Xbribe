const express = require('express')
const router = express.Router()
const { check, validationResult } = require('express-validator')
const jwt = require('jsonwebtoken')
const bcrypt = require('bcryptjs')
// auth middleware
const { userAuth } = require('../middleware/auth')
// models
const User = require('../models/User')
const Case = require('../models/Case')




router.post(
	'/signup',
	[
		check('email', 'Please enter a valid email').isEmail(),
		check('password', 'Please enter a valid password').isLength({	min: 6 })
	],
	async (req, res) => {
		const errors = validationResult(req)
		if (!errors.isEmpty()) {
			console.log('error: ', errors.array())
			return res.status(400).json({
				errors: errors.array()
			})
		}

		const { email, password } = req.body

		try {
			let user = await User.findOne({ email })
			/**
			 * if user exists. 2 cases arise
			 * 	1. user had registered previously from app (so, it'll have a password set)
			 * 	2. user registered from web previously (won't have a password set)
			 * 
			 * We have to let the registration proceed if it is case 2, otherwise return.
			 */
			if (user && user.password) {
				console.log('User Already Exists')
				return res.status(400).json({ msg: 'User Already Exists' })
			}

			const salt = await bcrypt.genSalt(10)
			const hashedPassword = await bcrypt.hash(password, salt)

			// handle 2nd case. If it is fresh registration, new user needs to be created
			// otherwise just update the password in case 2 user object
			if (!user) {
				user = new User({	email, hashedPassword	})
			} else {
				user.password = hashedPassword
			}
			
			await user.save()

			const token = user.generateAuthToken('app', 14 * 24 * 60 * 60)
			res.status(200).json({ token })
		}
		catch (err) {
			console.log(err.message)
			res.status(500).send('Error in Saving')
		}
	}
)




router.post(
	'/login',
	[
		check('email', 'Please enter a valid email').isEmail(),
		check('password', 'Please enter a valid password').isLength({
			min: 6
		})
	],
	async (req, res) => {
		const errors = validationResult(req)

		if (!errors.isEmpty()) {
			return res.status(400).json({
				errors: errors.array()
			})
		}

		const { email, password, fcmToken } = req.body
		console.log('FCM TOKEN: ', fcmToken)
		try {
			const user = await User.findOne({ email })
			if (!user) {
				console.log('User Not Exist')
				return res.status(400).json({ message: 'User Not Exist' })
			}

			const isMatch = await user.matchPassword(password)
			if (!isMatch) {
				console.log('Incorrect Password !')
				return res.status(400).json({ message: 'Incorrect Password !' })
			}

			// update new fcm token on user login
			user.fcmToken = fcmToken
			await user.save()

			const token = user.generateAuthToken('app', 14 * 24 * 60 * 60)
			res.status(200).json({ token })
		}
		catch (e) {
			console.error(e)
			res.status(500).json({ message: 'Server Error' })
		}
	}
)




router.get('/me', userAuth, async (req, res) => {
	try {
		// request.user is getting fetched from Middleware after token authentication
		const user = await User.findById(req.user.id)
		res.json(user)
	} catch (e) {
		console.log('Error in Fetching user')
		res.send({ message: 'Error in Fetching user' })
	}
})




// to fetch nearby cases
router.post('/fetchNearbyCases', userAuth, (req, res) => {
	const longitude = parseFloat(req.body.longitude)
	const latitude = parseFloat(req.body.latitude)
	const radius = parseInt(req.body.radius)
	Case.find({
		coordinates: {
			$near: {
				$maxDistance: radius || 1000, //in meters
				$geometry: {
					type: "Point",
					coordinates: [longitude, latitude] //longi, lati
				}
			}
		}
	})
	.then(cases => {
		res.send(cases)
	})
	.catch(e => {
		console.log('Error: ', e.message )
		res.status(400).send('Error fetching nearby cases')
	})
})





// // for notifications if db changes or updates
// router.post('/caseStatus', auth, (req, res) => {
//   Case.findOne({ userId: req.user.id , caseId: req.body.caseId })
//   .then(foundCase => {
//     if (foundCase) res.json(foundCase)
//     else res.status(400).send('Error finding case')
//   })
//   .catch(e => {
//     console.log('Error: ', e.message )
//     res.status(400).send('Error finding case')
//   })
// })


module.exports = router
