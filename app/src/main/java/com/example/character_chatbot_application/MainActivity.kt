package com.example.character_chatbot_application

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.FrameLayout
import androidx.fragment.app.FragmentManager

class MainActivity : AppCompatActivity() {
    private lateinit var frameLayout : FrameLayout
    private lateinit var fragmentManager : FragmentManager
    private lateinit var initOnboardingFragment: InitOnboardingFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        frameLayout = findViewById(R.id.frame)
        fragmentManager = supportFragmentManager
        // if no character created. -> go into fragment init onboarding,
        // else set fragment to home

        val transaction = supportFragmentManager.beginTransaction()
        val clickListener = View.OnClickListener {
            val swapTransaction = supportFragmentManager.beginTransaction()
            val promptOnboardingFragment = PromptOnboardingFragment()
            swapTransaction.replace(R.id.frame, promptOnboardingFragment)
            swapTransaction.commit()
        }
        initOnboardingFragment = InitOnboardingFragment(clickListener)
        transaction.replace(R.id.frame, initOnboardingFragment)
        transaction.commit()
    }

}