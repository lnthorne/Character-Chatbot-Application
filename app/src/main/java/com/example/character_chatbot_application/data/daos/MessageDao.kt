package com.example.character_chatbot_application.data.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.character_chatbot_application.data.models.Message

@Dao
interface MessageDao {
    @Insert
    suspend fun insertNewMessage(message: Message)

    @Query("SELECT * FROM message_table WHERE characterId = :characterId")
    suspend fun getMessagesFromCharacterId(characterId: Int)


}