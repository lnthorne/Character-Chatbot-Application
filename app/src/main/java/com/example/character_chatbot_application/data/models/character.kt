package com.example.character_chatbot_application.data.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(foreignKeys = arrayOf(ForeignKey(entity = user::class,
    parentColumns = arrayOf("id"),
    childColumns = arrayOf("userId"),
    onDelete = ForeignKey.CASCADE)),
    indices = [Index(value = ["userId"])])
data class character(
    @PrimaryKey(autoGenerate = true)
    val id: Int? = null,
    val userId: Int,

    @ColumnInfo(name = "description")
    val description: String,

    @ColumnInfo(name = "goal")
    val goal: String,

    @ColumnInfo(name = "background_context")
    val backgroundContext: String
)
