package com.example.character_chatbot_application

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnFocusChangeListener
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.character_chatbot_application.Util.RetrofitClient
import com.example.character_chatbot_application.data.database.AppDatabase
import com.example.character_chatbot_application.data.models.Character
import com.example.character_chatbot_application.repositorys.GPTRepository
import com.example.character_chatbot_application.repositorys.StoryRepository

class ChatBoardFragment: Fragment() {
    private lateinit var viewModel: ChatViewModel
    private lateinit var messageAdapter: MessageAdapter
    private lateinit var messageInput: EditText
    private lateinit var linearFeatures: LinearLayout
    private lateinit var character: Character
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.frag_chatboard, container, false)

        // Get an instance of the database
        val database = AppDatabase.getInstance(requireContext())

        // Create GPTService and GPTRepository instances
        val gptService = RetrofitClient.gptService
        val gptRepository = GPTRepository(gptService)
        val storyRepository = StoryRepository(database.userDao, database.characterDao, database.messageDao)

        // Create a ViewModelFactory
        val viewModelFactory = ChatViewModelFactory(storyRepository, gptRepository)

        val toolBarTitle: TextView = view.findViewById(R.id.toolbar_title)

        // Instantiate the ViewModel
        viewModel = ViewModelProvider(this, viewModelFactory).get(ChatViewModel::class.java)
        viewModel.character.observe(requireActivity(), Observer {
            character = it
            toolBarTitle.text = it.name
        })

        setupUI(view)
        setupObservers()

        return view
    }

    private fun setupUI(view: View) {
        messageInput = view.findViewById(R.id.edit_gchat_message)
        linearFeatures = view.findViewById(R.id.linear_features)
        val sendMessageButton = view.findViewById<ImageView>(R.id.button_gchat_send)
        val messagesView = view.findViewById<RecyclerView>(R.id.recycler_gchat)

        messageAdapter = MessageAdapter()
        messagesView.adapter = messageAdapter
        messagesView.layoutManager = LinearLayoutManager(context)

        messageInput.onFocusChangeListener = OnFocusChangeListener { _, hasFocus ->
            // If the EditText has focus, hide the LinearLayout
            if (hasFocus ) {
                linearFeatures.visibility = View.GONE

                messageInput.onFocusChangeListener=null
            } else {
                // If the EditText loses focus, show the LinearLayout
                linearFeatures.visibility = View.VISIBLE
            }
        }

        sendMessageButton.setOnClickListener {
            val messageText = messageInput.text.toString().trim()
            if (messageText.isNotEmpty()) {
                viewModel.sendMessage(messageText, character)
                messageInput.setText("")
            }
        }
    }

    private fun setupObservers() {
        viewModel.messages.observe(viewLifecycleOwner, { messages ->
            messageAdapter.setMessages(messages)
            if (messages.isNotEmpty()) {
                view?.findViewById<RecyclerView>(R.id.recycler_gchat)?.smoothScrollToPosition(messages.size - 1)
            }
        })
    }

}
