package com.example.character_chatbot_application.data.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_table")
data class User(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    @ColumnInfo(name = "first_name")
    var firstName: String="",

    @ColumnInfo(name = "last_name")
    var lastName: String="",

    @ColumnInfo(name = "password")
    var password: String="",

    @ColumnInfo(name = "username")
    var username: String=""
)
