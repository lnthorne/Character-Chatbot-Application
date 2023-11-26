package com.example.character_chatbot_application

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.FrameLayout
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModelProvider
import com.example.character_chatbot_application.data.daos.CharacterDao
import com.example.character_chatbot_application.data.daos.MessageDao
import com.example.character_chatbot_application.data.daos.UserDao
import com.example.character_chatbot_application.data.database.AppDatabase
import com.example.character_chatbot_application.data.models.User
import com.example.character_chatbot_application.repositorys.StoryRepository
import java.lang.Exception

class MainActivity : AppCompatActivity() {
    private lateinit var frameLayout : FrameLayout
    private lateinit var fragmentManager : FragmentManager

    private lateinit var currentUser : User

    private lateinit var repository: StoryRepository

    private lateinit var viewModel: OnboardingViewModel

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


        val sharedPreferences = getSharedPreferences("userinfo", MODE_PRIVATE)
        val userid = sharedPreferences.getInt(USER_ID_KEY, -1)
        println(userid)

        val viewModelFactory = OnboardingViewModelFactory(R.id.frame, userid, repository)
        viewModel = ViewModelProvider(this, viewModelFactory)[OnboardingViewModel::class.java]

        viewModel.currentUser.observe(this) {
            currentUser = viewModel.getCurrentUser()
            println("help me ${currentUser.id}")
            val editor = sharedPreferences.edit()
            editor.putInt(USER_ID_KEY, currentUser.id)
            println("saving uid")
            editor.apply()
        }


        val initOnboardingFragment = InitOnboardingFragment(initClickListener)

        viewModel.swapFragments(supportFragmentManager, initOnboardingFragment)

    }

    private val initClickListener = View.OnClickListener {
        val promptOnboardingFragment = PromptOnboardingFragment(currentUser)
        viewModel.swapFragments(supportFragmentManager, promptOnboardingFragment)
    }

    private val onboardingClickListener = View.OnClickListener {
        // CREATE MAIN FRAGMENT
        // Swap to main fragment
//        viewModel.swapFragments(supportFragmentManager, mainfragment)
    }

    companion object {
        const val USER_ID_KEY = "userid"
    }
}