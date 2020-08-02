# XBribe
A bribery reporting app.

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
                    |-- drafts
                    |   |-- DatabaseSaveDraft.java
                    |   |-- DraftAdapter.java
                    |   |-- DraftFragment.java
                    |   |-- DraftModel.java
                    |-- laws
                    |   |-- LawsAdapter.java
                    |   |-- LawsFragment.java
                    |   |-- LawsModel.java
                    |-- nearby
                    |   |-- NearbyAdapter.java
                    |   |-- NearbyFragment.java
                    |   |-- NearbyViewModel.java
                    |   |-- ImageApiService.java
                    |-- notification
                    |   |-- DatabaseHelperNotice.java
                    |   |-- NotificationAdapter.java
                    |   |-- NotificationFragment.java
                    |   |-- NotificationModel.java
                |-- MainActivity.java
                |-- ReportFragment.java
                |-- ReportFragmentViewModel.java
                |-- SliderAdapter.java
            |-- splash
            |   |-- SplashActivity.java
            |-- MyApplication.java
        |-- Constants