# XBribe
An Android App for Bribery Reporting System.

## Project structure 

|-- xbribe 
   |-- data
   |  |-- api
   |  |  |-- ApiHelper 
   |  |  |-- ApiService 
   |-- models 
   |  |  |-- CaseData
   |  |  |-- CaseResponse 
   |  |  |-- LocationDetails 
   |  |  |-- OrganizationResponse 
   |  |  |-- Organizations 
   |  |  |-- TokenResponse
   |  |  |-- User
   |-- prefs 
   |  |  |-- AppDataManager
   |  |  |-- AppDataManagerHelper 
   |-- retrofit 
   |  |  |-- RetrofitProvider 
   |-- service 
   |  |  |-- AddressService 
   |  |  |-- FirebaseMessagingService 
   |--ui
      |  |-- auth
             |-- login
             |   |-- LoginFragemnt  
             |-- register
             |   |-- RegisterFragment
             |--  AuthenticationActivity
             |--  AuthenticationActivityViewModel
      |  |-- function
          |   |-- ImageModel
          |   |-- ImagePreviewAdapter
          |   |-- OTPVerifyFragment 
          |   |-- SpinnerAdapter1
          |   |-- SpinnerAdapter2 
          |   |-- Step_one_Fragment
          |   |-- Step_two_Fragment
          |   |-- SubmissionActivity
          |   |-- SubmissionActivityViewModel
      |  |-- main
          |   |  |-- MainActivity
              |  |-- ReportFragment 
              |  |-- ReportViewModel
              |  |-- SecretCamera 
              |  |-- SecretFragment
              |  |-- SliderAdapter
      |  |-- splash 
             |-- SplashActivity
      |  |-- MyApplication
   |-- Constants 


#Find the apk in XBribe/app/build/outputs/apk/debug
