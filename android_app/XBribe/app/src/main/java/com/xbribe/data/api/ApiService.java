package com.xbribe.data.api;

import com.google.android.gms.common.api.Response;
import com.xbribe.data.models.CaseData;
import com.xbribe.data.models.CaseResponse;
import com.xbribe.data.models.LocationDetails;
import com.xbribe.data.models.NearbyCaseResponse;
import com.xbribe.data.models.OrganizationResponse;
import com.xbribe.data.models.TokenResponse;
import com.xbribe.data.models.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;

public interface ApiService
{

    @POST("user/login")
    Call<User> executeLogin(@Body User user);

    @POST("user/signup")
    Call<User> executeSignup(@Body User user);

    @Headers("Content-Type:application/json")
    @GET("user/me")
    Call<TokenResponse> getDetails(@Header("token") String token);

    @GET("getministries")
    Call<OrganizationResponse> getOrganizations();
    
    @FormUrlEncoded
    @POST("report")
    Call<CaseData> reportCase(@Header("token") String token,
                              @Field("ministryId") String orgId,
                              @Field("department") String department,
                              @Field("officialName") String officialName,
                              @Field("name") String name,
                              @Field("place") String place,
                              @Field("address") String address,
                              @Field("pin") String pin,
                              @Field("latitude") String latitude,
                              @Field("longitude") String longitude,
                              @Field("description") String description,
                              @Field("picsArray") ArrayList<String> picsArray,
                              @Field("audiosArray") ArrayList<String> audiosArray,
                              @Field("videosArray") ArrayList<String> videosArray);

    @FormUrlEncoded
    @POST("user/fetchNearbyCases")
    Call<List<NearbyCaseResponse>> getNearbyCases(  @Header("token") String token,
                                                    @Field("latitude") Double latitude,
                                                    @Field("longitude") Double longitude,
                                                    @Field("radius") Integer radius);

    @FormUrlEncoded
    @POST("report/sendotp")
    Call<ResponseBody> sendOtp(@Field("email") String email);

    @FormUrlEncoded
    @POST("report/verifyotp")
    Call<ResponseBody> verifyOtp(@Field("email") String email,
                                 @Field("otp") Integer otp);
}
