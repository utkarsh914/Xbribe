const express = require('express')
const router = express.Router()
const jwt = require('jsonwebtoken')
const axios = require('axios')
// auth middleware
const { userAuth } = require('../middleware/auth')
// cookie config
const cookieConfig = require('../configs/cookieConfig')
// helper functions
const helpers = require('../helpers/helpers')
const transporter = require('../configs/transporter')
const Case = require('../models/Case')
const User = require('../models/User')
const Admin = require('../models/Admin')
const Ministry = require('../models/Ministry')
const DailyStats = require('../models/DailyStats')




// case register form(useless for web or anything)
router.get('/newuser', (req, res) => {
	Ministry.find({}, async (err, found) => {
		if (err) {
			console.log(err)
			return res.send(err)
		}
		res.render('register/new-user', { ministries: found })
	})
})




// register/verify the user and send a jwt in cookie, then redirect to main page to report the case
router.post('/newuser', async (req, res) => {
	const { email, otp } = req.body
	if (!email || !otp) {
		req.flash('error_message', 'Email and otp should be provided!')
		res.redirect('/report/newuser')
		return
	}

	try {
		const user = await User.findOne({ email, otp });
		const caseId = await generateCaseID()
		res.cookie('caseId', caseId, cookieConfig)

		if (!user) {
			req.flash('error_message', 'Wrong OTP entered!')
			res.redirect('/report/newuser')
			return
		}

		const token = user.generateAuthToken('web', 1 * 60 * 60)
		res.cookie('token', token, cookieConfig)
		return res.redirect('/report')
	}

	catch(err) {
		console.log('found error\n', err)
		req.flash('error_message', 'Some error occured!')
		res.redirect('/report/newuser')
	}
	
		// User.findOne({ email, otp }, async (err, found) => {
		// if (err) {
		// 	console.log('found error\n', err)
		// 	req.flash('error_message', 'Some error occured!')
		// 	res.redirect('/report/newuser')
		// 	return
		// }

		// const caseId = await generateCaseID()
		// res.cookie('caseId', caseId, cookieConfig)

		// if (found) {
		// 	const payload = { 
		// 		user: {
		// 			id: found.id,
		// 			email: found.email,
		// 			role: 'user',
		// 			agent: 'web'
		// 		}
		// 	}
		// 	jwt.sign(payload, process.env.JWT_SECRET, { expiresIn: 1*60*60 }, (err, token) => {
		// 		if (err) throw err
		// 		res.cookie('token', token, cookieConfig)
		// 		return res.redirect('/report')
		// 	})
		// } else {
		// 	req.flash('error_message', 'Wrong OTP entered!')
		// 	res.redirect('/report/newuser')
		// }
		// })
})




// main form to register a report/case
router.get('/', userAuth, (req, res) => {
	Ministry.find({}, async (err, found) => {
		if (err) res.send(err)

		const caseId = req.signedCookies.caseId
		res.render('register/new-case', { ministries: found, caseId: caseId })
	})
})









const checkValidEmail = (email) => {
	if (!['gmail', 'hotmail', 'yahoo', 'bing']
		.includes(email.split('@')[1].split('.')[0]))
		return false;
	return true;
}

// arrays containing addresses of all uploaded files
// if uploaded from web with a single file,
// a string comes instead of array. So handle that too
const extractMediaURLs = (data) => {
	if (typeof data === 'string')
		return [data]
	if (data)
		return data
	else return []
}


const send_FCM_to_User = async (fcmToken, caseId) => {
	if (!fcmToken) {
		console.log(`Can't send notification to user, no FCM TOKEN exists`)
		return
	}
	try {
		const message = {
			title: "Successfully Reported!",
			body: `Your case has been reported successfully. It has been assigned a Case ID '${caseId}'`
		}
		const response = await helpers.sendFCM(message, null, fcmToken, 'data')
		console.log("Notification sent to user app.")
		// console.log(response)
	}
	catch (err) {
		console.log(`Error while sending notification to USER: `, err)
	}
}


const send_FCM_to_Admin = async (savedCase) => {
	try {
		const fcmToken = (await Admin.findOne({ adminId: 'admin' })).fcmToken
		if (!fcmToken) {
			console.log(`Can't send notification to ADMIN, no FCM TOKEN exists`)
			return;
		}
		const message = {
			title: `New case reported: ${savedCase.caseId}`,
			body: `Dep: ${savedCase.department}\nPlace: ${savedCase.place}, ${savedCase.date.toLocaleDateString()}`,
			click_action: `/admin/cases/case?id=${savedCase.caseId}`,
			icon: "/images/Xbribe_logo.png"
		}
		const data = {
			case: JSON.stringify(savedCase)
		}
		const response = await helpers.sendFCM(message, data,	fcmToken, 'notification')
		console.log("Notification sent to ADMIN.")
		// console.log(response)
	}
	catch (err) {
		console.log(`Error while sending notification to ADMIN: `, err)
	}
}



const sendSuccessEmail = async (email, caseId) => {
	try {
		const mailOptions = {
			from: process.env.NODEMAILER_EMAIL,
			to: email,
			subject: 'XBribe: Successfully Reported',
			html: `<p>Hello! Thanks for reporting</p>
						<p>Your case has been registered and an action will be taken on it soon. We'll keep you notified with all further updates.</p>
						<p>Please note the CASE ID <b>${caseId}</b> for future reference.</p>
					`
		}
		const { response } = await transporter.sendMail( mailOptions )
		console.log(`Email sent to ${email}. ${response}`)
	}
	catch (err) {
		console.log(`Error while sending email to ${email}\n`, error)
	}
}


const update_daily_stats = async () => {
	try {
		const currentDate = new Date(new Date().setHours(0, 0, 0, 0)).getTime()
		const stats = await DailyStats.findOne({ date: currentDate })
		if (!stats) {
			await new DailyStats({
				date: currentDate, received: 1, resolved: 0
			})
			.save()
		}
		else {
			await DailyStats.findOneAndUpdate(
				{ date: currentDate }, { $inc: { received: 1 } }, { new: true })
		}
		console.log("Daily stats updated.")
	}
	catch (err) {
		console.log("Error updating daily stats\n", err)
	}
}


/**
 * 
 * @param {string} message 
 * @returns { boolean, string } isspam, priority
 * explanation: 
 * 	0 = message is ham
 * 	1 = random text, spelling mistakes etc.
 * 	2 = Abusive language or Spam
 * 
 * 	isspam = true, only for 2, false otherwise
 * 	priority = 'Medium' for 1, low otherwise
 */
const checkSpamAndPriority = async (message) => {
	//check for spam from online model
	try {
		const params = new URLSearchParams()
		params.append('message', message)
		const url = 'https://spamrandomclassifier.herokuapp.com/predict'
		const response = await axios.post(url, params , {
			headers: { "content-type": "application/x-www-form-urlencoded" }
		})
		const prediction = response.data.prediction

		let isspam = false, priority = 'Medium'
		if (prediction == 2) isspam = true
		if (prediction != 0) priority = 'Low'
		return { isspam, priority }
	}
	catch (error) {
		console.log("Error occured while fetching spam check.")
		console.log(error)
		return { isspam: false, priority: "Medium" }
	}
}





// handle incoming case register request\
router.post('/', userAuth, async (req, res) => {
	try {
		const form = req.body
		// console.log(form, '\n')
		const caseId = req.signedCookies.caseId || await generateCaseID()
		const { id: userId, email, agent } = req.user

		//check for valid email
		if (checkValidEmail(email) == false) {
			throw new Error('Invalid email. Only emails on gmail, yahoo, hotmail, bing domains are accepted.')
		}

		// determine spam status and priority using API
		const { isspam, priority } = await checkSpamAndPriority(form.description)
		console.log(`Spam: ${isspam}, Priority: ${priority}`)

		// fetch coordinates when reported from website
		if (agent === 'web') {
			const coordinates = await helpers.geocode(form.address)
			form.latitude = coordinates.latitude
			form.longitude = coordinates.longitude
		}

		// stores form data temporarily except files
		const report = new Case({
			caseId: caseId,
			userId: userId,
			email: email,
			ministryId: form.ministryId,
			department: (form.department === 'Not sure') ? 'Not Specified' :  form.department,
			date: new Date(),
			name: form.name,
			officialName: form.officialName || 'Not Specified',
			place: form.place,
			coordinates: {
				type: "Point",
				coordinates: [parseFloat(form.longitude), parseFloat(form.latitude)]
			},
			location: {
				address: form.address,
				pin: form.pin,
				district: form.district || 'Not Specified'
			},
			description: form.description,
			// arrays containing addresses of all uploaded files
			picsArray: extractMediaURLs(form.picsArray),
			audiosArray: extractMediaURLs(form.audiosArray),
			videosArray: extractMediaURLs(form.videosArray),
			agent: agent,
			spam: isspam,
			priority: priority
		})

		//save the case to mongodb
		const savedCase = await report.save()
		console.log('A bribery case has been reported successfully')
		console.log('\n', savedCase)
		// add this case to the user database (push to casesArray)
		const updatedUser = await User.findOneAndUpdate(
			{ _id: userId }, { $push: { casesArray: caseId } }, { new: true })
		console.log(`Added the case ${caseId} to the user db`)
		
		
		// send a FCM notification to the user on their app
		await send_FCM_to_User(updatedUser.fcmToken, caseId)
		//send notif to Admin web portal
		await send_FCM_to_Admin(savedCase)
		//send success e-mail
		await sendSuccessEmail(email, caseId)
		// store into daily stats for chart purpose
		await update_daily_stats()

		// send the response to client based on the its agent type
		res.clearCookie('token')
			.clearCookie('caseId')

		if (agent === 'app')
			res.json({ error: false, message: 'success', data: savedCase })
		else if (agent === 'web')
			res.render('register/success', { report: savedCase })

	}
	catch (e) {
		console.log(e)
		res.status(400).json({ error: true, message: e })
	}
})




//generate otp
router.post('/sendotp', async (req, res) => {
	const email = req.body.email
	if (!email) res.status(400).send('Some error Occured!')
	console.log(email)

	try {
		if (!['gmail', 'hotmail', 'yahoo', 'bing'].includes(email.split('@')[1].split('.')[0])) {
			throw new Error('Invalid email. Only emails on gmail, yahoo, hotmail, bing domains are accepted.')
		}
		var user = await User.findOne({ email })
		if (!user) {
			user = await new User({ email }).save()
		}
		var otp = Math.floor( 100000 + Math.random() * 900000 )
		user.otp = otp
		await user.save()
	}
	catch (e) {
		console.log(e)
		return res.status(422).send(e.message)
	}

	var mailOptions = {
		from: process.env.NODEMAILER_EMAIL,
		to: email,
		subject: 'XBribe: Verify OTP',
		html: `Your OTP is <b>${otp}</b>`
	}

	transporter.sendMail( mailOptions, (error, info) => {
		if (error) {
			console.log(error)
			return res.status(400).send('Some error Occured!')
		} else {
			console.log('Email sent: ' + info.response)
			res.status(200).send('Otp sent')
		}
	})

})




//verify otp
router.post('/verifyotp', async (req, res) => {
	const { email, otp } = req.body
	if (!email || !otp) return res.status(400).send('Either email or otp not provided!')

	try {
		const user = await User.findOne({ email })
		if (!user) {
			return res.status(400).send(`Email doesn't exist`)
		}
		if (parseInt(otp) === user.otp) {
			return res.status(200).send('OTP Verified')
		}
		else return res.status(400).send(`Wrong OTP`)
	}
	catch (e) {
		return res.status(400).send('Some error Occured!')
	}
})




// fn to get a random unused case ID
async function generateCaseID () {
	let flag = true
	let caseId
	while (flag) {
		// generate a random case ID
		caseId = Math.random().toString(36).substr(2, 10).toUpperCase()
		await Case.findOne({ caseId: caseId }, (err, found) => {
			if (err) return err
			if (!found) flag = false
		})
	}
	return caseId
}




module.exports = router
