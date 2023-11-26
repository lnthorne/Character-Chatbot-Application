package com.example.messaging_ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.messaging_ui.ChatMessage
import com.example.messaging_ui.MessageType
import com.example.messaging_ui.R

class MessageAdapter : RecyclerView.Adapter<MessageAdapter.MessageViewHolder>() {
    private var messages = listOf<ChatMessage>()

    fun setMessages(newMessages: List<ChatMessage>) {
        messages = newMessages
        notifyDataSetChanged() // Consider using more specific notifications
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageViewHolder {
        val layoutId = if (viewType == MessageType.USER.ordinal) {
            R.layout.item_chat_me
        } else {
            R.layout.item_chat_bot
        }
        val view = LayoutInflater.from(parent.context).inflate(layoutId, parent, false)
        return MessageViewHolder(view, viewType)
    }

    override fun onBindViewHolder(holder: MessageViewHolder, position: Int) {
        val message = messages[position]
        holder.bind(message)
    }

    override fun getItemCount(): Int = messages.size

    override fun getItemViewType(position: Int): Int {
        return messages[position].type.ordinal
    }

    class MessageViewHolder(itemView: View, private val viewType: Int) : RecyclerView.ViewHolder(itemView) {
        fun bind(message: ChatMessage) {
            val textViewId = if (viewType == MessageType.USER.ordinal) {
                R.id.text_gchat_message_me
            } else {
                R.id.text_gchat_message_other
            }
            itemView.findViewById<TextView>(textViewId).text = message.message
        }
    }
}
