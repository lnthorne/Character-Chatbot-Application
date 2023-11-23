package com.example.character_chatbot_application

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.character_chatbot_application.ViewModels.CharacterViewModel
import com.example.character_chatbot_application.ViewModels.CharacterViewModelFactory
import com.example.character_chatbot_application.data.daos.CharacterDao
import com.example.character_chatbot_application.data.database.AppDatabase
import com.example.character_chatbot_application.data.models.SavedCharacter
import com.example.character_chatbot_application.repositorys.StoryRepository

class SavedActivity : AppCompatActivity(){
    private lateinit var myListView: ListView
    private lateinit var arrayList: ArrayList<SavedCharacter>
    private lateinit var arrayAdapter: ListViewAdapter
    private lateinit var database: AppDatabase
    private lateinit var databaseDao: CharacterDao
    private lateinit var repository: StoryRepository
    private lateinit var viewModelFactory: CharacterViewModelFactory
    private lateinit var commentViewModel: CharacterViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_saved)
        myListView = findViewById(R.id.listView)
        arrayList = ArrayList()
        arrayAdapter = ListViewAdapter(this, arrayList)
        myListView.adapter = arrayAdapter
        database = AppDatabase.getInstance(this)
        databaseDao = database.characterDao
        repository = StoryRepository(databaseDao)
    }

    override fun onCreate(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.activity_history, container, false)
        myListView = view.findViewById(R.id.list)



//        val application = requireNotNull(activity).application
        database = ExerciseDatabase.getInstance(requireActivity())
        databaseDao = database.ExerciseEntryDao
        repository = ExerciseEntryRepository(databaseDao)
        viewModelFactory = ExerciseViewModelFactory(repository)
        commentViewModel = ViewModelProvider(requireActivity(), viewModelFactory).get(ExerciseEntryViewModel::class.java)

        commentViewModel.allExerciseLiveData.observe(requireActivity(), Observer { exerciseEntries ->
            arrayList.clear() // Clear the existing list
            arrayList.addAll(exerciseEntries) // Populate arrayList with the data from the ViewModel
            Log.d("HistoryFragment", "Exercise List Size: ${arrayList.size}")
            arrayAdapter.notifyDataSetChanged()
            //arrayAdapter.replace(it)
        })

        myListView.setOnItemClickListener { parent, view, position, id ->
            val listItem = arrayList[position]
            if (listItem.inputType == "Manual Input") {
                Log.d("manual input clicked", listItem.inputType)
                val intent = Intent(requireActivity(), DisplayActivity::class.java)
                intent.putExtra("entryId", listItem.id)
                startActivity(intent)
            }
            if (listItem.inputType == "GPS") {
                Log.d("GPS input clicked", listItem.inputType)
                val intent = Intent(requireActivity(), Map::class.java)
                intent.putExtra("entryId", listItem.id)
                startActivity(intent)
            }
        }

        return view
    }

}