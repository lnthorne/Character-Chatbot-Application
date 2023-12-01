package com.example.character_chatbot_application

import android.app.AlertDialog
import android.app.Dialog
import android.content.Intent
import android.media.Image
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment

class ChatFragment: Fragment() {
    private lateinit var  sendingMsg: ImageView
    private lateinit var inputBar: EditText
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.frag_chat_chatboard, container,false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val text = TextView(requireContext())
        text.text = String.format("Please enter anything")
        sendingMsg = view.findViewById(R.id.send_msg)
        inputBar = view.findViewById(R.id.chat_input)
        sendingMsg.setOnClickListener(){
            if(inputBar.text.isEmpty()){
                var dialog = AlertDialog.Builder(requireContext())
                    .setTitle("Opps!")
                    .setView(text)
                    .setPositiveButton("OK",null)
                dialog.create()
                dialog.show()
            }
            else{
                // IMPLEMENT CODE HERE
            }

        }
    }
}