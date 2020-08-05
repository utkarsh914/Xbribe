var admin = require("firebase-admin");
// import your private key
var serviceAccount = require("../configs/firebase-adminsdk-config.json")

admin.initializeApp({
  credential: admin.credential.cert(serviceAccount),
  databaseURL: process.env.FCM_DatabaseURL
})

module.exports = async (msg, data, token, type) => {
  try {
    // var payload = {
    //   data: {
    //     title: "yoo working",
    //     body: "Holaaaaaaaaaaaaa"
    //   }
    // }

    //send notification msg or data msg depending on the type
    var payload = (type === 'data') ? { data: msg } : { notification: msg, data: data }
    var options = {
      priority: "high",
      timeToLive: 60 * 60 *24
    }
    
    return admin.messaging().sendToDevice(token, payload, options)
  }
  catch (err) {
    console.log(err)
    return err
  }
}
