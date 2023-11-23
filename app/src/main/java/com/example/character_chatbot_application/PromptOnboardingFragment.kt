package com.example.character_chatbot_application

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import com.example.character_chatbot_application.data.daos.CharacterDao
import com.example.character_chatbot_application.data.daos.MessageDao
import com.example.character_chatbot_application.data.daos.UserDao
import com.example.character_chatbot_application.data.database.AppDatabase
import com.example.character_chatbot_application.data.models.Character
import com.example.character_chatbot_application.data.models.User
import com.example.character_chatbot_application.repositorys.StoryRepository

class PromptOnboardingFragment(private val currentUser : User) : Fragment() {
    private lateinit var repository: StoryRepository
    private lateinit var cdao : CharacterDao
    private lateinit var mdao : MessageDao
    private lateinit var udao : UserDao
    private lateinit var database : AppDatabase

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_prompt_onboarding, container, false)
        database = AppDatabase.getInstance(requireActivity())

        cdao = database.characterDao
        mdao = database.messageDao
        udao = database.userDao
        repository = StoryRepository(udao, cdao, mdao)

        val descriptionEdit : EditText = view.findViewById(R.id.desctiption)
        val finishButton : Button = view.findViewById(R.id.finishButton)

        finishButton.setOnClickListener {
            val character = Character()
            character.userId = currentUser.id
            character.description = descriptionEdit.text.toString()
            repository.insertCharacter(character)

            // view model.addCharacter()
        }



        return view
    }

}