const express = require('express')
const router = express.Router()
// helper fns
const helpers = require('../helpers/helpers')
const DailyStats = require('../models/DailyStats')

const Case = require('../models/Case')
const Admin = require('../models/Admin')
const Ministry = require('../models/Ministry')

// var fs = require('fs')
// const https = require('https')
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

		const ministryName = (await Ministry.findOne({ ministryId: foundCase.ministryId })).ministryName
		const doc = new Pdfkit()
		// doc.pipe(fs.createWriteStream('demoFile.pdf')) // write to PDF
		doc.pipe(res)
		// add stuff to PDF
		doc.fontSize(16).font('Courier-Bold').text('SUBMITTED REPORT', { align: 'center' })
		.moveDown()
		.fontSize(15).font('Helvetica-Bold').text(`Ministry: ${ministryName}`, { align: 'center' })
		.moveDown()
		.fontSize(13).text(`Department: ${foundCase.department}`)
		.moveDown()
		.fontSize(13).text(`Institute/Organization Name: ${foundCase.name}`)
		.moveDown()
		.fontSize(13).text(`Concerned Person/Govt Official Name: ${foundCase.officialName}`)
		.moveDown()

		.fontSize(12).text(`Date: ${new Date(foundCase.date)}`)

		.text(`Place: ${foundCase.place}`)
		.text(`Address: ${foundCase.location.address}, ${foundCase.location.pin}`)
		.text(`Location Coordinates: ${foundCase.coordinates.coordinates[1]} Deg N, ${foundCase.coordinates.coordinates[0]} Deg E`, {
			link: `https://www.google.com/maps/search/${foundCase.coordinates.coordinates[1]},${foundCase.coordinates.coordinates[0]}`
		})
		.text(`Case ID: ${foundCase.caseId}`)
		.fillColor('blue').text(`Status: ${foundCase.status}`)
		.fillColor('black').text(`Priority: ${foundCase.priority}`)
		.moveDown()

		.text('Reporting person Info:')
		.fontSize(11).font('Helvetica').text(`User ID: ${foundCase.userId}`)
		.font('Helvetica').text(`User email: ${foundCase.email}`)
		.moveDown()
		.fontSize(12).font('Helvetica-Bold').text('Description:')
		.fontSize(11).font('Helvetica').text(foundCase.description, { align: 'justify' })
		.moveDown()

		.fontSize(12).font('Helvetica-Bold').text('Count of attached Media:')
		.fontSize(11).font('Helvetica').text(`Pictures: ${foundCase.picsArray.length}`)
		.font('Helvetica').text(`Audio Recordings: ${foundCase.audiosArray.length}`)
		.font('Helvetica').text(`Video Clips: ${foundCase.videosArray.length}`)
		.moveDown()

		// finalize the PDF and end the stream
		.end()
	}
	catch (e) {
		console.log(e)
		res.send('Some error occured on server side. Please try again')
	}
})

// display reminder form
router.get('/remind', (req, res) => {
	res.render('public/remind')
})

// remind admin for action
router.post('/remind', async (req, res) => {
	const { caseId, email } = req.body
	if ( !caseId || !email ) return res.status(400).send('Provide both caseID and email')

	try {
		const c = await Case.findOne({ caseId, email })
		if (!c) {
			req.flash('error_message', 'Case not found!')
			return res.redirect('/remind')
		}
		if (c.remindedAt) {
			const difference = ((new Date()).getTime() - c.remindedAt.getTime()) / (24*3600*1000)
			if (difference < 14) {
				req.flash('error_message', 'You can send a reminder at intervals of 14 days only.')
				return res.redirect('/remind')
			}
		}
		c.remindedAt = new Date()
		await c.save()
		req.flash('success_message', 'A reminder was sent regarding your case.')
		res.redirect('/remind')

		//send notif to Admin web portal
		const adminFcmToken = (await Admin.findOne({ adminId: 'admin' })).fcmToken
		if (adminFcmToken) {
			helpers.sendFCM({
				title: `New reminder for ${caseId}`,
				body: `Dep: ${c.department}\nPlace: ${c.place}, ${c.date.toLocaleDateString()}`,
				click_action: `/admin/cases/case?id=${caseId}`,
				icon: "/images/Xbribe_logo.png"
			},
			{
				case: JSON.stringify(c)
			},
			adminFcmToken, 'notification')
			.then(response => {
				console.log(response)
			})
			.catch(err => console.log(`Can't send notification to ADMIN: `, err))
		}
		else console.log(`Can't send notification to ADMIN, no FCM TOKEN exists`)
	}
	catch (e) {
		console.log(e)
		req.flash('error_message', `Couldn't send a reminder due to some problem on server side.`)
		res.redirect('/remind')
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
		}
	})
})


module.exports = router