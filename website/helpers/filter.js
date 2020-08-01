const Case = require('../models/Case')
// const Ministry = require('../models/Ministry')
//helper fn
const getMinistries = require("./getMinistries")
const { query } = require('express')

var filterPost = async (type, req, res) => {

	let path = ''
	if (type==="public") path = 'public/public-dashboard'
	else if (type==="admin") path = 'admin/cases-list'
	else if (type==="ministry") path = 'ministry/ministry-dashboard'

  var response = {
    "error": true, "message" : "Error fetching data",
    "pageNo": 1, "totalPages": 1,
    "posts": []
  }

  //make an object containing all ministries key value as id and name
  if (type!=="ministry") {
	  response.ministries = await getMinistries()
  }

  var filter = {
    $and: []
  }

  const searchQuery = req.query.query //|| req.signedCookies.query
  if (searchQuery) {
    const regex = new RegExp(searchQuery, 'i')
    const s =  [
      { 'location.address': regex },
      { 'location.pin': regex },
      { 'location.district': regex },
      { caseId: regex },
      { email: regex },
      { ministryId: regex },
      { department: regex },
      { name: regex },
      { place: regex }
    ]
    filter["$and"].push({ '$or': s })
    //set cookie
    // res.cookie('query', searchQuery, cookieConfig)
  }

  if (type==="ministry")	filter['$and'].push({ ministryId: req.user.ministryId })
  else if (req.query.ministryId && req.query.ministryId !== "any") filter['$and'].push({ ministryId: req.query.ministryId })
  
  if (req.query.status && req.query.status !== "any") filter['$and'].push({ status: req.query.status })

  var datefilter ={
    "$gte": (req.query.from) ? new Date(new Date(req.query.from).setHours(00, 00, 00)) : new Date(new Date('1970-01-01').setHours(00, 00, 00)),
    "$lt": (req.query.to) ? new Date(new Date(req.query.to).setHours(23, 59, 59)) : new Date()
  }
  // if date to < date from
  if (datefilter["$lt"] < datefilter["$gte"]) {
    response.message = "Date(From) can't be greater than Date(to)"
    return res.render(path, response)
  }

  filter['$and'].push({ date: datefilter })

  let pageNo = parseInt(req.query.pageNo) || 1
  if (pageNo<1) pageNo = 1
  let size = parseInt(req.query.size) || 20
  let orderBy = req.query.orderBy || "new"
  let totalPages = 1

  Case.countDocuments(filter, (err, count)=>{
    if (err) return err;
    //if zero records found
    if (count===0) {
      response.error = true
      response.message = "No records found for this query!"
      return res.render(path, response)
    }
    
    totalPages = Math.ceil(count / size)  
    
    Case.find(filter)
    .sort({date: orderBy==="new" ? -1: 1})
    .skip(size*(pageNo-1))
    .limit(size)
    .then(async (data) => {
      //query to send to db for pagination and all other rendering
      response.error = false
      if (searchQuery) {
        response.message = `Found ${count} results for "${searchQuery}"`
      }else {
        response.message = `Successfully fetched ${count} results in ${totalPages} pages`
      }
      response.pageNo = pageNo
      response.pageSize = size
      response.totalPages = totalPages
      response.posts = data

      if (type==="admin") {
        // find cases which are reminded but not resolved yet
        const reminders = await Case.find({ resolvedAt: null, remindedAt: { $ne: null } }).sort({ remindedAt: -1 }).limit(10)
        response.reminders = reminders
        res.render(path, response)
      }
      else res.render(path, response)

    }).catch(err=>{
      if (err) return res.render(path, response)
    })

  })  
}

module.exports = filterPost;