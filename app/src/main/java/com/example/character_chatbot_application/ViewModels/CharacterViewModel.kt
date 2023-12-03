package com.example.character_chatbot_application.ViewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.example.character_chatbot_application.data.models.Character
import com.example.character_chatbot_application.repositorys.StoryRepository

class CharacterViewModel(private val repository: StoryRepository) : ViewModel() {
    val allCharacterLiveData: LiveData<List<Character>> = repository.allCharacterEntries.asLiveData()
    fun insert(entry: Character) {
        repository.insertCharacter(entry)
    }

    fun deleteEntryById(id: Int) {
        repository.deleteCharacterById(id)
    }

    fun updateCharacter(character: Character){
        repository.updateCharacter(character)
//        From the updated repository
    }

}