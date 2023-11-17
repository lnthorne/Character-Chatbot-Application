package com.example.character_chatbot_application.data.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.character_chatbot_application.data.models.Character

@Dao
interface CharacterDao {
    @Insert
    suspend fun insertCharacter(character: Character)

    @Query("SELECT * FROM character_table WHERE userId = :userId")
    suspend fun getCharactersByUserId(userId: Int)

    @Query("DELETE FROM character_table WHERE id = :id")
    suspend fun deleteCharacterById(id: Int)
}