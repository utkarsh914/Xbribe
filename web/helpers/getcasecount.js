const Case = require('../models/Case')

module.exports = async () => {
  try {
    const count = {}
    count.received = await Case.countDocuments({})
    count.resolved = await Case.countDocuments({ status: 'resolved' })
    count.pending = count.received - count.resolved
    return count
  } catch (err) {
    return err
  }
}
