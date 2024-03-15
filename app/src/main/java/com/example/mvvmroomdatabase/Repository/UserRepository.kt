package com.example.mvvmroomdatabase.Repository

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.mvvmroomdatabase.Database.UserDatabase
import com.example.mvvmroomdatabase.Model.User
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch


class UserRepository {

    companion object {
        private var userDatabase: UserDatabase? = null
        fun initaliseDB(context: Context): UserDatabase? {
            return UserDatabase.getInstance(context)
        }

        fun insert(context: Context, user: User) {
            userDatabase = initaliseDB(context)
            CoroutineScope(IO).launch {

                userDatabase?.getDao()?.insert(user)
            }
        }

        fun getAllUserData(context: Context): LiveData<List<User>> {
            userDatabase = initaliseDB(context)
            return userDatabase?.getDao()?.getAllUserData() ?: MutableLiveData()
        }

        fun updateUser(context: Context, user: User) {
            userDatabase = initaliseDB(context)
            CoroutineScope(IO).launch {
                userDatabase?.getDao()?.updateUser(user)
            }
        }

        fun deleteUser(context: Context, user: User) {
            userDatabase = initaliseDB(context)
            CoroutineScope(IO).launch {
                userDatabase?.getDao()?.deleteUser(user)
            }

        }
    }
}