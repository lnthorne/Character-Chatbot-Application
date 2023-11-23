package com.example.character_chatbot_application.ViewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.character_chatbot_application.repositorys.StoryRepository

class CharacterViewModelFactory (private val repository: StoryRepository) : ViewModelProvider.Factory {
    override fun<T: ViewModel> create (modelClass: Class<T>) : T{
        if(modelClass.isAssignableFrom(CharacterViewModel::class.java))
            return CharacterViewModel(repository) as T
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}