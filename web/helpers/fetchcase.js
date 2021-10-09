const Case = require('../models/Case')
// helper fn
const getMinistries = require('./getMinistries')

// gathers the details of a case report and renders the page
// type is "admin" or "ministry"
var getcase = async (type, req, res) => {
	const id = req.query.id
	if (!id) return res.send('Error! No case defined')

	const render = (type === 'admin') ? 'admin/case' : 'ministry/case'
	const response = {}

	if (type === 'admin') {
		response.ministries = await getMinistries()
	}

	Case.findOne({ caseId: id }).populate('userId')
	.then(foundCase => {
	if (foundCase) {
		response.c = foundCase
		return res.render(render, response)
	}
	else res.send('Cannot find this case in Database')
	})
	.catch(err => {
		res.send('Cannot find this case in Database')
		console.log('Error fetching case: ', err.message)
	})
}

module.exports = getcase
