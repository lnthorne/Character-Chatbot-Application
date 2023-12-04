package com.example.character_chatbot_application.ViewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import com.example.character_chatbot_application.data.models.Character
import com.example.character_chatbot_application.repositorys.StoryRepository
import kotlinx.coroutines.flow.Flow

class CharacterViewModel(private val repository: StoryRepository) : ViewModel() {
    val allCharacterLiveData: LiveData<List<Character>> = repository.allCharacterEntries.asLiveData()
    fun insert(entry: Character) {
        repository.insertCharacter(entry)
    }

    fun deleteEntryById(id: Int) {
        repository.deleteCharacterById(id)
    }

    fun updateCharacter(character: Character) {
        viewModelScope.launch {
            repository.updateCharacter(character)
        }
    }

    fun getCharacterByCharacterId(characterId: Int): LiveData<Character> {
        return repository.getCharacterByCharacterId(characterId)
    }

    fun updateCharacterNameAndDescription(id: Int, newName: String, newDescription: String) {
        viewModelScope.launch {
            repository.updateCharacterNameAndDescription(id, newName, newDescription)
        }
    }

}