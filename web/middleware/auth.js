const jwt = require('jsonwebtoken')


const userAuth = function (req, res, next) {
	// app sends token in header, website sends token in cookies
	const token = req.header('token') || req.signedCookies.token
	if (!token) {
		if (req.method === 'GET' && req.baseUrl === '/report') {
			req.flash('error_message', 'Verify your email first!')
			return res.redirect('/report/newuser')
		}
		else
			return res.status(401).json({ error: true, message: 'Auth Error: No JWT token specified' })
	}
	try {
		const decoded = jwt.verify(token, process.env.JWT_SECRET)
		if (decoded.user.role === 'user') {
			req.user = decoded.user
			next()
		}
		else res.status(500).send({ error: true, message: 'Not allowed' })
	} catch (e) {
		console.error(e)
		res.status(500).send({ error: true, message: 'Invalid Token' })
	}
}


// check login for admin
const adminAuth = function isLoggedIn (req, res, next) {
	const token = req.signedCookies.jwt
	if (!token) {
		return res.redirect('/admin/login')
	}
	try {
		const decoded = jwt.verify(token, process.env.JWT_SECRET)
		if (decoded.user.role === 'admin') {
			req.user = decoded.user
			res.locals.user = req.user
			next()
		}
		else {
			req.flash('error_message', 'Not Allowed! Please login.')
			res.redirect('/admin/login')
		}
	}
	catch (e) {
		console.error(e)
		req.flash('error_message', 'Session timed out! Please login Again.')
		res.redirect('/admin/login')
	}
}


// check login for ministry
const ministryAuth = function isLoggedIn (req, res, next) {
	const token = req.signedCookies.jwt
	if (!token) {
		return res.redirect('/ministry/login')
	}
	try {
		const decoded = jwt.verify(token, process.env.JWT_SECRET)
		if (decoded.user.role === 'ministry') {
			req.user = decoded.user
			res.locals.user = req.user
			next()
		}
		else {
			req.flash('error_message', 'Not Allowed! Please login.')
			res.redirect('/ministry/login')
		}
	}
	catch (e) {
		console.error(e)
		req.flash('error_message', 'Session timed out! Please login Again.')
		res.redirect('/ministry/login')
	}
}


module.exports = {
	userAuth,
	adminAuth,
	ministryAuth
}