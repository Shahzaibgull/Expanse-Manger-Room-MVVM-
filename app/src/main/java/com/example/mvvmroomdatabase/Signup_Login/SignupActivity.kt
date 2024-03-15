package com.example.mvvmroomdatabase.Signup_Login

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.mvvmroomdatabase.HomeScreen
import com.example.mvvmroomdatabase.databinding.ActivitySignupBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class SignupActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignupBinding
    private lateinit var appDb: AppDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)



        appDb=AppDatabase.getDatabase(this)
        binding.buttonSignup.setOnClickListener {

            val loadingDialog = loadingDialog(this@SignupActivity)  // Loading Dialog box
            loadingDialog.startLoadingDialog()
            val handler = Handler()
            handler.postDelayed({
                loadingDialog.dismissDialog()
            }, 5000)
            writeData()
        }
        binding.login2.setOnClickListener {
            startActivity(Intent(this@SignupActivity, LoginActivity::class.java))
        }
    }

    private fun writeData(){
        val firstName=binding.etFirstName.text.toString()
        val lastName=binding.etLastName.text.toString()
        val yourPassword=binding.etYourPassword.text.toString()
        val yourAge=binding.etYourAge.text.toString()

        if(firstName.isNotEmpty() && lastName.isNotEmpty() && yourPassword.isNotEmpty() && yourAge.isNotEmpty()){
            val student= Student(
                null,firstName,lastName,yourPassword,yourAge.toInt()
            )
            GlobalScope.launch(Dispatchers.IO) {
                appDb.studentDao().insert(student)
            }

            // Set a flag indicating that signup is complete
            val sharedPreferences = getSharedPreferences("user_session", Context.MODE_PRIVATE)
            val editor = sharedPreferences.edit()
            editor.putBoolean("isLoggedIn", true)
            editor.apply()

            startActivity(Intent(this@SignupActivity, HomeScreen::class.java))

            Toast.makeText(this@SignupActivity, "Successfully Signup", Toast.LENGTH_SHORT).show()
            binding.etFirstName.text?.clear()
            binding.etLastName.text?.clear()
            binding.etYourPassword.text?.clear()
            binding.etYourAge.text?.clear()

        }
        else{
            Toast.makeText(this@SignupActivity, "Please Enter Data", Toast.LENGTH_SHORT).show()
        }

    }
}