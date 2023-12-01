package com.example.messaging_ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class ChatBoardFragment: Fragment() {
    private lateinit var viewModel: ChatViewModel
    private lateinit var messageAdapter: MessageAdapter
    private lateinit var messageInput: EditText

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.frag_chatboard, container, false)

        viewModel = ViewModelProvider(this).get(ChatViewModel::class.java)

        setupUI(view)
        setupObservers()

        return view
    }

    private fun setupUI(view: View) {
        messageInput = view.findViewById(R.id.edit_gchat_message)
        val sendMessageButton = view.findViewById<ImageView>(R.id.button_gchat_send)
        val messagesView = view.findViewById<RecyclerView>(R.id.recycler_gchat)

        messageAdapter = MessageAdapter()
        messagesView.adapter = messageAdapter
        messagesView.layoutManager = LinearLayoutManager(context)

        sendMessageButton.setOnClickListener {
            val messageText = messageInput.text.toString().trim()
            if (messageText.isNotEmpty()) {
                viewModel.sendMessage(messageText)
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
