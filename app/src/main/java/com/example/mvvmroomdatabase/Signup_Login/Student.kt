package com.example.mvvmroomdatabase.Signup_Login

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName="student_table")
data class Student(
    @PrimaryKey(autoGenerate = false) val id:Int?,
    @ColumnInfo(name="first_name") val firstName: String?,
    @ColumnInfo(name="last_name") val lastName: String?,
    @ColumnInfo(name="your_password") val yourPassword: String?,
    @ColumnInfo(name="your_age") val yourAge: Int?
    )
