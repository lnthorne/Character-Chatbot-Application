package com.example.character_chatbot_application

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.example.character_chatbot_application.data.models.Character

class ListViewAdapter(private val context: Context, private var characterList: List<Character>) : BaseAdapter(){

    override fun getItem(position: Int): Any {
        return characterList.get(position)
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getCount(): Int {
        return characterList.size
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view = View.inflate(context, R.layout.character_entry_item, null)
        val listItemTitle = view.findViewById<TextView>(R.id.title)
        val listItemDetail = view.findViewById<TextView>(R.id.details)
        val listItem = characterList[position]
        listItemTitle.text = "${listItem.name}"
        listItemDetail.text = "${listItem.description}"

        return view

    }

}