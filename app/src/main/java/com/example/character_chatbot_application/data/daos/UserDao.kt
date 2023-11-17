package com.example.character_chatbot_application.data.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.character_chatbot_application.data.models.User

@Dao
interface UserDao {
    @Insert
    suspend fun insertUser(user: User)

    @Query("SELECT * FROM user_table WHERE id = :id")
    suspend fun getUserById(id: Int)

    @Query("DELETE FROM user_table WHERE id = :id")
    suspend fun deleteUserById(id: Int)
}