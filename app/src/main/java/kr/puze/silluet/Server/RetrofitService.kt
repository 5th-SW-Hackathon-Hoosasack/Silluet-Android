package kr.puze.silluet.Server

import kr.puze.silluet.Data.User
import retrofit2.http.*

interface RetrofitService {
    @GET("/account/login/facebook")
    fun login_facebook(@Query("token") token: String): retrofit2.Call<User>

    @GET("/account/login/kakao")
    fun login_kakao(@Query("token") token: String): retrofit2.Call<User>
//    @FormUrlEncoded
//    @POST("/login")
//    fun signup(@Field("id") id: String, @Field("password") password: String, @Field("age") age: String, @Field("sex") sex: String, @Field("name") name: String): retrofit2.Call<SignUp>
//
//    @GET("/location?")
//    fun location(@Path("latitude") latitude: String, @Field("longitude") longitude: String): retrofit2.Call<Location>
}