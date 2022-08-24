package com.wahidabd.dicodingstories.view.page.splash

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import com.wahidabd.dicodingstories.databinding.ActivitySplashBinding
import com.wahidabd.dicodingstories.utils.Constants
import com.wahidabd.dicodingstories.utils.MySharedPreference
import com.wahidabd.dicodingstories.view.MainActivity
import com.wahidabd.dicodingstories.view.page.auth.AuthActivity
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@SuppressLint("CustomSplashScreen")
@AndroidEntryPoint
class SplashActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySplashBinding
    @Inject lateinit var pref: MySharedPreference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        Handler(Looper.getMainLooper()).postDelayed({
            if (pref.getLogin()) {
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            }else{
                startActivity(Intent(this, AuthActivity::class.java))
                finish()
            }
        }, Constants.SPLASH_DURATION)

    }
}