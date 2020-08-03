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
  
  User.findOne({ email, otp }, async (err, found) => {
    if (err) {
      console.log('found error\n', err)
      req.flash('error_message', 'Some error occured!')
      res.redirect('/report/newuser')
      return
    }

    const caseId = await generateCaseID()
    res.cookie('caseId', caseId, cookieConfig)

    if (found) {
      const payload = { 
        user: {
          id: found.id,
          email: found.email,
          role: 'user',
          agent: 'web'
        }
      }
      jwt.sign(payload, process.env.JWT_SECRET, { expiresIn: 1*60*60 }, (err, token) => {
        if (err) throw err
        res.cookie('token', token, cookieConfig)
        return res.redirect('/report')
      })
    } else {
      req.flash('error_message', 'Wrong OTP entered!')
      res.redirect('/report/newuser')
    }
  })
})




// main form to register a report/case
router.get('/', userAuth, (req, res) => {
  Ministry.find({}, async (err, found) => {
    if (err) res.send(err)

    const caseId = req.signedCookies.caseId
    res.render('register/new-case', { ministries: found, caseId: caseId })
  })
})




// handle incoming case register request\
router.post('/', userAuth, async (req, res) => {
  try {
    var form = req.body
    console.log(form, '\n')
    const caseId = req.signedCookies.caseId || await generateCaseID()
    const { id: userId, email, agent } = req.user

    //check for valid email
    if (!['gmail', 'hotmail', 'yahoo', 'bing'].includes(email.split('@')[1].split('.')[0])) {
    	throw new Error('Invalid email. Only emails on gmail, yahoo, hotmail, bing domains are accepted.')
    }

    //check for spam from online model
    const params = new URLSearchParams()
    params.append('message', form.description)
    const url = 'https://spamrandomclassifier.herokuapp.com/predict'
    const response = await axios.post(url, params , {
    	headers: {
    		"content-type": "application/x-www-form-urlencoded"
    	}
    })
    // const params = { "message": form.description }
    // const response = await axios.post(url, params, {
    //   "headers": {
    //     "content-type": "application/json"
    //   }
    // })
    const isspam = response.data.isspam.toString()
    if (isspam === '1') console.log('case was marked as low priority')
    else if (isspam === '2') console.log('case was marked as spam with low priority')
    // if (isspam === '1') {
    //   if (agent === 'app')
    //     return res.status(400).json({ error: true, message: 'Your case was marked as spam and was not registered!' })
    //   else {
    //     req.flash('error_message', 'Your case was marked as spam and was not registered!')
    //     res.clearCookie('token')
    //     .clearCookie('caseId')
    //     .redirect('/report/newuser')
    //     return console.log('Case marked a spam')
    //   }
    // }


    // fetch coordinates when reported from website
    if (agent === 'web') {
      const coordinates = await helpers.geocode(form.address)
      console.log(coordinates)
      form.latitude = coordinates.latitude
      form.longitude = coordinates.longitude
    }

    //check for duplicate description
    // const dupCase = await Case.findOne({ description: form.description })
    // if (dupCase) throw new Error('Duplicate case')

    // stores form data temporarily except files
    var report = new Case({
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
      // if uploaded from web with a single file, a string comes instead of array. So handle that too
      picsArray: (typeof form.picsArray === 'string') ? [form.picsArray] : form.picsArray || [],
      audiosArray: (typeof form.audiosArray === 'string') ? [form.audiosArray] : form.audiosArray || [],
      videosArray: (typeof form.videosArray === 'string') ? [form.videosArray] : form.videosArray || [],
      agent: agent,
      spam: (isspam === '2') ? true : false,
      priority: (isspam !== '0') ? 'Low' : 'Medium'
    })

    //save the case to mongodb
    const savedCase = await report.save()
    console.log('A bribery case has been reported successfully')
    console.log('\n', savedCase)
    // add this case to the user database (push to casesArray)
    const updatedUser = await User.findOneAndUpdate({ _id: userId }, { $push: { casesArray: caseId } }, {new: true})
    console.log(`Added the case ${caseId} to the user db`)

    const userFcmToken = updatedUser.fcmToken
    //send a fcm notification
    if (userFcmToken) {
      helpers.sendFCM({
        title: "Successfully Reported!",
        body: `Your case has been reported successfully. It has been assigned a Case ID '${caseId}'`
      }, null, userFcmToken, 'data')
      .then(response => {
        console.log(response)
      })
      .catch(err => console.log(`Can't send notification to USER: `, err))
    }
    else console.log(`Can't send notification to user, no FCM TOKEN exists`)

    //send notif to Admin web portal
    const adminFcmToken = (await Admin.findOne({ adminId: 'admin' })).fcmToken
    if (adminFcmToken) {
      helpers.sendFCM({
        title: `New case reported: ${caseId}`,
        body: `Dep: ${savedCase.department}\nPlace: ${savedCase.place}, ${savedCase.date.toLocaleDateString()}`,
        click_action: `/admin/cases/case?id=${caseId}`,
        icon: "/images/Xbribe_logo.png"
      },
      {
        case: JSON.stringify(savedCase)
      },
      adminFcmToken, 'notification')
      .then(response => {
        console.log(response)
      })
      .catch(err => console.log(`Can't send notification to ADMIN: `, err))
    }
    else console.log(`Can't send notification to ADMIN, no FCM TOKEN exists`)

    //send mail
    var mailOptions = {
      from: process.env.NODEMAILER_EMAIL,
      to: email,
      subject: 'XBribe: Successfully Reported',
      html: `<p>Hello! Thanks for reporting</p>
            <p>Your case has been registered and an action will be taken on it soon. We'll keep you notified with all further updates.</p>
            <p>Please note the CASE ID <b>${caseId}</b> for future reference.</p>
          `
    }

    transporter.sendMail( mailOptions, (error, info) => {
      if (error) console.log(error)
      else console.log('Email sent: ' + info.response)
    })

    // store into daily stats for chart purpose
    const currentDate = new Date(new Date().setHours(0, 0, 0, 0)).getTime()
    const stats = await DailyStats.findOne({ date: currentDate })
    if (!stats) {
      await new DailyStats({
        date: currentDate,
        received: 1,
        resolved: 0
      }).save()
    } else {
      await DailyStats.findOneAndUpdate({ date: currentDate }, { $inc: { received: 1 } }, {new: true })
    }

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
