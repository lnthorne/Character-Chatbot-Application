package com.example.character_chatbot_application

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button

class InitOnboardingFragment(private val clickListener : View.OnClickListener) : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_init_onboarding, container, false)
        val button : Button = view.findViewById(R.id.createButton)
        button.setOnClickListener(clickListener)
        return view
    }

    override fun onDestroy() {
        super.onDestroy()
        println("Fragment is destroyed")
    }
}