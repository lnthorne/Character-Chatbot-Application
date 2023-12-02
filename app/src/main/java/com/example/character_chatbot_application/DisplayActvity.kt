package com.example.character_chatbot_application

import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.Button
import android.widget.EditText
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
    private lateinit var saveBtn: Button
    private var isEditMode = false
    var id by Delegates.notNull<Int>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_display_history)

        name = findViewById(R.id.nameTV)
        description = findViewById(R.id.descriptionTV)
        val deleteBtn = findViewById<Button>(R.id.deleteButton)
        val saveBtn = findViewById<Button>(R.id.saveButton)
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

        name.setOnClickListener { toggleEditMode(name) }
        description.setOnClickListener { toggleEditMode(description) }

        deleteBtn.setOnClickListener {
            if (id != -1) {
                characterViewModel.deleteEntryById(id)
                Toast.makeText(this, "Entry deleted", Toast.LENGTH_SHORT).show()
                finish()
            }
        }

        saveBtn.setOnClickListener {
            val newName = name.text.toString()
            val newDescription = description.text.toString()

            characterViewModel.allCharacterLiveData.observe(this, Observer { savedCharacter ->
                val entry = savedCharacter.find { it.id == id }
                entry?.let {
                    it.name = newName
                    it.description = newDescription
                    characterViewModel.updateCharacter(it)
                }
            })
        }
    }
    private fun toggleEditMode(viewToEdit: TextView?) {
        isEditMode = !isEditMode
        if (isEditMode) {
            viewToEdit?.let {
                val text = it.text.toString()
                val parent = it.parent as ViewGroup
                val index = parent.indexOfChild(it)
                parent.removeViewAt(index)

                val editText = EditText(this)
                editText.setText(text)
                editText.requestFocus()
                editText.setSelection(editText.length())
                editText.setOnEditorActionListener { _, actionId, _ ->
                    if (actionId == EditorInfo.IME_ACTION_DONE) {
                        it.text = editText.text
                        parent.addView(it, index)
                        toggleEditMode(null)
                    }
                    true
                }
                parent.addView(editText, index, it.layoutParams)
            }
        } else {
            viewToEdit?.let {
                val text = it.text.toString()
                val parent = it.parent as ViewGroup
                val index = parent.indexOfChild(it)
                parent.removeViewAt(index)

                val textView = TextView(this)
                textView.text = text
                textView.layoutParams = it.layoutParams
                parent.addView(textView, index)
            }
        }
    }

}