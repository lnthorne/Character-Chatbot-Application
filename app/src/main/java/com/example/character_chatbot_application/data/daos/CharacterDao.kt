package com.example.character_chatbot_application.data.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
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

    @Query("SELECT * FROM character_table WHERE userId = :characterId")
    fun getCharacterByCharacterId(characterId: Int): Flow<Character>

     @Update
     suspend fun updateCharacter(character: Character)

    @Query("UPDATE character_table SET name = :newName, description = :newDescription, background_context = :newBackground WHERE id = :characterId")
    suspend fun updateCharacterNameAndDescription(characterId: Int, newName: String, newDescription: String, newBackground: String)


    @Query("DELETE FROM character_table WHERE id = :characterId")
    suspend fun deleteCharacterById(characterId: Int)
}