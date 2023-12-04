package com.example.character_chatbot_application

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.character_chatbot_application.ViewModels.CharacterViewModel
import com.example.character_chatbot_application.ViewModels.CharacterViewModelFactory
import com.example.character_chatbot_application.data.daos.CharacterDao
import com.example.character_chatbot_application.data.database.AppDatabase
import com.example.character_chatbot_application.data.models.Character
import com.example.character_chatbot_application.repositorys.StoryRepository

class SaveFragment : Fragment() {
    private lateinit var myListView: ListView
    private lateinit var arrayList: ArrayList<Character>
    private lateinit var arrayAdapter: ListViewAdapter
    private lateinit var database: AppDatabase
    private lateinit var databaseDao: CharacterDao
    private lateinit var repository: StoryRepository
    private lateinit var viewModelFactory: CharacterViewModelFactory
    private lateinit var characterViewModel: CharacterViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreate(savedInstanceState)
        val view = inflater.inflate(R.layout.frag_save, container, false)
        myListView = view.findViewById(R.id.listView)
        arrayList = ArrayList()
        arrayAdapter = ListViewAdapter(requireActivity(), arrayList)
        myListView.adapter = arrayAdapter
        database = AppDatabase.getInstance(requireActivity())
        databaseDao = database.characterDao
        repository = StoryRepository(database.userDao, database.characterDao, database.messageDao)
        viewModelFactory = CharacterViewModelFactory(repository)
        characterViewModel =
            ViewModelProvider(
                requireActivity(),
                viewModelFactory
            ).get(CharacterViewModel::class.java)

        characterViewModel.allCharacterLiveData.observe(
            requireActivity(),
            Observer { savedCharacter ->
                Log.i("TEST", savedCharacter.toString())
                arrayList.clear()
                arrayList.addAll(savedCharacter)
                arrayAdapter.notifyDataSetChanged()
            })

        myListView.setOnItemClickListener { parent, view, position, id ->
            val listItem = arrayList[position]
            val intent = Intent(activity, DisplayActivity::class.java)
            intent.putExtra("entryId", listItem.id)
            startActivity(intent)
        }
        return view
    }
}

