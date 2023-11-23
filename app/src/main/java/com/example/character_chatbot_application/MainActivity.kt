package com.example.character_chatbot_application

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.FrameLayout
import androidx.fragment.app.FragmentManager
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
    private lateinit var initOnboardingFragment: InitOnboardingFragment
    private lateinit var promptOnboardingFragment: PromptOnboardingFragment
    private lateinit var currentUser : User

    private lateinit var repository: StoryRepository
    private lateinit var cdao : CharacterDao
    private lateinit var mdao : MessageDao
    private lateinit var udao : UserDao
    private lateinit var database : AppDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        frameLayout = findViewById(R.id.frame)
        fragmentManager = supportFragmentManager
        // if no character created. -> go into fragment init onboarding,
        // else set fragment to home

        database = AppDatabase.getInstance(this)

        cdao = database.characterDao
        mdao = database.messageDao
        udao = database.userDao
        repository = StoryRepository(udao, cdao, mdao)

        // move the swap into viewmodel. then change the listeners = viewmodel.swap(fragmenttype)

        val sharedPreferences = getSharedPreferences("userinfo", MODE_PRIVATE)
        val userid = sharedPreferences.getInt(USER_ID_KEY, -1)
        if (userid < 0) {
            currentUser = User()
            val editor = sharedPreferences.edit()
            editor.putInt(USER_ID_KEY, currentUser.id)
            editor.apply()
            repository.registerUser(currentUser)
        } else {
            val user = repository.getUserById(userid)
            if (user == null) {
                throw Exception("User created but not in database.")
            } else {
                currentUser = user
            }
        }

        val initClickListener = View.OnClickListener {
            val swapTransaction = supportFragmentManager.beginTransaction()
            swapTransaction.replace(R.id.frame, promptOnboardingFragment)
            swapTransaction.commit()
        }
        val onboardingClickListener = View.OnClickListener {
            val swapTransaction = supportFragmentManager.beginTransaction()
            // CREATE MAIN FRAGMENT
            // Swap to main fragment
            swapTransaction.commit()
        }

        initOnboardingFragment = InitOnboardingFragment(initClickListener)
        promptOnboardingFragment = PromptOnboardingFragment(currentUser)

        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.frame, initOnboardingFragment)
        transaction.commit()


    }
    companion object {
        const val USER_ID_KEY = "userid"
    }

}