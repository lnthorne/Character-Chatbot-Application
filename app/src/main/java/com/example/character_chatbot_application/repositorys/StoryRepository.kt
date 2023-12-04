package com.example.character_chatbot_application.repositorys

import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import com.example.character_chatbot_application.data.daos.CharacterDao
import com.example.character_chatbot_application.data.daos.MessageDao
import com.example.character_chatbot_application.data.daos.UserDao
import com.example.character_chatbot_application.data.models.Message
import com.example.character_chatbot_application.data.models.Character
import com.example.character_chatbot_application.data.models.User
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class StoryRepository(
    private val userDao: UserDao,
    private val characterDao: CharacterDao,
    private val messageDao: MessageDao,
) {

    val allUsers : Flow<List<User>> = userDao.getUsers()
    val allCharacterEntries: Flow<List<Character>> = characterDao.getAllEntries()
    fun registerUser(user: User, callback : (Int) -> Unit) {
        CoroutineScope(Dispatchers.IO).launch {
//            Hash passwd here
            println("Inserting $user")
            val id = userDao.insertUser(user).toInt()
            callback(id)
        }
    }

    fun getUserById(id: Int): LiveData<User>? {
        return userDao.getUserById(id)?.asLiveData()
    }

    fun getUserByLogin(username: String, password: String): LiveData<User>? {
        return userDao.getUserByLogin(username, password)?.asLiveData()
    }

    fun deleteUserById(id: Int) {
        CoroutineScope(Dispatchers.IO).launch {
            userDao.deleteUserById(id)
        }
    }

    fun updateUser(user: User) {
        CoroutineScope(Dispatchers.IO).launch {
            userDao.updateUser(user)
        }
    }

    fun insertCharacter(character: Character) {
        CoroutineScope(Dispatchers.IO).launch {
            characterDao.insertCharacter(character)
        }
    }
    
    fun updateCharacter(character: Character) {
        CoroutineScope(Dispatchers.IO).launch {
            characterDao.updateCharacter(character)
        }
    }

    fun getCharacterByUserId(id: Int): Flow<List<Character>> {
        return characterDao.getCharactersByUserId(id)
    }

    fun getCharacterByCharacterId(characterId: Int): LiveData<Character> {
        return characterDao.getCharacterByCharacterId(characterId).asLiveData()
    }

    suspend fun updateCharacterNameAndDescription(id: Int, newName: String, newDescription: String, newBackground: String) {
        characterDao.updateCharacterNameAndDescription(id, newName, newDescription, newBackground)
    }

    fun deleteCharacterById(id: Int) {
        CoroutineScope(Dispatchers.IO).launch {
            characterDao.deleteCharacterById(id)
        }
    }

    fun insertNewMessage(message: Message) {
        CoroutineScope(Dispatchers.IO).launch {
            messageDao.insertNewMessage(message)
        }
    }

    fun getMessagesFromCharacterId(id: Int): LiveData<List<Message>> {
        return messageDao.getMessagesFromCharacterId(id).asLiveData()
    }
}