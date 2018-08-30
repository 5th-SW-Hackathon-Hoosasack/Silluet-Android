package kr.puze.silluet

import android.content.Intent
import android.content.SharedPreferences
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.transition.Visibility
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.Button
import com.facebook.*
import kotlinx.android.synthetic.main.activity_login.*
import com.facebook.login.LoginResult
import com.facebook.login.widget.LoginButton
import com.facebook.login.LoginManager
import java.util.*
import android.widget.Toast
import kr.puze.silluet.Data.User
import kr.puze.silluet.R.id.edit_password
import kr.puze.silluet.Server.RetrofitService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.Retrofit




class LoginActivity : AppCompatActivity() {

    lateinit var retrofitService: RetrofitService
    private var callbackManager: CallbackManager = CallbackManager.Factory.create()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        setContentView(R.layout.activity_login)
        supportActionBar!!.hide()
        facebook_login.visibility = View.GONE

        retrofitSetting()

        val sharedPreferences : SharedPreferences = getSharedPreferences("AutoLogin", MODE_PRIVATE)
        val editor : SharedPreferences.Editor = sharedPreferences.edit()

        button_facebook_login.setOnClickListener{
            facebook_login.performClick()
        }

        facebook_login.setReadPermissions("email")
        LoginManager.getInstance().registerCallback(callbackManager,
                object : FacebookCallback<LoginResult> {
                    override fun onSuccess(loginResult: LoginResult) {
                        val call: Call<User> = retrofitService.login_facebook(loginResult.accessToken.token)
                        call.enqueue(object : Callback<User> {
                            override fun onResponse(call: Call<User>?, response: Response<User>?) {
                                if (response?.code() == 200) {
                                    val user = response.body()
                                    if (user != null) {
                                        editor.putString("name", user.name)
                                        editor.putString("image", user.image)
                                        editor.apply()
                                        val intent = Intent(this@LoginActivity, MainActivity::class.java)
                                        intent.putExtra("name", user.name)
                                        intent.putExtra("image", user.image)
                                        startActivity(intent)
                                        finish()
                                        Toast.makeText(this@LoginActivity, "로그인 성공", Toast.LENGTH_SHORT).show()
                                    }
                                } else if (response?.code() == 401) {
                                    Toast.makeText(this@LoginActivity, "사용할 수 없는 아이디", Toast.LENGTH_SHORT).show()
                                } else {
                                    Toast.makeText(this@LoginActivity, "UNKNOWN ERR", Toast.LENGTH_SHORT).show()
                                }
                            }
                            override fun onFailure(call: Call<User>?, t: Throwable?) {
                                Toast.makeText(this@LoginActivity, "요청 불가", Toast.LENGTH_SHORT).show();
                            }
                        })

                    }
                    override fun onCancel() {
                        // App code
                    }
                    override fun onError(exception: FacebookException) {
                        // App code
                    }
                })

        LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("public_profile"))
//        val accessToken = AccessToken.getCurrentAccessToken()
//        val userToken = accessToken.token
//        var profile = Profile.getCurrentProfile()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        callbackManager.onActivityResult(requestCode, resultCode, data)
        super.onActivityResult(requestCode, resultCode, data)
    }

    private fun retrofitSetting() {
        val retrofit: Retrofit = Retrofit.Builder()
                .baseUrl("http://soylatte.kr:3000")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        retrofitService = retrofit.create(RetrofitService::class.java)
    }
}
