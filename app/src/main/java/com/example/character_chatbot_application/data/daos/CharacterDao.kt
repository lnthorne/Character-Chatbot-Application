package com.example.character_chatbot_application.data.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.character_chatbot_application.data.models.Character
import kotlinx.coroutines.flow.Flow

@Dao
interface CharacterDao {
    @Insert
    suspend fun insertCharacter(character: Character)

    @Query("SELECT * FROM character_table")
    fun getAllEntries(): Flow<List<Character>>

    @Query("SELECT * FROM character_table WHERE userId = :userId")
     fun getCharactersByUserId(userId: Int): Flow<List<Character>>

    @Query("DELETE FROM character_table WHERE id = :characterId")
    suspend fun deleteCharacterById(characterId: Int)
}