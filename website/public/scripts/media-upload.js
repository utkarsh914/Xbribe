// $('#reportForm').hide()
// $('#filesUploadForm').show()

//to validate form
function validateForm() {
  var isValid = true;
  $('#reportForm input').add('#reportForm textarea').each(function() {
    if ( $(this).val() === '' )
      if($(this).name !== 'submit') {
        $(this).css("border-bottom", '2px solid red');
        isValid = false;
      }
  });
  return isValid;
}

$('#reportForm input[type="submit"]').click((event)=>{
  event.preventDefault();
  if (validateForm()) {
    $('#alert').hide(50)
    $('#reportForm').hide(300)
    $('#filesUploadForm').show(300)
  }
  else {
    //show error
    $('#alert').html('Make sure all form fields are filled').show(80);
  }
})

//finally submit the form
$('#finalSubmitForm').click((event)=>{
  event.preventDefault();
  $('#reportForm').submit()
})

$('#cancelSubmitForm').click((event)=>{
  event.preventDefault();
  $('#filesUploadForm').hide(300)
  $('#reportForm').show(300)
})

//variables to store pics, audios, and videos files
let caseId = 'democaseID';
var pics;
var audios;
var videos;

const generatePreviewData = (file) => {
  if (!file) return
  const fr = new FileReader();
  return new Promise((resolve, reject) => {
    fr.addEventListener('load', (e) => {
      resolve(fr.result);
    });
    fr.addEventListener('error', (e) => {
      reject();
    });
    fr.readAsDataURL(file);
  });
}


//function to show previews of all images to be uploaded
document.querySelector('input[name="pics"]').oninput = () => {
  $('#picsPreview').html(null)
  pics = document.querySelector('input[name="pics"]').files;
  
  Object.values(pics).forEach(file=>{
    console.log('lastmodified: ', file.lastModified)
    generatePreviewData(file)
      .then((data) => {
        let style = `position: absolute; left: 0; right: 0; top: 0; bottom: 0; width:100%; height: 100%; background-color: rgba(0,0,0,0.6); display: none;`
        let a = `
          <div class='col-sm-4 mb-2'>
            <div class="work img d-flex align-items-center justify-content-center" style="background-image: url(${data}); filte: opacity(30%)" id="${file.lastModified}">
              <div class="overlays" style="${style}"></div>
              <div class="progress" style="width: 90%; display: none; position: relative">
                <div class="progress-bar progress-bar-striped progress-bar-animated" role="progressbar" aria-valuenow="0" aria-valuemin="0" aria-valuemax="100" style="width: 0%"></div>
              </div>
            </div>
          </div>`
        $('#picsPreview').append(a)
      })
  })

  $('#uploadPicsButton').show()
}

//function to show previews of all audios to be uploaded
document.querySelector('input[name="audios"]').oninput = () => {
  $('#audiosPreview').html(null)
  audios = document.querySelector('input[name="audios"]').files;  
  Object.values(audios).forEach(file=>{
    let a = `<li><b>Name:</b> ${file.name} &nbsp;&nbsp;&nbsp; <b>Size: </b>${file.size} Bytes</li>`
    $('#audiosPreview').append(a)
  })
  $('#uploadAudiosButton').show()
}

//function to show previews of all videos name to be uploaded
document.querySelector('input[name="videos"]').oninput = () => {
  $('#videosPreview').html(null)
  videos = document.querySelector('input[name="videos"]').files;
  Object.values(videos).forEach(file=>{
    let a = `<li><b>Name:</b> ${file.name} &nbsp;&nbsp;&nbsp; <b>Size: </b>${file.size} Bytes</li>`
    $('#videosPreview').append(a)
  })
  $('#uploadVideosButton').show()
}


// ========== MEDIA UPLOAD ===============


//fn to batch upload pics, audios, videos one at a time using another fn named uploadFileAsPromise
async function batchUpload(caseId, fileType, fileArray) {
  console.log('batch upload ran for: ',caseId, fileType, fileArray)
  //return if vars not defined
  if (fileArray.length === 0 || !fileType || !caseId) return
  if (!(fileType==='Pics' || fileType==='Audios' || fileType==='Videos')) return
  
  //change button status to upload success and remove onclick event listener
  $(`#upload${fileType}Button`).html('Uploading ..')
  //loop over all files and upload one by one
  for (let i = 0; i < fileArray.length; i++) {
    let path = `${caseId}/${fileType}/${fileArray[i].name}`
    let file = fileArray[i]
    await uploadFileAsPromise(file, path).then((url)=>{
      console.log('File available at', url);
      let field = `<input name="${fileType.toLowerCase()}Array" type="text" value="${url}">`;
      $(`#${fileType}ArrayContainer`).append(field)
    });
  }
  //change button status to upload success and remove onclick event listener
  $(`#upload${fileType}Button`)
  .removeClass("btn-info")
  .addClass("btn-success")
  .html('Upload success!')
  .off('click')
}

//required promise based fn to stop uploading all files at once causing errors
async function uploadFileAsPromise (file, path) {
  return new Promise(function (resolve, reject) {
      var storageRef = firebase.storage().ref(path);
      var task = storageRef.put(file);
      //display progress bar for uploading file
      $(`#${file.lastModified} .progress`).css("display", "flex");
      $(`#${file.lastModified} .overlays`).show(30);

      //Update progress bar
      task.on('state_changed',
        function progress(snapshot){
          var percentage = (snapshot.bytesTransferred / snapshot.totalBytes) * 100;
          console.log(percentage)
          //set width of progress bar
          $(`#${file.lastModified} .progress .progress-bar`).css('width', `${Math.ceil(percentage)}%`);
        },
        function error(err){
          console.log(err);
          reject(err);
        },
        function(){
          task.snapshot.ref.getDownloadURL().then(function(downloadURL) {
            //hide progress bar upon completion
            $(`#${file.lastModified} .progress`).css("display", "none");
            $(`#${file.lastModified} .overlays`).hide(30);
            resolve(downloadURL)
          });
        }
      );
  });
}

//upload the images to firebase on respective button clicks
$('#uploadPicsButton').click(()=>batchUpload(caseId, 'Pics', pics))
$('#uploadAudiosButton').click(()=>batchUpload(caseId, 'Audios', audios))
$('#uploadVideosButton').click(()=>batchUpload(caseId, 'Videos', videos))