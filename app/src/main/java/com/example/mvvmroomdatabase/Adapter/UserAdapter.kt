package com.example.mvvmroomdatabase.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.mvvmroomdatabase.Model.User
import com.example.mvvmroomdatabase.R

class UserAdapter(
    private val context: Context,
    var userList: ArrayList<User>,
    private val updateListener: (User) -> Unit,
    private val deleteListener: (User) -> Unit
) : RecyclerView.Adapter<UserAdapter.UserViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        return UserViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.each_row_cash_in,
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int = userList.size

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val user: User = userList[position]
        holder.name.text = user.name
        holder.age.text = user.age.toString()

        // Set click listeners for update and delete buttons
        holder.updateButton.setOnClickListener {
            updateListener(user)
        }

        holder.deleteButton.setOnClickListener {
            deleteListener(user)
        }
    }

    fun setData(newList: ArrayList<User>) {
        val diffCallback = UserDiffCallback(userList, newList)
        val diffResult = DiffUtil.calculateDiff(diffCallback)

        userList.clear()
        userList.addAll(newList)
        diffResult.dispatchUpdatesTo(this)
    }

    inner class UserViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val name: TextView = itemView.findViewById(R.id.name)
        val age: TextView = itemView.findViewById(R.id.age)
        val updateButton: Button = itemView.findViewById(R.id.updateButton)
        val deleteButton: Button = itemView.findViewById(R.id.deleteButton)
    }
}
