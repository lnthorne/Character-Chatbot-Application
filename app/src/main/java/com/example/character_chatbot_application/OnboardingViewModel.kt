package com.example.character_chatbot_application

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.character_chatbot_application.data.models.User
import com.example.character_chatbot_application.repositorys.StoryRepository
import com.example.character_chatbot_application.data.models.Character

class OnboardingViewModel( private val repository : StoryRepository ) : ViewModel() {
    private lateinit var _currentUser : MutableLiveData<User>
    val currentUser : LiveData<User> get() = _currentUser
    fun getUser(id : Int) {
        val user = repository.getUserById(id)
        if (user != null) {
            _currentUser.value = user!!
        }
    }
    fun registerUser(user : User) {
        // or change the call signature to take inputs instead of a user
        // (firstName:String,lastName:String,password:String,username:String)
        // val user = User()
        // user.firstName = firstName
        // user.lastName = lastName
        // user.password = password
        // user.username = userName
        val existingUser : User? = repository.getUserById(user.id)
        if (existingUser != null) {
            // throw some error or something
        } else {
            repository.registerUser(user)
            _currentUser.value = user
        }
    }
    fun addCharacter(character : Character) {
        // or change to
        // (name : String, description : String, goal : String, background : String)
        // val character = Character()
        // caracter.name = name
        // character.userId = currentUser.value?.id
        repository.insertCharacter(character)
    }
}