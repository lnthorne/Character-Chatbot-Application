package com.example.character_chatbot_application

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText

class PromptOnboardingFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_prompt_onboarding, container, false)

        val descriptionEdit : EditText = view.findViewById(R.id.desctiption)
        val finishButton : Button = view.findViewById(R.id.finishButton)

        finishButton.setOnClickListener {
            descriptionEdit.text.toString()

            // view model.addCharacter()
        }



        return view
    }

}