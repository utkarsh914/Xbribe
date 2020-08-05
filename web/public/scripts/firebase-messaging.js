// Retrieve an instance of Firebase Messaging so that it can handle background
// messages.
const messaging = firebase.messaging();

// [START get_messaging_object]
// Retrieve Firebase Messaging object.
// const messaging = firebase.messaging();
// [END get_messaging_object]
// [START set_public_vapid_key]
// Add the public key generated from the console here.
messaging.usePublicVapidKey("blablablabla");
// [END set_public_vapid_key]

// IDs of divs that display Instance ID token UI or request permission UI.
const tokenDivId = 'token_div';
const permissionDivId = 'permission_div';

// [START refresh_token]
// Callback fired if Instance ID token is updated.
messaging.onTokenRefresh(() => {
  messaging.getToken().then((refreshedToken) => {
    console.log('Token refreshed.');
    // Indicate that the new Instance ID token has not yet been sent to the
    // app server.
    setTokenSentToServer(false);
    // Send Instance ID token to app server.
    sendTokenToServer(refreshedToken);
    // [START_EXCLUDE]
    // Display new Instance ID token and clear UI of all previous messages.
    resetUI();
    // [END_EXCLUDE]
  }).catch((err) => {
    console.log('Unable to retrieve refreshed token ', err);
    // showToken('Unable to retrieve refreshed token ', err);
  });
});
// [END refresh_token]

// [START receive_message]
// Handle incoming messages. Called when:
// - a message is received while the app has focus
// - the user clicks on an app notification created by a service worker
//   `messaging.setBackgroundMessageHandler` handler.
messaging.onMessage((payload) => {
  console.log('Message received. ', payload);
  // [START_EXCLUDE]
  // Update the UI to include the received message.
  if (payload.notification.title.includes('remind'))
    appendReminder(payload);
  else
    showNotification(payload);
  // [END_EXCLUDE]
});
// [END receive_message]

function resetUI() {
  console.log('running resetUI');
  // clearMessages();
  // showToken('loading...');
  // [START get_token]
  // Get Instance ID token. Initially this makes a network call, once retrieved
  // subsequent calls to getToken will return from cache.
  messaging.getToken().then((currentToken) => {
    console.log('Token received: ', currentToken);
    if (currentToken) {
      sendTokenToServer(currentToken);
      // updateUIForPushEnabled(currentToken);
    } else {
      // Show permission request.
      console.log('No Instance ID token available. Request permission to generate one.');
      // Show permission UI.
      // updateUIForPushPermissionRequired();
      setTokenSentToServer(false);
    }
  }).catch((err) => {
    console.log('An error occurred while retrieving token. ', err);
    // showToken('Error retrieving Instance ID token. ', err);
    setTokenSentToServer(false);
  });
  // [END get_token]
}


function showToken(currentToken) {
  // Show token in console and UI.
  const tokenElement = document.querySelector('#token');
  tokenElement.textContent = currentToken;
}

// Send the Instance ID token your application server, so that it can:
// - send messages back to this app
// - subscribe/unsubscribe the token from topics
function sendTokenToServer(currentToken) {
  // if (!isTokenSentToServer()) {
    console.log('Sending token to server...');

    let url
    if (window.location.href.includes('ministry'))
      url = '/ministry/savefcmtoken'
    else
      url = '/admin/savefcmtoken'
    // TODO(developer): Send the current token to your server.
    $.post(url, { fcmToken: currentToken }, (data, status) => {
      if (status === 'success') {
        console.log('Token sent to server.')
        setTokenSentToServer(true);  
      }
    })
  // } else {
  //   console.log('Token already sent to server so won\'t send it again unless it changes');
  // }

}

function isTokenSentToServer() {
  return window.localStorage.getItem('sentToServer') === '1';
}

function setTokenSentToServer(sent) {
  window.localStorage.setItem('sentToServer', sent ? '1' : '0');
}

function showHideDiv(divId, show) {
  const div = document.querySelector('#' + divId);
  if (show) {
    div.style = 'display: visible';
  } else {
    div.style = 'display: none';
  }
}

function requestPermission() {
  console.log('Requesting permission...');
  // [START request_permission]
  Notification.requestPermission().then((permission) => {
    if (permission === 'granted') {
      console.log('Notification permission granted.');
      // TODO(developer): Retrieve an Instance ID token for use with FCM.
      // [START_EXCLUDE]
      // In many cases once an app has been granted notification permission,
      // it should update its UI reflecting this.
      resetUI();
      // [END_EXCLUDE]
    } else {
      console.log('Unable to get permission to notify.');
    }
  });
  // [END request_permission]
}

function deleteToken() {
  // Delete Instance ID token.
  // [START delete_token]
  messaging.getToken().then((currentToken) => {
    messaging.deleteToken(currentToken).then(() => {
      console.log('Token deleted.');
      setTokenSentToServer(false);
      // [START_EXCLUDE]
      // Once token is deleted update UI.
      resetUI();
      // [END_EXCLUDE]
    }).catch((err) => {
      console.log('Unable to delete token. ', err);
    });
    // [END delete_token]
  }).catch((err) => {
    console.log('Error retrieving Instance ID token. ', err);
    showToken('Error retrieving Instance ID token. ', err);
  });

}



// to show reported case notification
function showNotification(payload) {
  const newcase = JSON.parse(payload.data.case)
  let notif = new Notification(`New case reported: ${newcase.caseId}`, {
    icon: '/images/Xbribe_logo.png',
    body: `Dep: ${newcase.department}\nPlace: ${newcase.place} ${newcase.date.toLocaleString()}`
  })
}



// Add a message to the messages element.
function appendReminder(payload) {
  // remove alert of no reminders
  $('#notif-container .alert-danger').remove()
  const reminder = JSON.parse(payload.data.case)
  $('#count-badge').text(parseInt($('#count-badge').text())+1)
  const a = `
    <div class="notification py-3 px-3 bg-light-" onClick="location.href='/admin/cases/case?id=${reminder.caseId}'">
      <h6 class="mb-1">Case ID: ${reminder.caseId}</h6>
      <p class="mb-0"><b>Dep:</b> ${reminder.department}</p>
      <p class="mb-0"><b>Place:</b> ${reminder.place}, ${reminder.date.toLocaleString()}</p>
      <p class="mb-0"><b>Last Reminded:</b> ${reminder.remindedAt.toLocaleString()}</p>
    </div>
    <hr class="m-0">
  `
  $('#notif-container').prepend(a)
  //play notif sound
  const audio = new Audio("/audio/notification.ogg")
  audio.play()
}

// Clear the messages element of all children.
function clearMessages() {
  const messagesElement = document.querySelector('#messages');
  while (messagesElement.hasChildNodes()) {
    messagesElement.removeChild(messagesElement.lastChild);
  }
}

function updateUIForPushEnabled(currentToken) {
  showHideDiv(tokenDivId, true);
  showHideDiv(permissionDivId, false);
  showToken(currentToken);
}

function updateUIForPushPermissionRequired() {
  showHideDiv(tokenDivId, false);
  showHideDiv(permissionDivId, true);
}

resetUI();
requestPermission();