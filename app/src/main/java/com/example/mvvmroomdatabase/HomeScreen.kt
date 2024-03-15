package com.example.mvvmroomdatabase

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.icu.util.Calendar
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.mvvmroomdatabase.Model.User
import com.example.mvvmroomdatabase.Signup_Login.LoginActivity
import com.example.mvvmroomdatabase.ViewModel.UserViewModel
import com.example.mvvmroomdatabase.databinding.ActivityHomeScreenBinding
import java.text.SimpleDateFormat
import java.util.Locale

class HomeScreen : AppCompatActivity() {

    private lateinit var binding: ActivityHomeScreenBinding
    private lateinit var userViewModel: UserViewModel
    private lateinit var builder: AlertDialog.Builder
    private lateinit var dialog: AlertDialog
    private lateinit var name: EditText
    private lateinit var age: EditText
    private lateinit var save: Button
    private lateinit var title: TextView
    private lateinit var titleDate: TextView
    private lateinit var selectedUser: User


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        userViewModel = ViewModelProvider(this, UserViewModelFactory(this))[UserViewModel::class.java]

        userViewModel.totalAgeSum.observe(this) { totalSum ->
            binding.CashInHandTextView.text = totalSum.toString()
            binding.totalBalanceTextView.text = totalSum.toString()
        }
        userViewModel.totalAgeSum2.observe(this) { totalSum2 ->
            //binding.CashInHandTextView.text = totalSum2.toString()
            binding.totalBalanceTextView.text = totalSum2.toString()
        }
        userViewModel.totalAgeSum1.observe(this) { totalSum1 ->
            binding.CashInHandTextView.text = totalSum1.toString()
        }

        binding.brightness.setOnClickListener {
            userViewModel.resetTotalAgeSum(this)
        }

        binding.logout.setOnClickListener {

            clearUserSession()
            startActivity(Intent(this, LoginActivity::class.java))
            Toast.makeText(this@HomeScreen, "Sign out Successfully", Toast.LENGTH_SHORT).show()
            finish()
        }

        // Initialize selectedUser if it's not initialized
        if (!::selectedUser.isInitialized) {
            selectedUser = User("", 0) // Provide default values or fetch from your data source
        }

        val calendar = Calendar.getInstance()
        val dateFormat = SimpleDateFormat("EEE, dd MMM yyyy", Locale.getDefault())
        val formattedDate = dateFormat.format(calendar.time)
        val resultText = formattedDate
        binding.addguideTextView1.text = resultText


        binding.bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.action_item1 -> {
                    true
                }
                R.id.action_item2 -> {
                    true
                }
                R.id.action_item3 -> {
                    true
                }
                else -> false
            }
        }

        binding.cashInButton.setOnClickListener {

            openDialog1("cashIn")

            val sharedPreferences1: SharedPreferences = getSharedPreferences("dialog status", Context.MODE_PRIVATE)
            val editor = sharedPreferences1.edit()
            editor.putInt("key", 1)
            editor.apply()
        }
        binding.cashOutButton.setOnClickListener {
            openDialog2("cashOut")
        }
        binding.historyTextView.setOnClickListener {
            val intent = Intent(this, HistoryScreen::class.java)
            intent.putExtra("SELECTED_USER", selectedUser)
            startActivity(intent)
        }
    }


    private fun clearUserSession() {
        val sharedPreferences = getSharedPreferences("user_session", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.clear()
        editor.putBoolean("isLoggedIn", false)  // Add this line to set the isLoggedIn flag to false
        editor.apply()
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun openDialog1(action: String) {
        builder = AlertDialog.Builder(this)
        val layoutResId = when (action) {
            "cashIn" -> R.layout.dialog_cashin
            "cashOut" -> R.layout.dialog_cashout
            else -> throw IllegalArgumentException("Unsupported action: $action")
        }
        val itemView: View = LayoutInflater.from(applicationContext).inflate(layoutResId, null)
        dialog = builder.create()
        dialog.setView(itemView)

        val sharedPreferences1: SharedPreferences = getSharedPreferences("dialog status", Context.MODE_PRIVATE)
        val status = sharedPreferences1.getInt("key", 0)

        if(status==1)
        {
            title =itemView.findViewById(R.id.CashIntitle)
            title.text="Update Income"
        }

        name = itemView.findViewById(R.id.name1)
        age = itemView.findViewById(R.id.age1)
        save = itemView.findViewById(R.id.save)
        titleDate = itemView.findViewById(R.id.titleDate)



        age.setOnTouchListener { _, event ->
            val DRAWABLE_RIGHT = 2
            if (event.action == MotionEvent.ACTION_UP) {
                if (event.rawX >= age.right - age.compoundDrawables[DRAWABLE_RIGHT].bounds.width()) {
                    val currentText = age.text.toString()
                    if (currentText.isNotEmpty()) {
                        val newText = currentText.substring(0, currentText.length - 1)
                        age.setText(newText)
                        age.setSelection(newText.length)
                    }
                    return@setOnTouchListener true
                }
            }
            false
        }

        val calendar = Calendar.getInstance()
        val dateFormat = SimpleDateFormat("EEE, dd MMM yyyy", Locale.getDefault())
        val formattedDate = dateFormat.format(calendar.time)
        val resultText = formattedDate
        titleDate.text = resultText

        save.setOnClickListener {
            when (action) {
                "cashIn" -> saveDataIntoRoomDatabase1()
            }
            Toast.makeText(this, "Balance Saved", Toast.LENGTH_SHORT).show().toString()
        }

        dialog.show()
    }


    @SuppressLint("ClickableViewAccessibility")
    private fun openDialog11(action: String, itemView: View) {
        builder = AlertDialog.Builder(this)
        val layoutResId = when (action) {
            "cashIn" -> R.layout.dialog_cashin
            "cashOut" -> R.layout.dialog_cashout
            else -> throw IllegalArgumentException("Unsupported action: $action")
        }
        //val itemView: View = LayoutInflater.from(applicationContext).inflate(layoutResId, null)
        dialog = builder.create()
        dialog.setView(itemView)

        /*name = itemView.findViewById(R.id.name1)
        age = itemView.findViewById(R.id.age1)*/
        save = itemView.findViewById(R.id.save)
        titleDate = itemView.findViewById(R.id.titleDate)



        age.setOnTouchListener { _, event ->
            val DRAWABLE_RIGHT = 2
            if (event.action == MotionEvent.ACTION_UP) {
                if (event.rawX >= age.right - age.compoundDrawables[DRAWABLE_RIGHT].bounds.width()) {
                    val currentText = age.text.toString()
                    if (currentText.isNotEmpty()) {
                        val newText = currentText.substring(0, currentText.length - 1)
                        age.setText(newText)
                        age.setSelection(newText.length)
                    }
                    return@setOnTouchListener true
                }
            }
            false
        }

        val calendar = Calendar.getInstance()
        val dateFormat = SimpleDateFormat("EEE, dd MMM yyyy", Locale.getDefault())
        val formattedDate = dateFormat.format(calendar.time)
        val resultText = formattedDate
        titleDate.text = resultText

        save.setOnClickListener {
            when (action) {
                "cashIn" -> saveDataIntoRoomDatabase1()
            }
            Toast.makeText(this, "Balance Saved", Toast.LENGTH_SHORT).show().toString()
        }

        dialog.show()
    }



    @SuppressLint("ClickableViewAccessibility")
    private fun openDialog2(action: String) {
        builder = AlertDialog.Builder(this)
        val layoutResId = when (action) {
            "cashIn" -> R.layout.dialog_cashin
            "cashOut" -> R.layout.dialog_cashout
            else -> throw IllegalArgumentException("Unsupported action: $action")
        }
        val itemView: View = LayoutInflater.from(applicationContext).inflate(layoutResId, null)
        dialog = builder.create()
        dialog.setView(itemView)

        name = itemView.findViewById(R.id.name1)
        age = itemView.findViewById(R.id.age1)
        save = itemView.findViewById(R.id.save)
        titleDate = itemView.findViewById(R.id.titleDate)


        age.setOnTouchListener { _, event ->
            val DRAWABLE_RIGHT = 2
            if (event.action == MotionEvent.ACTION_UP) {
                if (event.rawX >= age.right - age.compoundDrawables[DRAWABLE_RIGHT].bounds.width()) {
                    val currentText = age.text.toString()
                    if (currentText.isNotEmpty()) {
                        val newText = currentText.substring(0, currentText.length - 1)
                        age.setText(newText)
                        age.setSelection(newText.length)
                    }
                    return@setOnTouchListener true
                }
            }
            false
        }

        val calendar = Calendar.getInstance()
        val dateFormat = SimpleDateFormat("EEE, dd MMM yyyy", Locale.getDefault())
        val formattedDate = dateFormat.format(calendar.time)
        val resultText = formattedDate
        titleDate.text = resultText

        save.setOnClickListener {
            when (action) {
                "cashOut" -> saveDataIntoRoomDatabase2()
            }
            Toast.makeText(this, "Balance Saved", Toast.LENGTH_SHORT).show().toString()
        }

        dialog.show()
    }

    private fun saveDataIntoRoomDatabase1() {
        val getName = name.text.toString().trim()
        val getAge = age.text.toString().trim()

        if (!TextUtils.isEmpty(getName) && !TextUtils.isEmpty(getAge)) {
            val ageValue = Integer.parseInt(getAge)

            userViewModel.insert1(this, User(getName, ageValue))


            dialog.dismiss()
        } else {
            Toast.makeText(applicationContext, "Please fill all the fields", Toast.LENGTH_SHORT).show()
            dialog.dismiss()
        }
    }

    private fun saveDataIntoRoomDatabase2() {
        val getName = name.text.toString().trim()
        val getAge = age.text.toString().trim()

        if (!TextUtils.isEmpty(getName) && !TextUtils.isEmpty(getAge)) {
            val ageValue = Integer.parseInt(getAge)

            userViewModel.insert2(this, User(getName, ageValue))


            dialog.dismiss()
        } else {
            Toast.makeText(applicationContext, "Please fill all the fields", Toast.LENGTH_SHORT).show()
            dialog.dismiss()
        }
    }

}

