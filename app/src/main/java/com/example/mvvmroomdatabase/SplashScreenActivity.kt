package com.example.mvvmroomdatabase

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.mvvmroomdatabase.Signup_Login.AppDatabase
import com.example.mvvmroomdatabase.Signup_Login.LoginActivity
import com.example.mvvmroomdatabase.Signup_Login.SignupActivity
import com.example.mvvmroomdatabase.databinding.ActivitySplashScreenBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class SplashScreenActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySplashScreenBinding
    private lateinit var appDb: AppDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivitySplashScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.splashImage.alpha=0f
        binding.splashImage.animate().setDuration(1500).alpha(1f).withEndAction{

            overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out)


            appDb = AppDatabase.getDatabase(this)

            GlobalScope.launch(Dispatchers.Main) {
                val sharedPreferences = getSharedPreferences("user_session", Context.MODE_PRIVATE)
                val isLoggedIn = sharedPreferences.getBoolean("isLoggedIn", false)
                val isSignUpComplete = sharedPreferences.getBoolean("isSignUpComplete", false)

                if (isLoggedIn) {
                    startActivity(Intent(this@SplashScreenActivity, HomeScreen::class.java))
                } else {
                    if (isSignUpComplete) {
                        startActivity(Intent(this@SplashScreenActivity, HomeScreen::class.java))
                    } else {
                        startActivity(Intent(this@SplashScreenActivity, SignupActivity::class.java))
                    }
                }
                finish()
            }
        }

    }
}