const nodemailer = require('nodemailer')

var transporter = nodemailer.createTransport({
	// host: 'smtp.gmail.com',
	// port: 465,
	// secure: true,
	service: 'gmail',
	auth: {
		user: process.env.NODEMAILER_EMAIL,      //email id
		pass: process.env.NODEMAILER_PASSWORD     //my gmail password
	}
})

module.exports = transporter