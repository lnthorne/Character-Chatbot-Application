package com.example.character_chatbot_application.data.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.character_chatbot_application.data.models.User
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {
    @Insert
    suspend fun insertUser(user: User)

    @Query("SELECT * FROM user_table WHERE id = :userId")
    fun getUserById(userId: Int): Flow<User>?

    @Query("SELECT * FROM user_table WHERE username = :username AND password = :password")
    fun getUserByLogin(username: String, password: String): Flow<User>?

    @Query("DELETE FROM user_table WHERE id = :userId")
    suspend fun deleteUserById(userId: Int)

    @Update
    suspend fun updateUser(user: User)
}