package com.example.character_chatbot_application

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.character_chatbot_application.repositorys.StoryRepository

class OnboardingViewModelFactory(private val frameId : Int, private val userId : Int, private val repository : StoryRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(OnboardingViewModel::class.java)) {
            return OnboardingViewModel(frameId, userId, repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}