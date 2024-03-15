package com.example.mvvmroomdatabase

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mvvmroomdatabase.Adapter.UserAdapter
import com.example.mvvmroomdatabase.Model.User
import com.example.mvvmroomdatabase.ViewModel.UserViewModel
import com.example.mvvmroomdatabase.databinding.ActivityHistoryScreenBinding

class HistoryScreen : AppCompatActivity() {

    private lateinit var binding: ActivityHistoryScreenBinding
    private lateinit var userViewModel: UserViewModel
    private lateinit var recyclerView: RecyclerView
    private lateinit var userAdapter: UserAdapter

    //var items = arrayOf("This Month", "Single Day", "Last Week", "Last Month", "Date Range")
    //var arrayAdapter: ArrayAdapter<String>? = null


    companion object {
        const val EDIT_USER_REQUEST = 1
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHistoryScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)


        //arrayAdapter = ArrayAdapter(this, R.layout.gender_list, items)
        //binding.status.setAdapter(arrayAdapter)




        // Initialize the AutoCompleteTextView with the saved status
        val sharedPreferences = getSharedPreferences("Status", Context.MODE_PRIVATE)
        val savedStatus = sharedPreferences.getString("selected_status", "")

        val items = arrayOf("This Month", "Single Day", "Last Week", "Last Month", "Date Range")
        val arrayAdapter = ArrayAdapter(this, R.layout.gender_list, items)
        binding.status.setAdapter(arrayAdapter)
         // Set the selected item if it was saved previously
        if (savedStatus != null && savedStatus.isNotEmpty()) {
            val position = items.indexOf(savedStatus)
            if (position != -1) {
                binding.status.setText(savedStatus, false)
            }
        }
        binding.status.setOnItemClickListener { _, _, position, _ ->
            val selectedStatus = items[position]
            saveStatusToSharedPreferences(selectedStatus)
        }

        binding.cashReportBack.setOnClickListener {
            val intent = Intent(this, HomeScreen::class.java)
            startActivity(intent)
        }

        userAdapter = UserAdapter(
            this,
            ArrayList(),
            // Update listener
            { selectedUser -> updateUser(selectedUser) },
            // Delete listener
            { selectedUser -> deleteUser(selectedUser) }
        )

        binding.recyclerView.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(this@HistoryScreen)
            adapter = userAdapter
        }

        //userViewModel = ViewModelProvider(this).get(UserViewModel::class.java)
        userViewModel = ViewModelProvider(this, UserViewModelFactory(this))[UserViewModel::class.java]
        // Observe user list changes
        userViewModel.userListLiveData.observe(this, Observer {
            userAdapter.setData(it as ArrayList<User>)
        })
    }

    private fun saveStatusToSharedPreferences(selectedStatus: String) {
        val sharedPreferences = getSharedPreferences("Status", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString("selected_status", selectedStatus)
        editor.apply()


    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == EDIT_USER_REQUEST && resultCode == Activity.RESULT_OK) {
            val updatedUser = data?.getParcelableExtra<User>("UPDATED_USER")
            if (updatedUser != null) {
                // Find the position of the updated user in your data list
                val updatedUserPosition = userAdapter.userList.indexOfFirst { it.id == updatedUser.id }
                if (updatedUserPosition != -1) {
                    // Replace the old user with the updated user
                    userAdapter.userList[updatedUserPosition] = updatedUser
                    userAdapter.notifyItemChanged(updatedUserPosition)
                }
            }
        }
    }


    private fun updateUser(user: User) {

        val intent = Intent(this, UpdateEntry::class.java).apply {
            putExtra("SELECTED_USER", user)
        }
        startActivityForResult(intent, HistoryScreen.EDIT_USER_REQUEST)

        /*val intent = Intent(this, UpdateEntry::class.java)
            intent.putExtra("SELECTED_USER", user)
            startActivity(intent)*/
    }

    private fun deleteUser(user: User) {
        userViewModel.deleteUser(applicationContext, user)
    }
}
