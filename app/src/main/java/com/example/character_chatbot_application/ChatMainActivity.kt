package com.example.messaging_ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class ChatMainActivity : AppCompatActivity() {
    private lateinit var viewModel: ChatViewModel
    private lateinit var messageAdapter: MessageAdapter
    private lateinit var messageInput: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat_main)

        viewModel = ViewModelProvider(this)[ChatViewModel::class.java]

        setupUI()
        setupObservers()
    }

    private fun setupUI() {
        messageInput = findViewById(R.id.edit_gchat_message)
        val sendMessageButton = findViewById<Button>(R.id.button_gchat_send)
        val messagesView = findViewById<RecyclerView>(R.id.recycler_gchat)

        messageAdapter = MessageAdapter()
        messagesView.adapter = messageAdapter
        messagesView.layoutManager = LinearLayoutManager(this)

        sendMessageButton.setOnClickListener {
            val messageText = messageInput.text.toString().trim()
            if (messageText.isNotEmpty()) {
                viewModel.sendMessage(messageText)
                messageInput.setText("")
            }
        }
    }

    private fun setupObservers() {
        viewModel.messages.observe(this, { messages ->
            messageAdapter.setMessages(messages)
            if (messages.isNotEmpty()) {
                findViewById<RecyclerView>(R.id.recycler_gchat).smoothScrollToPosition(messages.size - 1)
            }
        })
    }

}
