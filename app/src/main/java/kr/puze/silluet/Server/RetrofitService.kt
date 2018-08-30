package kr.puze.silluet.Server

import kr.puze.silluet.Data.User
import retrofit2.http.*

interface RetrofitService {
    @FormUrlEncoded
    @POST("/account/login/facebook")
    fun login_facebook(@Field("token") id: String): retrofit2.Call<User>

    @FormUrlEncoded
    @POST("/account/login/kakao")
    fun login_kakao(@Field("token") id: String): retrofit2.Call<User>
//    @FormUrlEncoded
//    @POST("/login")
//    fun signup(@Field("id") id: String, @Field("password") password: String, @Field("age") age: String, @Field("sex") sex: String, @Field("name") name: String): retrofit2.Call<SignUp>
//
//    @GET("/location?")
//    fun location(@Path("latitude") latitude: String, @Field("longitude") longitude: String): retrofit2.Call<Location>
}