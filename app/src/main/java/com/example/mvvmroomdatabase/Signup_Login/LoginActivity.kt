package com.example.mvvmroomdatabase.Signup_Login

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.widget.Toast
import com.example.mvvmroomdatabase.HomeScreen
import com.example.mvvmroomdatabase.databinding.ActivityLoginBinding

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var appDb: AppDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        appDb=AppDatabase.getDatabase(this)
        binding.buttonLogin.setOnClickListener {

            val loadingDialog = loadingDialog(this@LoginActivity)  // Loading Dialog box
            loadingDialog.startLoadingDialog()
            val handler = Handler()
            handler.postDelayed({
                loadingDialog.dismissDialog()
            }, 5000)

            readDataFromDatabase()
        }
        binding.SignUp2.setOnClickListener {
            startActivity(Intent(this@LoginActivity, SignupActivity::class.java))
        }

    }
    /*private fun readDataFromDatabase(){

        val nameLogin=binding.etfirstNameLogin.text.toString()
        val passwordLogin=binding.etPasswordLogin.text.toString()

        if(nameLogin.isNotEmpty() && passwordLogin.isNotEmpty()){

            GlobalScope.launch(Dispatchers.Main) { // Switch to the main thread

                val getName = appDb.studentDao().findByName(nameLogin)
                val getPassword = appDb.studentDao().findByPassword(passwordLogin)

                if (getName != null && getPassword != null && nameLogin == getName.firstName && passwordLogin == getPassword.yourPassword) {
                    startActivity(Intent(this@LoginActivity, HomeScreen::class.java))
                    Toast.makeText(this@LoginActivity, "Successfully Login", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this@LoginActivity, "Incorrect credentials", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }*/
    private fun readDataFromDatabase() {
        val nameLogin = binding.etfirstNameLogin.text.toString()
        val passwordLogin = binding.etPasswordLogin.text.toString()

        if (nameLogin.isNotEmpty() && passwordLogin.isNotEmpty()) {
            GlobalScope.launch(Dispatchers.Main) {
                val user = appDb.studentDao().findByCredentials(nameLogin, passwordLogin)

                if (user != null) {
                    val sharedPreferences = getSharedPreferences("user_session", Context.MODE_PRIVATE)
                    val editor = sharedPreferences.edit()
                    editor.putBoolean("isLoggedIn", true)
                    editor.apply()
                    // Successfully logged in
                    startActivity(Intent(this@LoginActivity, HomeScreen::class.java))
                    Toast.makeText(this@LoginActivity, "Successfully Login", Toast.LENGTH_SHORT).show()
                } else {
                    // Incorrect credentials
                    Toast.makeText(this@LoginActivity, "Incorrect credentials", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}