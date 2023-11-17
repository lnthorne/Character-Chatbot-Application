package com.example.character_chatbot_application.data.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "message_table",
    foreignKeys = arrayOf(
    ForeignKey(entity = Character::class,
    parentColumns = arrayOf("id"),
    childColumns = arrayOf("characterId"),
    onDelete = ForeignKey.CASCADE)
),
    indices = [Index(value = ["characterId"])])
data class Message(
    @PrimaryKey(autoGenerate = true)
    val id: Int? = null,
    val characterId: Int,

    @ColumnInfo(name = "content")
    val content: String,

    @ColumnInfo(name = "timestamp")
    val timestamp: Long,

    @ColumnInfo(name = "is_user")
    val isUser: Boolean
)
