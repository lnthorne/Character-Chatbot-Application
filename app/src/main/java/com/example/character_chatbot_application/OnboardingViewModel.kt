package com.example.character_chatbot_application

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.example.character_chatbot_application.data.models.User
import com.example.character_chatbot_application.repositorys.StoryRepository
import com.example.character_chatbot_application.data.models.Character
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch

class OnboardingViewModel( private val frameId : Int, private val userId : Int, private val repository : StoryRepository ) : ViewModel() {
    val users = repository.allUsers.asLiveData()
    val currentUser = MutableLiveData<User>()
//    val currentUser : LiveData<User> get() = _currentUser
    val currentUserId = MutableLiveData<Int>()
    init {

    }
    fun getUserById(userid : Int) : User? {
        val userList = users.value ?: return null
        for (user in userList) {
            if (user.id == userId) {
                return user
            }
        }
        return null
    }
    fun registerUser(user : User) {
        repository.registerUser(user) {
            CoroutineScope(Dispatchers.Main).launch {
                currentUserId.value = it
                currentUser.value = user
            }
        }
    }
    // or change the call signature to take inputs instead of a user
    // (firstName:String,lastName:String,password:String,username:String)
    // val user = User()
    // user.firstName = firstName
    // user.lastName = lastName
    // user.password = password
    // user.username = userName
    fun addCharacter(character : Character) {
        repository.insertCharacter(character)
    }
    fun addCharacter( name : String, description : String, goal : String, background : String ) {
        val character = Character()
        character.name = name
        character.description = description
        character.goal = goal
        character.backgroundContext = background
        repository.insertCharacter(character)
    }

    fun swapFragments(supportFragmentManager : FragmentManager, fragment : Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(frameId, fragment)
        transaction.commit()
    }
}