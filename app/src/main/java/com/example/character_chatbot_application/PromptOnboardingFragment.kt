package com.example.character_chatbot_application

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.lifecycle.ViewModelProvider
import com.example.character_chatbot_application.Util.Globals
import com.example.character_chatbot_application.data.database.AppDatabase
import com.example.character_chatbot_application.data.models.Character
import com.example.character_chatbot_application.repositorys.StoryRepository

class PromptOnboardingFragment(private val swapFragment : View.OnClickListener) : Fragment() {
    private lateinit var descriptionEdit : EditText
    private lateinit var nameEdit: EditText
    private lateinit var bgContextEdit: EditText
    private lateinit var repository: StoryRepository
    private lateinit var viewModel: OnboardingViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_prompt_onboarding, container, false)
        val database = AppDatabase.getInstance(requireContext())
        repository = StoryRepository(database.userDao, database.characterDao, database.messageDao)
        val viewModelFactory = OnboardingViewModelFactory(R.id.frame, repository)
        viewModel = ViewModelProvider(this, viewModelFactory)[OnboardingViewModel::class.java]

        descriptionEdit = view.findViewById(R.id.desctiption)
        nameEdit = view.findViewById(R.id.name)
        bgContextEdit = view.findViewById(R.id.bgContext)
        val finishButton : Button = view.findViewById(R.id.finishButton)

        finishButton.setOnClickListener {
            println("Prompt complete.")
            swapFragment.onClick(descriptionEdit)
        }

        return view
    }

    fun getInputData(): Triple<String, String, String> {
        return Triple(descriptionEdit.text.toString(), nameEdit.text.toString(), bgContextEdit.text.toString())
    }

    override fun onDestroy() {
        println("Destroying prompt fragment.")
        super.onDestroy()
    }

}