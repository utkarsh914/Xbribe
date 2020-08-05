# XBribe

## Features

    1. Easy to  use:
        A feasible interface for user to choose  and categorize the organisation and easily fill up the necessary details.
        
    2. Complete Anonymity:
        A secured database to provide anonymity to users, while ensuring the authenticity of the reported case by sending the necessary details of the user to the administrator and the relevant authority.
        
    3. Track case:
        A public portal tracking the status of the ongoing cases and it's settlement.
        
    4.Realtime notification
        A real time notification to user using the app such that he/she gets proper updates about their cases. And also to ministry in their portal, if a case occurs in their department and the case is accepted by the admin.
        
    5.In app recording :
        A private audio recorder & camera that gets triggered on clicking the main screen floating button and provides a secret interface for them.
        
    6. Future cases prediction:
        Using ML model, create analysis report and predictive models using past trends for reporting and prediction of future cases that may occur.
        
    7.Spam Detection:
        Using ML, creating a spam classifier model that checks cases for spam and thus strengthening the authentication and safety of the data reported.
        
    8. Nearby Case Detection:
        A nearby cases detection feature which gives the user information about all the cases nearby him/her.
        
    9. Chatbot:
        Integrating a Chatbot in the website for further assistance to the user and an informative laws section in app for making users aware of the laws regarding bribery cases.
        
    10.Prioritization: 
        Integrating the spam model with a prioritization feature, that assigns cases with random text in the description to be of low priority and cases with no spams as medium priority. Priority options are also manually available to the administrator. If abusive language or spamming is found in the description, the case would be placed in the spam section.

## Thirs party libraries, frameworks or APIs used.
    All of them mentioned below are either open-source or free to use.

## For Website:

### Mapbox API
    https://docs.mapbox.com/api/

### Kommunicate
    https://docs.kommunicate.io/docs/api-detail

### Firebase
    https://firebase.google.com/docs/storage/web/start

### jQuery
    https://api.jquery.com/

### Bootstrap
    https://getbootstrap.com/

### Magnific Popup
    https://dimsemenov.com/plugins/magnific-popup/

## For App:

### Firebase by Google Open Source
    https://github.com/firebase/
    
### Pinview Library by GoodieBag
    https://github.com/GoodieBag/Pinview

### Circular Image View by Mikhael Lopez
    https://github.com/lopspower/CircularImageView

### Butterknife by Jake Wharton
    https://github.com/JakeWharton/butterknife

### Image Slider by Smarteist
    https://github.com/smarteist/Android-Image-Slider
    
### Mock API for Image Fetching
    https://5ee7467352bb0500161fd73a.mockapi.io/api/v1/images/image/

### Glide Library by Bumptech
    https://github.com/bumptech/glide

### Retrofit by Square
    https://github.com/square/retrofit

### Hidden Camera by Keval Patel 
    https://github.com/kevalpatel2106/android-hidden-camera


## Note: To enable firebase notifications in website and app
	1. Configure web/public/scripts/firebase-config.js as per your credentials
	2. Configure PublicVapidKey in web/public/scripts/firebase-messaging.js on line 11 [for notifications on web].
	3. Put your firebase-admin-sdk config in web/firebase and import in web/helpers/sendFCM on line 3.
	4. Now you're good to go.
