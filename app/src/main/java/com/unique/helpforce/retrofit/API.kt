package com.unique.helpforce.retrofit

import com.unique.helpforce.ServiceProvider
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.*


interface API
{
    @FormUrlEncoded
    @POST("/auth/register")
    fun registerUser(
        @Field("phone") phone: String,
        @Field("password") password: String,
        @Field("confirmPassword") confirmPassword: String,
        @Field("firstName") firstName: String,
        @Field("lastName") lastName: String,
        @Field("email") email: String,
        @Field("gender") gender: String,
        @Field("dob") dob: String
    ): Call<DefaultResponse>


    @FormUrlEncoded
    @POST("/auth/login")
    fun loginUser(
        @Field("phone") phone: String,
        @Field("password") password: String
    ): Call<DefaultResponse>

    @FormUrlEncoded
    @POST("/auth/forget_password")
    fun ResetPassword(
        @Field("phone") phone: String,
        @Field("email") email: String
    ): Call<DefaultResponse>

    @FormUrlEncoded
    @PUT("/user/location")
    fun UserLocation(
        @Header("Authorization") authHeader: String,
        @Field("_id") _id: String,
        @Field("longitude") longitude: Double,
        @Field("latitude") latitude: Double
    ): Call<DefaultResponse>


    @FormUrlEncoded
    @POST("/help")
    fun helpRequest(
        @Header("Authorization") authHeader: String,
        @Field("description") description: String,
        @Field("longitude") longitude: Double,
        @Field("latitude") latitude: Double,
        @Field("fnfAllowed") fnfAllowed: Boolean,
        @Field("nearbyUsersAllowed") nearbyUsersAllowed: Boolean,
        @Field("urgencyLevel") urgencyLevel: String
    ): Call<HelpRequestResponse>


    @FormUrlEncoded
    @POST("/help/respond")
    fun helpRequestRespond(
        @Header("Authorization") authHeader: String,
        @Field("requestId") requestId: String,
        @Field("action") action: String,
    ): Call<RequestRespondResponse>


    @GET("/user/profile/{userId}")
    suspend fun getProfile(
        @Header("Authorization") authHeader: String,
        @Path("userId") userId: String
    ) : retrofit2.Response<GetProfileResponse>

    @FormUrlEncoded
    @POST("/services/request")
    fun sendRequesttoServiceProvider(
        @Header("Authorization") authHeader: String,
        @Field("serviceProviderId") serviceProviderId: String,
        @Field("serviceName") serviceName: String
    ) : Call<RequesttoServiceProviderResponse>

    @GET("/services/request/received/:status?")
    suspend fun showlistofServiceNeededUsers(
        @Header("Authorization") authHeader: String,
    ) : retrofit2.Response<ListOfServiceNeededUsersResponse>

    @GET("/services/request/sent/:status?")
    suspend fun showlistofServiceProvidedUsers(
        @Header("Authorization") authHeader: String,
    ) : retrofit2.Response<ListOfServiceProviderUsersResponse>

    @GET("/services")
    suspend fun showlistofServices(
        @Header("Authorization") authHeader: String,
    ) : retrofit2.Response<List<ListOfServices>>

    @FormUrlEncoded
    @POST("/services/register")
    fun registerPredefinedServices(
        @Header("Authorization") authHeader: String,
        @Field("name") name: String
    ) : Call<RegisterServicesResponse>

    @FormUrlEncoded
    @POST("/services/request/respond")
    fun respondtoServicesRequest(
        @Header("Authorization") authHeader: String,
        @Field("aid") aid: String,
        @Field("status") status: String
    ) : Call<RespondtoServicesRequestResponse>

    @FormUrlEncoded
    @POST("/ratings")
    fun setRating(
        @Header("Authorization") authHeader: String,
        @Field("ratedTo") ratedTo: String,
        @Field("score") score: Int
    ) : Call<SetRatingResponse>

    @GET("/ratings/{userId}")
    suspend fun getRating(
        @Header("Authorization") authHeader: String,
        @Path("userId") userId: String
    ) : retrofit2.Response<GetRatingResponse>


    @GET("/services/providers/{service}")
    suspend fun fetchallServiceProviders(
        @Header("Authorization") authHeader: String,
        @Path("service") service: String
    ) : retrofit2.Response<List<ServiceProvider>>

    @FormUrlEncoded
    @POST("/user/friend")
    fun addafriend(
        @Header("Authorization") authHeader: String,
        @Field("phone") phone: String,
    ) : Call<RegisterServicesResponse>

    @Multipart
    @POST("/user/upload/image")
    fun uploadImage(
        @Header("Authorization") authHeader: String,
        @Part("name") ImageName: RequestBody,
        @Part file: MultipartBody.Part
    ): Call<ImageUploadResponse>

    @GET("/help/requests/sent")
    suspend fun showlistofHelpProvidedUsers(
        @Header("Authorization") authHeader: String,
    ) : retrofit2.Response<ListOfHelpProviderUsersForSent>

    @GET("/user/friends")
    suspend fun showlistofOwnFriends(
        @Header("Authorization") authHeader: String,
    ) : retrofit2.Response<ListOfOwnFriends>

    @GET("/services/user")
    suspend fun showlistofOwnServices(
        @Header("Authorization") authHeader: String,
    ) : retrofit2.Response<ListOfOwnServices>

    @DELETE("/user/friend")
    suspend fun deletefnf(
        @Header("Authorization") authHeader: String,
        @Field("phone") phone: String,
    ): Response<RequesttoServiceProviderResponse>


    @FormUrlEncoded
    @POST("/deviceRegistrationToken")
    fun deviceRegistrationToken(
        @Header("Authorization") authHeader: String,
        @Field("devToken") devToken: String
    ) : Call<DefaultResponse>

    @FormUrlEncoded
    @POST("/user/review")
    fun setReview(
        @Header("Authorization") authHeader :String,
        @Field("userId") userId : String,
        @Field("content") content: String
    ) : Call<ReviewResponse>

    @FormUrlEncoded
    @POST("/user/report")
    fun setReport(
        @Header("Authorization") authHeader :String,
        @Field("label") label : String,
        @Field("reportedTo") reportedTo: String
    ) : Call<RequesttoServiceProviderResponse>



}
/*
    @Headers("Content-Type:application/json")
    @POST("users")
    fun registerUser(
        @Body info: UserBody
    fun registerUser(
        @Field("phone") phone: UserBody,
        @Field("password") password:String,
        @Field("confirmPassword") confirmPassword:String,
        @Field("firstName") firstName:String,
        @Field("lastName") lastName:String,
        @Field("email") email:String,
        @Field("gender") gender:String,
        @Field("dob") dob:String)
            :Call<DefaultResponse>


}*/
