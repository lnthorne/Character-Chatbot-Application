package com.example.character_chatbot_application

import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.FrameLayout
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import com.example.character_chatbot_application.data.database.AppDatabase
import com.example.character_chatbot_application.data.models.User
import com.example.character_chatbot_application.repositorys.StoryRepository
import com.example.character_chatbot_application.data.models.Character

class MainActivity : AppCompatActivity() {
    private lateinit var frameLayout : FrameLayout
    private lateinit var fragmentManager : FragmentManager

    private lateinit var sharedPreferences : SharedPreferences
    private var userid = -1
    private lateinit var currentUser : User

    private lateinit var repository: StoryRepository

    private lateinit var viewModel: OnboardingViewModel
    private lateinit var promptOnboardingFragment: PromptOnboardingFragment
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        frameLayout = findViewById(R.id.frame)
        fragmentManager = supportFragmentManager
        // if no character created. -> go into fragment init onboarding,
        // else set fragment to home

        val database = AppDatabase.getInstance(this)

        val cdao = database.characterDao
        val mdao = database.messageDao
        val udao = database.userDao
        repository = StoryRepository(udao, cdao, mdao)


        sharedPreferences = getSharedPreferences("userinfo", MODE_PRIVATE)
        userid = sharedPreferences.getInt(USER_ID_KEY, -1)
        println(userid)

        val viewModelFactory = OnboardingViewModelFactory(R.id.frame, repository)
        viewModel = ViewModelProvider(this, viewModelFactory)[OnboardingViewModel::class.java]

        viewModel.users.observe(this, testObserver)

        viewModel.currentUser.observe(this) {
            println("User is $it")
        }
        viewModel.currentUserId.observe(this, idObserver)

        // if there is character.
        val initOnboardingFragment = InitOnboardingFragment(initClickListener)

        viewModel.swapFragments(supportFragmentManager, initOnboardingFragment)

    }

    private fun saveUserId(id : Int) {
        val editor = sharedPreferences.edit()
        editor.putInt(USER_ID_KEY, id)
        println("saving uid as $id")
        editor.apply()
    }

    private val testObserver = Observer<List<User>> {
        onInitialize(it)
    }

    private val idObserver = Observer<Int> {
        println("Userid is $it")
        saveUserId(it)
        val duser = viewModel.getUserById(it)
        println("getting current user")
        println(duser)
        if (duser != null) {
            currentUser = duser
        }
    }

    private fun onInitialize (it : List<User>) {
        println("We have users")
        println(it)
        // wait for initialization
        viewModel.users.removeObserver(testObserver)

        val hasUser : User? = viewModel.getUserById(userid)
        if (hasUser != null) {
            println("Existing user $userid")
            currentUser = hasUser
            viewModel.currentUserId.removeObserver(idObserver)
            viewModel.currentUserId.value = userid
            val frag = WelcomeFragment()
            viewModel.swapFragments(supportFragmentManager, frag)
        } else {
            val newUser = User(
                id = 0,
                firstName = "",
                lastName = "",
                password = "",
                username = ""
            )
            currentUser = newUser
            viewModel.registerUser(newUser)
        }
    }

    private val initClickListener = View.OnClickListener {
        promptOnboardingFragment = PromptOnboardingFragment(insertPromptClickListener)
        viewModel.swapFragments(supportFragmentManager, promptOnboardingFragment)
    }

    private val insertPromptClickListener = View.OnClickListener {
        val editText : EditText = it as EditText
        val description = it.text.toString()
        val character = Character(
            id = 0,
            userId = viewModel.currentUserId.value!!,
            name = "",
            description = description,
            goal = "",
            backgroundContext = ""
        )
        println("Inserting character $character")
        viewModel.addCharacter(character)
        println("Inserted character")
        println("Finished Onboarding")
        val frag = WelcomeFragment()
        viewModel.swapFragments(supportFragmentManager, frag)

//        val transaction = supportFragmentManager.beginTransaction()
//        transaction.remove(promptOnboardingFragment)
//        transaction.commit()
        // CREATE MAIN FRAGMENT
        // Swap to main fragment
//        viewModel.swapFragments(supportFragmentManager, mainfragment)
    }

    companion object {
        const val USER_ID_KEY = "userid"
    }
}