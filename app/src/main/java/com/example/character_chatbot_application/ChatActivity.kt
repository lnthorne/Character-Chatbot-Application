package com.example.character_chatbot_application

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import java.util.ArrayList

class ChatActivity:AppCompatActivity() {
    private lateinit var chatFrag: ChatFragment
    private lateinit var saveFrag:SaveFragment
    private lateinit var viewPager2: ViewPager2
    private lateinit var tabLayout: TabLayout
    private lateinit var myMyFragmentStateAdapter: FragmentStateAdapter
    private lateinit var fragments: ArrayList<Fragment>
    private val tabTitles = arrayOf("CHAT", "SAVE") //Tab titles
    private lateinit var tabConfigurationStrategy: TabLayoutMediator.TabConfigurationStrategy
    private lateinit var tabLayoutMediator: TabLayoutMediator

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        viewPager2 = findViewById(R.id.viewpager)
        tabLayout = findViewById(R.id.tab)

        chatFrag = ChatFragment()
        saveFrag = SaveFragment()

        fragments = ArrayList()
        fragments.add(chatFrag)
        fragments.add(saveFrag)

        myMyFragmentStateAdapter = FragmentStateAdapter(this, fragments)
        viewPager2.adapter = myMyFragmentStateAdapter

        tabConfigurationStrategy =
            TabLayoutMediator.TabConfigurationStrategy { tab: TabLayout.Tab, position: Int ->
                tab.text = tabTitles[position]
            }
        tabLayoutMediator = TabLayoutMediator(tabLayout, viewPager2, tabConfigurationStrategy)
        tabLayoutMediator.attach()
    }

    override fun onDestroy() {
        super.onDestroy()
        tabLayoutMediator.detach()
    }
}