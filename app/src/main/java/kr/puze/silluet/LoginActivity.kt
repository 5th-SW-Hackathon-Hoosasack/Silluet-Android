package kr.puze.silluet

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowManager
import com.facebook.*
import kotlinx.android.synthetic.main.activity_login.*
import com.facebook.login.LoginResult
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import java.util.*
import com.kakao.auth.ISessionCallback
import com.kakao.auth.Session
import com.kakao.network.ErrorResult
import com.kakao.usermgmt.UserManagement
import com.kakao.usermgmt.callback.MeResponseCallback
import com.kakao.usermgmt.response.model.UserProfile
import com.kakao.util.exception.KakaoException
import kr.puze.silluet.Data.User
import kr.puze.silluet.Server.RetrofitService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.Retrofit


class LoginActivity : AppCompatActivity() {

    companion object {
        lateinit var token: String
        lateinit var call: Call<User>
        lateinit var retrofitService: RetrofitService
        @SuppressLint("StaticFieldLeak")
        lateinit var context: Context
        val callbackManager = CallbackManager.Factory.create()!!
        lateinit var editor: SharedPreferences.Editor
        lateinit var sharedPreferences: SharedPreferences
        lateinit var intent: Intent
    }


    @SuppressLint("CommitPrefEdits")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        setContentView(R.layout.activity_login)
        supportActionBar!!.hide()
        context = applicationContext
        intent = Intent(this@LoginActivity, MainActivity::class.java)
        facebook_login.visibility = View.GONE
        kakao_login.visibility = View.GONE
        retrofitSetting()

        sharedPreferences = getSharedPreferences("AutoLogin", 0)
        editor = sharedPreferences.edit()

//        if(!(sharedPreferences.getBoolean("isUser", false))){
//            val intent = Intent(this@LoginActivity, MainActivity::class.java)
//            intent.putExtra("name", sharedPreferences.getString("name", ""))
//            intent.putExtra("image", sharedPreferences.getString("image", ""))
//            startActivity(intent)
//            finish()
//        }

        button_facebook_login.setOnClickListener {
            facebook_login.performClick()
        }
        button_kakao_login.setOnClickListener {
            kakao_login.performClick()
        }

        facebook_login.setReadPermissions(Arrays.asList("public_profile", "email"))
        facebook_login.registerCallback(callbackManager, object: FacebookCallback<LoginResult>{
            override fun onSuccess(result: LoginResult?) {
                var accessToken: AccessToken = result!!.accessToken
                token = accessToken.token
                Log.d("facebook_login", token)
                login("FACEBOOK_LOGIN")            }

            override fun onCancel() {
                Log.d("facebook_login", "onCancel")
            }

            override fun onError(error: FacebookException?) {
                Log.d("facebook_login", "onError")
                error!!.printStackTrace()
            }
        })
/*
        LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("public_profile"))
*/
//        val accessToken = AccessToken.getCurrentAccessToken()
//        val userToken = accessToken.token
//        var profile = Profile.getCurrentProfile()

        val sessionCallBack = SessionCallback()
        Session.getCurrentSession().addCallback(sessionCallBack)
//        Session.getCurrentSession().checkAndImplicitOpen()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        Log.d("onActivityResult", resultCode.toString())
        if (Session.getCurrentSession().handleActivityResult(requestCode, resultCode, data)) {
            return
        }
        super.onActivityResult(requestCode, resultCode, data)
        callbackManager.onActivityResult(requestCode, resultCode, data)
    }

    private class SessionCallback : ISessionCallback {
        override fun onSessionOpenFailed(exception: KakaoException?) {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }

        override fun onSessionOpened() {
            Log.d("TAG", "onSessionOpened")
            requestMe()
        }

        private fun requestMe() {
            // 사용자정보 요청 결과에 대한 Callback
            UserManagement.requestMe(object : MeResponseCallback() {
                // 세션 오픈 실패. 세션이 삭제된 경우,
                override fun onSessionClosed(errorResult: ErrorResult) {
                    Log.e("SessionCallback :: ", "onSessionClosed : " + errorResult.errorMessage)
                }

                // 회원이 아닌 경우,
                override fun onNotSignedUp() {
                    Log.e("SessionCallback :: ", "onNotSignedUp")
                }

                // 사용자정보 요청에 성공한 경우,
                override fun onSuccess(userProfile: UserProfile) {
                    Log.e("SessionCallback :: ", "onSuccess")
                    token = Session.getCurrentSession().accessToken
                    Log.d("kakao_login", token)
                    LoginActivity().login("KAKAO_LOGIN")
                    val nickname = userProfile.nickname
                    val profileImagePath = userProfile.profileImagePath
                    val thumnailPath = userProfile.thumbnailImagePath
                    val UUID = userProfile.uuid
                    val id = userProfile.id

                    Log.e("Profile : ", token + "")
                    Log.e("Profile : ", nickname + "")
                    Log.e("Profile : ", profileImagePath + "")
                    Log.e("Profile : ", thumnailPath + "")
                    Log.e("Profile : ", UUID + "")
                    Log.e("Profile : ", id.toString() + "")
                }

                // 사용자 정보 요청 실패
                override fun onFailure(errorResult: ErrorResult?) {
                    Log.e("SessionCallback :: ", "onFailure : " + errorResult!!.errorMessage)
                }
            })
        }
    }

    fun login(type: String) {
        Log.d("login_fun", "running")
        when (type) {
            "KAKAO_LOGIN" -> {
                Log.d("call", "kakao")
                call = retrofitService.login_kakao(token)
            }
            "FACEBOOK_LOGIN" -> {
                Log.d("call", "facebook")
                call = retrofitService.login_facebook(token)
            }
        }
        call.enqueue(object : Callback<User> {
            override fun onResponse(call: Call<User>?, response: Response<User>?) {
                Log.d("login_call", "onResponse")
                Log.d("login_token", token)
                if (response?.code() == 200) {
                    val user = response.body()
                    Log.d("response", user?.name)
                    Log.d("response", user?.imgUrl)
                    if (user != null) {
                        editor.putString("name", user.name)
                        editor.putString("image", user.imgUrl)
                        editor.putBoolean("isUser", true)
                        editor.apply()
                        intent.putExtra("name", user.name)
                        intent.putExtra("image", user.imgUrl)
                        startActivity(intent)
                        finish()
                    }
                } else if (response?.code() == 401) {
                    Log.d("login_call", "401")
                } else {
                    Log.d("login_call", "else")
                    Log.d("login_code", response!!.code().toString())
                }
            }

            override fun onFailure(call: Call<User>?, t: Throwable?) {
                Log.d("login_call", "onFailure")
                Log.d("login_call", t.toString())
            }
        })
    }

    private fun retrofitSetting() {
        var gson: Gson = GsonBuilder()
                .setLenient()
                .create();

        val retrofit: Retrofit = Retrofit.Builder()
                .baseUrl("http://18.223.150.68:3000")
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build()
        retrofitService = retrofit.create(RetrofitService::class.java)
    }
}
