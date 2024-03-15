package com.example.mvvmroomdatabase

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mvvmroomdatabase.Adapter.UserAdapter
import com.example.mvvmroomdatabase.Model.User
import com.example.mvvmroomdatabase.ViewModel.UserViewModel
import com.example.mvvmroomdatabase.databinding.ActivityExpanseMainBinding


class ExpanseMainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityExpanseMainBinding
    private lateinit var userViewModel: UserViewModel
    private lateinit var builder:AlertDialog.Builder
    private lateinit var dialog: AlertDialog
    private lateinit var name:EditText
    private lateinit var age:EditText
    private lateinit var save:Button
    private lateinit var titleDate:TextView
    private lateinit var recyclerView: RecyclerView
    private lateinit var userAdapter: UserAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityExpanseMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //userAdapter= UserAdapter(this,ArrayList<User>())
        recyclerView=findViewById(R.id.recyclerView)
        recyclerView.apply {
            setHasFixedSize(true)
            layoutManager=LinearLayoutManager(this@ExpanseMainActivity)
            adapter=userAdapter
        }

        userViewModel= ViewModelProvider(this).get(UserViewModel::class.java)
        userViewModel.getAllUserData(applicationContext)?.observe(this, Observer {

            userAdapter.setData(it as ArrayList<User>)

        })

        binding.floatingActionButton.setOnClickListener {
            openDailog()
        }
    }

    private fun openDailog() {
        builder=AlertDialog.Builder(this)
        var itemView: View =LayoutInflater.from(applicationContext).inflate(R.layout.dialog_cashin,null)
        dialog=builder.create()
        dialog.setView(itemView)

        name=itemView.findViewById(R.id.name1)
        age=itemView.findViewById(R.id.age1)
        save=itemView.findViewById(R.id.save)
        titleDate=itemView.findViewById(R.id.titleDate)



        save.setOnClickListener {
            saveDataIntoRoomDatabase()
        }
        dialog.show()

    }

    private fun saveDataIntoRoomDatabase() {

        val getName=name.text.toString().trim()
        val getAge=age.text.toString().trim()

        if(!TextUtils.isEmpty(getName) && !TextUtils.isEmpty(getAge))
        {
            //userViewModel.insert(this,User(getName,Integer.parseInt(getAge)))
            dialog.dismiss()

        }
        else
        {
            Toast.makeText(applicationContext, "Please fill all the fields", Toast.LENGTH_SHORT).show()
            dialog.dismiss()
        }
    }


}