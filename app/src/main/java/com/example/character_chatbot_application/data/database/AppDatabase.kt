package com.example.character_chatbot_application.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.character_chatbot_application.data.daos.CharacterDao
import com.example.character_chatbot_application.data.daos.MessageDao
import com.example.character_chatbot_application.data.daos.UserDao
import com.example.character_chatbot_application.data.models.Character
import com.example.character_chatbot_application.data.models.Message
import com.example.character_chatbot_application.data.models.User

@Database(entities = [User::class, Character::class, Message::class], version = 1)
abstract class AppDatabase: RoomDatabase() {
    abstract val userDao: UserDao
    abstract val characterDao: CharacterDao
    abstract val messageDao: MessageDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context) : AppDatabase{
            synchronized(this){
                var instance = INSTANCE
                if(instance == null){
                    instance = Room.databaseBuilder(context.applicationContext,
                        AppDatabase::class.java, "app_database").build()
                    INSTANCE = instance
                }
                return instance
            }
        }

    }
}