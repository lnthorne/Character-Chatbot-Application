package com.example.character_chatbot_application

import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.character_chatbot_application.ViewModels.CharacterViewModel
import com.example.character_chatbot_application.ViewModels.CharacterViewModelFactory
import com.example.character_chatbot_application.data.daos.CharacterDao
import com.example.character_chatbot_application.data.database.AppDatabase
import com.example.character_chatbot_application.repositorys.StoryRepository
import kotlin.properties.Delegates

class DisplayActivity : AppCompatActivity() {
    private lateinit var name: TextView
    private lateinit var description: TextView

    private lateinit var database: AppDatabase
    private lateinit var databaseDao: CharacterDao
    private lateinit var repository: StoryRepository
    private lateinit var viewModelFactory: CharacterViewModelFactory
    private lateinit var characterViewModel: CharacterViewModel
    var id by Delegates.notNull<Int>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_display_history)

        name = findViewById(R.id.nameTV)
        description = findViewById(R.id.descriptionTV)
        val deleteBtn = findViewById<Button>(R.id.deleteButton)
        val backBtn = findViewById<Button>(R.id.backButton)
        database = AppDatabase.getInstance(this)
        databaseDao = database.characterDao
        repository = StoryRepository(database.userDao, database.characterDao, database.messageDao)
        viewModelFactory = CharacterViewModelFactory(repository)
        characterViewModel =
            ViewModelProvider(this, viewModelFactory).get(CharacterViewModel::class.java)

        characterViewModel.allCharacterLiveData.observe(this, Observer { savedCharacter ->
            val entry = savedCharacter.find{it.id == id}
            if (entry!=null){
                name.text = entry.name
                description.text = entry.description
            }
        })
        backBtn.setOnClickListener {
            finish()
        }
        deleteBtn.setOnClickListener {
            if (id != -1) {
                characterViewModel.deleteEntryById(id)
                Toast.makeText(this, "Entry deleted", Toast.LENGTH_SHORT).show()
                finish()
            }
        }

    }
}