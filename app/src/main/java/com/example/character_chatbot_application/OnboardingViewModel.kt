package com.example.character_chatbot_application

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.example.character_chatbot_application.data.models.User
import com.example.character_chatbot_application.repositorys.StoryRepository
import com.example.character_chatbot_application.data.models.Character
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.launch

class OnboardingViewModel( private val frameId : Int, private val repository : StoryRepository ) : ViewModel() {
    var users = repository.allUsers.asLiveData()
    val currentUser = MutableLiveData<User>()

    val currentUserId = MutableLiveData<Int>()


    fun setUserById(userId : Int) {
        val userList = users.value ?: return
        for (user in userList) {
            if (user.id == userId) {
                currentUser.value = user
                currentUserId.value = userId
            }
        }
    }
    fun registerUser(user : User) {
        repository.registerUser(user) {
            CoroutineScope(Dispatchers.Main).launch {
                users = repository.allUsers.asLiveData()
                currentUserId.value = it
                currentUser.value = user
            }
        }
    }
    fun addCharacter(character : Character) {
        repository.insertCharacter(character)
    }


    fun swapFragments(supportFragmentManager : FragmentManager, fragment : Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(frameId, fragment)
        transaction.commit()
    }
}