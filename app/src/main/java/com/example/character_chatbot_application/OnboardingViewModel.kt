package com.example.character_chatbot_application

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.character_chatbot_application.data.models.User
import com.example.character_chatbot_application.repositorys.StoryRepository

class OnboardingViewModel( private val repository : StoryRepository ) : ViewModel() {
    private lateinit var currentUser : LiveData<User>
    fun getUser(id : Int) {
        repository.getUserById(id)
    }
    fun registerUser(user : User) {
        val existingUser : User? = repository.getUserById(user.id)
        if (existingUser != null) {
            // throw some error or something
        } else {
            repository.registerUser(user)
        }
    }
    fun addCharacter() {
        repository.insertCharacter()
    }
}