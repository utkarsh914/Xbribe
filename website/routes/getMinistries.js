const express = require('express')
const router = express.Router()
const Ministry = require('../models/Ministry')

router.get('/', (req, res) => {
  Ministry.find({}, (err, found) => {
    if (err) {
      return res.status(400).json({
        error: true,
        message: "Can't find ministries, some error occured"
      })
    }
    var ministries = []
    found.forEach(element => {
      ministries.push({
        ministryId: element.ministryId,
        ministryName: element.ministryName,
        departments: element.departments
      })
    })
    res.json({
      error: false,
      message: 'success',
      data: ministries
    })
  })
})

module.exports = router
