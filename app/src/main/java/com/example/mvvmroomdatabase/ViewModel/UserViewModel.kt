package com.example.mvvmroomdatabase.ViewModel

import android.content.Context
import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.mvvmroomdatabase.Model.User
import com.example.mvvmroomdatabase.Repository.UserRepository


class UserViewModel(private val context: Context):ViewModel() {

    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences("my_preference", Context.MODE_PRIVATE)

    private var _totalAgeSum = MutableLiveData<Int>()
    val totalAgeSum: LiveData<Int> = _totalAgeSum

    private var _totalAgeSum1 = MutableLiveData<Int>()
    val totalAgeSum1: LiveData<Int> = _totalAgeSum1

    private var _totalAgeSum2 = MutableLiveData<Int>()
    val totalAgeSum2: LiveData<Int> = _totalAgeSum2

    init {
        _totalAgeSum.value = getSavedTotalAgeSum()
    }
    init {
        _totalAgeSum1.value = getSavedTotalAgeSum1()
    }
    init {
        _totalAgeSum2.value = getSavedTotalAgeSum2()
    }

    val userListLiveData: LiveData<List<User>> = UserRepository.getAllUserData(context)

    fun insert1(context: Context, user: User) {
        UserRepository.insert(context, user)

        _totalAgeSum2.value = _totalAgeSum.value?.plus(user.age)
        saveTotalAgeSumToSharedPreferences2(context, _totalAgeSum2.value ?: 0)

        if(_totalAgeSum.value==0)
        {
            _totalAgeSum.value = _totalAgeSum.value?.plus(user.age)
            saveTotalAgeSumToSharedPreferences(context, _totalAgeSum.value ?: 0)
            saveTotalAgeSumToSharedPreferences1(context, _totalAgeSum.value ?: 0)
        }
    }

    fun insert2(context: Context, user: User) {
        UserRepository.insert(context, user)
        if(_totalAgeSum1.value==0)
        {
            _totalAgeSum1.value = _totalAgeSum.value?.minus(user.age)
            saveTotalAgeSumToSharedPreferences1(context, _totalAgeSum1.value ?: 0)
        }
        _totalAgeSum1.value = _totalAgeSum1.value?.minus(user.age)
        saveTotalAgeSumToSharedPreferences1(context, _totalAgeSum1.value ?: 0)

    }


    fun getAllUserData(context: Context): LiveData<List<User>>?
    {
        return UserRepository.getAllUserData(context)
    }

    fun updateUser(context: Context, user: User) {
        UserRepository.updateUser(context, user)
    }

    fun deleteUser(context: Context, user: User) {
        UserRepository.deleteUser(context, user)
    }



    // Save the totalAgeSum to SharedPreferences
    private fun saveTotalAgeSumToSharedPreferences(context: Context, totalAgeSum: Int) {
        with(sharedPreferences.edit()) {
            putInt("Combine save", totalAgeSum)
            apply()
        }
    }
    // Retrieve totalAgeSum from SharedPreferences, defaulting to 0 if not found
    private fun getSavedTotalAgeSum(): Int {
        return sharedPreferences.getInt("Combine save", 0)
    }





    // Save the totalAgeSum to SharedPreferences
    private fun saveTotalAgeSumToSharedPreferences1(context: Context, totalAgeSum1: Int) {
        with(sharedPreferences.edit()) {
            putInt("cash in hand", totalAgeSum1)
            apply()
        }
    }
    // Retrieve totalAgeSum from SharedPreferences, defaulting to 0 if not found
    private fun getSavedTotalAgeSum1(): Int {
        return sharedPreferences.getInt("cash in hand", 0)
    }




    // Save the totalAgeSum to SharedPreferences
    private fun saveTotalAgeSumToSharedPreferences2(context: Context, totalAgeSum2: Int) {
        with(sharedPreferences.edit()) {
            putInt("your income", totalAgeSum2)
            apply()
        }
    }
    // Retrieve totalAgeSum from SharedPreferences, defaulting to 0 if not found
    private fun getSavedTotalAgeSum2(): Int {
        return sharedPreferences.getInt("your income", 0)
    }


    fun resetTotalAgeSum(context: Context) {
        // Update SharedPreferences to zero
        saveTotalAgeSumToSharedPreferences(context, 0)
        saveTotalAgeSumToSharedPreferences1(context, 0)
        saveTotalAgeSumToSharedPreferences2(context, 0)

        // Update LiveData
        _totalAgeSum.postValue(0)
    }
}