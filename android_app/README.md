# XBribe
A bribery reporting app focusing on bringing user privacy, user utility and administrator utility under one place. We hope this app serves the community to build a crime-free society.

## Features

    1)Instant case reporting.
    2)Checking reported cases.
    3)Getting live notification for : a)Case Updates
                                      b)Communication from Administrator Side
    4)Saving cases as drafts to report later.
    5)Fetching information on bribery cases reported nearby.
    6)A unique secrecy focused utility feature: a)Secret Audio Recording
                                                b)Secret Image Capture
    7)An information bulletin to know all the laws related to Bribery Cases.

## Technology Stack

    The App is build using a MVVM Architechture with the frontend designed in XML following Google Material Design. 
    The App backend is implemented with Firebase Storage and SQLite Database and for API requests, Retrofit is used.

## Workflow

    It consists of four activities Splash, Main, Authentication and Submissiom.

    Splash: Welcome Screen, Icon and Illustrations designed in Adobe Photoshop & Illustrator.
    Main: Contains all the links to the different fragments and their functioning. Also, it contains the floating action button redictering to Secret Fragment with the secrecy based features.
    Authentication: Login and Register implemented with Retrofit where backend is working with the help of JWT.
    Submission: a)Step One: Text Details Reporting
                b)Step Two: Media Uploading and URL Generation with the help of Firebase Storage
                c)OTP Verification: Carried out by API calls at the backend.

## Project Structure

    |-- XBribe
        |-- data
            |-- api
            |   |-- ApiHelper.java
            |   |-- ApiService.java
            |-- models
            |   |-- CaseData.java
            |   |-- CaseResponse.java
            |   |-- CollecImages.java
            |   |-- LocationDetails.java
            |   |-- NearbyCaseResponse.java
            |   |-- OrganizationResponse.java
            |   |-- Organizations.java
            |   |-- TokenResponse.java
            |   |-- User.java
            |-- prefs
            |   |-- AppPreferences.java
            |   |-- AppPreferencesHelper.java
            |-- AppDataManager.java
            |-- AppDataManagerHelper.java
        |-- retrofit
        |   |-- RetrofitProvider.java
        |-- services
        |   |-- AddressService.java
        |   |-- FirebaseMessagingService.java
        |-- ui
            |-- auth
                |-- login
                |   |-- LoginFragment.java
                |-- register
                |   |-- RegisterFragment.java
                |-- AuthenticationActivity.java
                |-- AuthenticationActivityViewModel.java
            |-- function
            |   |-- DatabaseHelper.java
            |   |-- ImageModel.java
            |   |-- ImagePreviewAdapter.java
            |   |-- OTPVerifyFragment.java
            |   |-- SpinnerAdapter1.java
            |   |-- SpinnerAdapter2.java
            |   |-- StepOneFragment.java
            |   |-- StepTwoFragment.java
            |   |-- SubmissionActivity.java
            |   |-- SubmissionActivityViewModel.java
            |-- main
                |-- drawers
                    |-- checkcase
                    |   |-- CheckCaseAdapter.java
                    |   |-- CheckCaseFragment.java
                    |   |-- CheckCaseModel.java
                    |   |-- ImageApiService.java
                    |-- notification
                    |   |-- DatabaseHelperNotice.java
                    |   |-- NotificationAdapter.java
                    |   |-- NotificationFragment.java
                    |   |-- NotificationModel.java
                    |-- drafts
                    |   |-- DatabaseSaveDraft.java
                    |   |-- DraftAdapter.java
                    |   |-- DraftFragment.java
                    |   |-- DraftModel.java
                    |-- nearby
                    |   |-- NearbyAdapter.java
                    |   |-- NearbyFragment.java
                    |   |-- NearbyViewModel.java
                    |   |-- ImageApiService.java
                    |-- laws
                    |   |-- LawsAdapter.java
                    |   |-- LawsFragment.java
                    |   |-- LawsModel.java
                    |--contact
                    |   |-- ContactFragment.java
                    |-- aboutus
                    |   |-- AboutUsAdapter.java
                    |   |-- AboutUsFragment.java
                    |   |-- AboutUsModel.java
                |-- MainActivity.java
                |-- ReportFragment.java
                |-- ReportFragmentViewModel.java
                |-- SliderAdapter.java
            |-- splash
            |   |-- SplashActivity.java
            |-- MyApplication.java
        |-- Constants
        
        
### Apk can be found in app/build/outputs/apk/debug
