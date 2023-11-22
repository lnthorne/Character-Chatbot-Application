package com.example.character_chatbot_application

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Message
import android.widget.Button
import android.widget.EditText
import com.example.character_chatbot_application.data.daos.CharacterDao
import com.example.character_chatbot_application.data.daos.MessageDao
import com.example.character_chatbot_application.data.daos.UserDao
import com.example.character_chatbot_application.data.database.AppDatabase
import com.example.character_chatbot_application.data.models.Character
import com.example.character_chatbot_application.repositorys.StoryRepository

class OnboardingActivity : AppCompatActivity() {
    private lateinit var descriptionText : EditText
    private lateinit var finishButton : Button

    private lateinit var characterDao : CharacterDao
    private lateinit var messageDao: MessageDao
    private lateinit var userDao: UserDao

    private lateinit var database : AppDatabase
    private lateinit var repository: StoryRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_onboarding)
        descriptionText = findViewById(R.id.prompt)
        finishButton = findViewById(R.id.finishButton)

        database = AppDatabase.getInstance(this)
        characterDao = database.characterDao
        messageDao = database.messageDao
        userDao = database.userDao
        repository = StoryRepository(userDao, characterDao, messageDao)

        finishButton.setOnClickListener {
            val character = Character()
            character.description = descriptionText.text.toString()
            repository.insertCharacter(character)
        }
    }
}