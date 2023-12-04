package com.example.character_chatbot_application

import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.FrameLayout
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import com.example.character_chatbot_application.Util.Globals
import com.example.character_chatbot_application.data.database.AppDatabase
import com.example.character_chatbot_application.data.models.User
import com.example.character_chatbot_application.repositorys.StoryRepository
import com.example.character_chatbot_application.data.models.Character

class MainActivity : AppCompatActivity() {
    private lateinit var frameLayout : FrameLayout

    private lateinit var repository: StoryRepository

    private lateinit var viewModel: OnboardingViewModel
    private lateinit var promptOnboardingFragment: PromptOnboardingFragment
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        frameLayout = findViewById(R.id.frame)

        // if no character created. -> go into fragment init onboarding,
        // else set fragment to home

        val database = AppDatabase.getInstance(this)

        val cdao = database.characterDao
        val mdao = database.messageDao
        val udao = database.userDao
        repository = StoryRepository(udao, cdao, mdao)

        val viewModelFactory = OnboardingViewModelFactory(R.id.frame, repository)
        viewModel = ViewModelProvider(this, viewModelFactory)[OnboardingViewModel::class.java]

        viewModel.users.observe(this, userObserver)

        viewModel.currentUser.observe(this) {
            println("User is $it")
        }
        viewModel.currentUserId.observe(this, idObserver)

        // if there is character.
        val initOnboardingFragment = InitOnboardingFragment(initialOnboardingClickListener)

        viewModel.swapFragments(supportFragmentManager, initOnboardingFragment)

    }

    private fun saveUserId(id : Int) {
        val sharedPreferences = getSharedPreferences("userinfo", MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putInt(USER_ID_KEY, id)
        println("saving uid as $id")
        editor.apply()
    }

    private val userObserver = Observer<List<User>> {
        onInitialize(it)
    }

    private val idObserver = Observer<Int> {
        println("Userid is $it")
        saveUserId(it)
        println("getting current user")
        viewModel.setUserById(it)
        println("User is ${viewModel.currentUser.value}")
    }

    private fun onInitialize (it : List<User>) {
        println("There are existing users")
        println(it)
        viewModel.users.removeObserver(userObserver)

        val sharedPreferences = getSharedPreferences("userinfo", MODE_PRIVATE)
        val userid = sharedPreferences.getInt(USER_ID_KEY, -1) // move all user data to vm
        if (userid == -1) {
            val newUser = User(
                id = Globals.USER_ID,
                firstName = "",
                lastName = "",
                password = "",
                username = ""
            )
            viewModel.registerUser(newUser)
        } else {
            viewModel.currentUserId.removeObserver(idObserver)
//            viewModel.currentUser.removeObserver()
            viewModel.setUserById(userid)
            val createdCharacter = sharedPreferences.getBoolean(CHARACTER_CREATED_KEY, false)
            if (createdCharacter) {
                val frag = WelcomeFragment()
                viewModel.swapFragments(supportFragmentManager, frag)
            }
        }
    }

    private val initialOnboardingClickListener = View.OnClickListener {
        promptOnboardingFragment = PromptOnboardingFragment(insertPromptClickListener)
        viewModel.swapFragments(supportFragmentManager, promptOnboardingFragment)
    }

    private val insertPromptClickListener = View.OnClickListener {
        val editText : EditText = it as EditText
        val description = it.text.toString()
        val character = Character(
            id = Globals.CHARACTER_ID,
            userId = viewModel.currentUserId.value!!,
            name = "",
            description = description,
            goal = "",
            backgroundContext = ""
        )
        println("Inserting character $character")
        viewModel.addCharacter(character)
        println("Inserted character $character")
        println("Finished Onboarding")
        val sharedPreferences = getSharedPreferences("userinfo", MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putBoolean(CHARACTER_CREATED_KEY, true)
        editor.commit()
        val frag = WelcomeFragment()
        viewModel.swapFragments(supportFragmentManager, frag)

    }

    companion object {
        const val USER_ID_KEY = "userid"
        const val CHARACTER_CREATED_KEY = "character_created"
    }
}