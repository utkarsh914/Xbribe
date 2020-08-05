importScripts('https://www.gstatic.com/firebasejs/7.16.1/firebase-app.js');
importScripts('https://www.gstatic.com/firebasejs/7.16.1/firebase-messaging.js');
importScripts("https://www.gstatic.com/firebasejs/7.16.1/firebase-analytics.js");
importScripts("/scripts/firebase-config.js");

const messaging = firebase.messaging();

messaging.setBackgroundMessageHandler(function(payload) {
  console.log("[firebase-messaging-sw.js] Received background message ", payload );
  
  const notification = JSON.parse(payload.data.notification);
  // Customize notification here
  const notificationTitle = notification.title;
  const notificationOptions = {
    body: notification.body,
    icon: notification.icon
  };

  return self.registration.showNotification(
    notificationTitle,
    notificationOptions,
  );
});