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
    private lateinit var nameTV: TextView
    private lateinit var description: TextView
    private lateinit var background: TextView
    private lateinit var database: AppDatabase
    private lateinit var databaseDao: CharacterDao
    private lateinit var repository: StoryRepository
    private lateinit var viewModelFactory: CharacterViewModelFactory
    private lateinit var characterViewModel: CharacterViewModel
    private lateinit var nameEdit: EditText
    private lateinit var descriptionEdit: EditText
    private lateinit var backgroundEdit: EditText
    private var isEditMode = false
    var id by Delegates.notNull<Int>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_display_history)
        id = intent.getIntExtra("entryId", -1)
        nameTV = findViewById(R.id.nameTV)
        description = findViewById(R.id.descriptionTV)
        background = findViewById(R.id.backgroundTV)
        val deleteBtn = findViewById<Button>(R.id.deleteButton)
        val saveBtn = findViewById<Button>(R.id.saveButton)
        database = AppDatabase.getInstance(this)
        databaseDao = database.characterDao
        repository = StoryRepository(database.userDao, database.characterDao, database.messageDao)
        viewModelFactory = CharacterViewModelFactory(repository)
        characterViewModel =
            ViewModelProvider(this, viewModelFactory).get(CharacterViewModel::class.java)

        characterViewModel.allCharacterLiveData.observe(this, Observer { savedCharacter ->
            Log.i("Observer", "Observer triggered")
            val entry = savedCharacter.find { it.id == id }
            if (entry != null) {
                nameTV.text = entry.name
                description.text = entry.description
                background.text = entry.backgroundContext
            }
        })

        nameEdit = EditText(this)
        descriptionEdit = EditText(this)
        backgroundEdit = EditText(this)

        nameTV.setOnClickListener {
            toggleEditMode(nameTV, nameEdit)
        }

        description.setOnClickListener {
            toggleEditMode(description, descriptionEdit)
        }

       background.setOnClickListener {
            toggleEditMode(background, backgroundEdit)
        }

        deleteBtn.setOnClickListener {
            if (id != -1) {
                characterViewModel.deleteEntryById(id)
                Toast.makeText(this, "Entry deleted", Toast.LENGTH_SHORT).show()

                finish()
            }
        }
        saveBtn.setOnClickListener {
            val newName = nameEdit.text.toString()
            val newDescription = descriptionEdit.text.toString()
            val newBackground = backgroundEdit.text.toString()
            Log.d("SaveButton", "New Name: $newName, New Description: $newDescription")

            // Get the original values from TextViews
            val originalDescription = description.text.toString()
            val originalBackground = background.text.toString()
            val originalName = nameTV.text.toString()

            // Update only if there's a change in the corresponding field
            if (newDescription.isNotEmpty() && newDescription != originalDescription) {
                description.text = newDescription
            }
            else {
                // If newBackground is unchanged, retain the original value
                description.text = originalDescription
            }

            if (newName.isNotEmpty() && newName != originalName) {
                nameTV.text = newName
            }else {
                // If newBackground is unchanged, retain the original value
                nameTV.text = originalName
            }

            // Check if newBackground has changed
            if (newBackground.isNotEmpty() && newBackground != originalBackground) {
                background.text = newBackground
            } else {
                // If newBackground is unchanged, retain the original value
                background.text = originalBackground
            }

            // Update ViewModel only with changed values
            characterViewModel.updateCharacterNameAndDescription(id, nameTV.text.toString(), description.text.toString(), background.text.toString())
            finish()
        }
    }
    private fun toggleEditMode(viewToEdit: TextView?, editTextView: EditText?) {
        if (!isEditMode) {
            val text = viewToEdit?.text.toString()
            val parent = viewToEdit?.parent as ViewGroup?
            val index = parent?.indexOfChild(viewToEdit)
            parent?.removeView(viewToEdit)

            editTextView?.setText(text)
            editTextView?.requestFocus()
            editTextView?.setSelection(editTextView.length())
            editTextView?.setOnEditorActionListener { _, actionId, _ ->
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    if (viewToEdit != null) {
                        viewToEdit.text = editTextView.text
                    }
                    parent?.addView(viewToEdit, index ?: 0)
                    isEditMode = false
                }
                true
            }
            parent?.addView(editTextView, index ?: 0, viewToEdit?.layoutParams)
        }
    }

}