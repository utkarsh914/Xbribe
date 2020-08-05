$(document).ready(() => {
  var caseId = document.querySelector('#caseId').innerHTML
  console.log(caseId)

  // //submiiting form 1 opens form 2
  // $("#form1").submit(()=>{
  //   let email = $('#form1 input[name=email]').val()
  //   // console.log(email, caseId)
  //   // ajax call to get the userId
  //   $.post("/report/newuser", {email: email}, (data, status)=>{
  //     if (status==='success' && data.error===false) {
  //       console.log(status, data)
  //       $('#form1').hide()
  //       $('#form2').show()
  //       $('#filesUploadForm').show()
  //       $('#form2 input[name=userId]').val(data.userId)
  //       $('#form2 input[name=email]').val(email)
  //     }
  //   })
  // })

  var pics = []
  var audios = []
  var videos = []
  // put all selected files in pics array to loop through them
  document.querySelector('#filesUploadForm input[name=pics]').addEventListener('change', (e) => {
    pics = e.target.files
  })
  document.querySelector('#filesUploadForm input[name=audios]').addEventListener('change', (e) => {
    audios = e.target.files
  })
  document.querySelector('#filesUploadForm input[name=videos]').addEventListener('change', (e) => {
    videos = e.target.files
  })

  // fn to batch upload pics, audios, videos one at a time using another fn named uploadFileAsPromise
  async function batchUpload (caseId, fileType, fileArray) {
    console.log('batch upload ran for: ', caseId, fileType, fileArray)
    // return if vars not defined
    if (fileArray.length === 0 || !fileType || !caseId) return
    if (!(fileType === 'pics' || fileType === 'audios' || fileType === 'videos')) return
    // loop over all files and upload one by one
    for (let i = 0; i < fileArray.length; i++) {
      const path = `${caseId}/${fileType}/${fileArray[i].name}`
      const file = fileArray[i]
      await uploadFileAsPromise(file, path).then((url) => {
        console.log('File available at', url)
        const field = `<input name="${fileType}Array" type="text" value="${url}">`
        $(`#${fileType}ArrayContainer`).append(field)
        // document.querySelector(`#form2 input[name=${fileType}Array]`).value += `, ${url}`;
      })
    }
    document.querySelector(`#upload${fileType}Status`).innerHTML = 'Upload success!'
  }
  // required promise based fn to stop uploading all files at once causing errors
  async function uploadFileAsPromise (file, path) {
    return new Promise(function (resolve, reject) {
      var storageRef = firebase.storage().ref(path)
      var task = storageRef.put(file)

      // Update progress bar
      task.on('state_changed',
        function progress (snapshot) {
          var percentage = (snapshot.bytesTransferred / snapshot.totalBytes) * 100
          console.log(percentage)
        },
        function error (err) {
          console.log(err)
          reject(err)
        },
        function () {
          task.snapshot.ref.getDownloadURL().then(function (downloadURL) {
            resolve(downloadURL)
          })
        }
      )
    })
  }

  // upload the images to firebase on respective button clicks
  $('#uploadPicsBtn').click(() => batchUpload(caseId, 'pics', pics))
  $('#uploadAudiosBtn').click(() => batchUpload(caseId, 'audios', audios))
  $('#uploadVideosBtn').click(() => batchUpload(caseId, 'videos', videos))
})
