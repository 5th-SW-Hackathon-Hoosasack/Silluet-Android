package kr.puze.silluet.KaKaoLogin

import android.app.Activity
import android.app.Application
import android.content.Context
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.util.Base64
import com.kakao.auth.KakaoSDK
import com.kakao.auth.IApplicationConfig
import com.kakao.auth.ApprovalType
import com.kakao.auth.AuthType
import com.kakao.auth.ISessionConfig
import com.kakao.auth.KakaoAdapter
import android.util.Base64.NO_WRAP
import android.util.Log
import com.kakao.util.helper.Utility.getPackageInfo
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException


class GlobalApplication : Application(){

    inner class KakaoSDKAdapter : KakaoAdapter(){

        override fun getApplicationConfig(): IApplicationConfig {
            return object : IApplicationConfig {
                override fun getApplicationContext(): Context {
                    return this@GlobalApplication.applicationContext
                }
            }
        }

        override fun getSessionConfig(): ISessionConfig {
            return object : ISessionConfig {
                override fun isSaveFormData(): Boolean {
                    return true
                }

                override fun getAuthTypes(): Array<AuthType> {
                    return arrayOf(AuthType.KAKAO_LOGIN_ALL)
                }

                override fun isSecureMode(): Boolean {
                    return false
                }

                override fun getApprovalType(): ApprovalType {
                    return ApprovalType.INDIVIDUAL
                }

                override fun isUsingWebviewTimer(): Boolean {
                    return false
                }
            }
        }
    }

    fun getKeyHash(context: Context): String? {
        val packageInfo = getPackageInfo(context, PackageManager.GET_SIGNATURES) ?: return null

        for (signature in packageInfo!!.signatures) {
            try {
                val md = MessageDigest.getInstance("SHA")
                md.update(signature.toByteArray())
                return Base64.encodeToString(md.digest(), Base64.NO_WRAP)
            } catch (e: NoSuchAlgorithmException) {
                Log.w("Tlqkf", "Unable to get MessageDigest. signature=$signature", e)
            }

        }
        return null
    }

    override fun onCreate() {
        super.onCreate()
        KakaoSDK.init(KakaoSDKAdapter())
        val hash = getKeyHash(this)
        Log.e("hash", hash)

    }






}