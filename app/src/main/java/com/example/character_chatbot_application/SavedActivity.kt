package com.example.character_chatbot_application

import android.os.Bundle
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.character_chatbot_application.ViewModels.CharacterViewModel
import com.example.character_chatbot_application.ViewModels.CharacterViewModelFactory
import com.example.character_chatbot_application.data.daos.CharacterDao
import com.example.character_chatbot_application.data.database.AppDatabase
import com.example.character_chatbot_application.data.models.SavedCharacter
import com.example.character_chatbot_application.repositorys.StoryRepository

class SavedActivity : AppCompatActivity() {
    private lateinit var myListView: ListView
    private lateinit var arrayList: ArrayList<SavedCharacter>
    private lateinit var arrayAdapter: ListViewAdapter
    private lateinit var database: AppDatabase
    private lateinit var databaseDao: CharacterDao
    private lateinit var repository: StoryRepository
    private lateinit var viewModelFactory: CharacterViewModelFactory
    private lateinit var characterViewModel: CharacterViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_saved)
        myListView = findViewById(R.id.listView)
        arrayList = ArrayList()
        arrayAdapter = ListViewAdapter(this, arrayList)
        myListView.adapter = arrayAdapter
        database = AppDatabase.getInstance(this)
        databaseDao = database.characterDao
        repository = StoryRepository(database.userDao, database.characterDao, database.messageDao)
        viewModelFactory = CharacterViewModelFactory(repository)
        characterViewModel =
            ViewModelProvider(this, viewModelFactory).get(CharacterViewModel::class.java)

        characterViewModel.allCharacterLiveData.observe(this, Observer { savedCharacter ->
            arrayList.clear()
            arrayList.addAll(savedCharacter)
            arrayAdapter.notifyDataSetChanged()
        })


    }
}

