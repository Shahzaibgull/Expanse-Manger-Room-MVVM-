package com.example.mvvmroomdatabase.Signup_Login

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.PrimaryKey
import androidx.room.Query


@Dao
interface StudentDao {
    @PrimaryKey(autoGenerate = true)

    /*@Query("SELECT * FROM student_table WHERE first_name = :userName LIMIT 1")
    suspend fun findByName(userName: String): Student?

    @Query("SELECT * FROM student_table WHERE your_password = :userPassword LIMIT 1")
    suspend fun findByPassword(userPassword: String): Student?*/

    @Query("SELECT * FROM student_table WHERE first_name = :userName AND your_password = :userPassword LIMIT 1")
    suspend fun findByCredentials(userName: String, userPassword: String): Student?

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(student: Student)

    @Query("SELECT COUNT(*) FROM student_table")
    suspend fun hasLoggedInUser(): Int



}