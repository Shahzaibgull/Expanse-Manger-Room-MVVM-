package com.example.mvvmroomdatabase

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.mvvmroomdatabase.Database.UserDatabase
import com.example.mvvmroomdatabase.Model.User
import com.example.mvvmroomdatabase.ViewModel.UserViewModel
import com.example.mvvmroomdatabase.databinding.ActivityUpdateEntryBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class UpdateEntry : AppCompatActivity() {

    private lateinit var binding: ActivityUpdateEntryBinding
    private lateinit var userViewModel: UserViewModel
    private lateinit var updatedUser: User

    private lateinit var appDb: UserDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUpdateEntryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        appDb = UserDatabase.getInstance(this)!!
        userViewModel = ViewModelProvider(this, UserViewModelFactory(this))[UserViewModel::class.java]

        binding.updateReportBack.setOnClickListener {
            val intent = Intent(this, HistoryScreen::class.java)
            startActivity(intent)
        }

        val selectedUser = intent.getParcelableExtra<User>("SELECTED_USER")
        binding.editTextName.setText(selectedUser?.name)
        binding.editTextAge.setText(selectedUser?.age.toString())

        binding.buttonSave.setOnClickListener {
            updateData(selectedUser)
        }
    }

    private fun updateData(selectedUser: User?) {
        val newName = binding.editTextName.text.toString().trim()
        val newAge = binding.editTextAge.text.toString().trim().toInt()

        if (newName.isNotEmpty() && newAge > 0) {
            // Update the user using the UserViewModel
            selectedUser?.let {
                it.name = newName
                it.age = newAge
                userViewModel.updateUser(this, it)
                Toast.makeText(this, "Successfully Updated", Toast.LENGTH_SHORT).show()
            }

            // Set the result and finish the activity
            val resultIntent = Intent().apply {
                putExtra("UPDATED_USER", selectedUser)
            }
            setResult(Activity.RESULT_OK, resultIntent)
            finish()
        } else {
            Toast.makeText(this, "Please Enter Valid Data", Toast.LENGTH_SHORT).show()
        }
    }
}
