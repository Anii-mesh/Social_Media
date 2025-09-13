package com.animesh.social_media

import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import com.animesh.social_media.Fragments.AddFragment
import com.animesh.social_media.Fragments.HomeFragment
import com.animesh.social_media.Fragments.NotificationFragment
import com.animesh.social_media.Fragments.ProfileFragment
import com.animesh.social_media.Fragments.SearchFragment
import com.animesh.social_media.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    lateinit var viewBinding : ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        viewBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)

        setSupportActionBar(viewBinding.toolbar)
        supportActionBar?.title = "My Profile"

        // Load default fragment
        loadFragment(HomeFragment())

        viewBinding.toolbar.visibility = View.GONE
        viewBinding.bottomNavigation.setOnItemSelectedListener {
            item ->
            when(item.itemId){
                R.id.nav_home -> {
                    loadFragment(HomeFragment())
                    viewBinding.toolbar.visibility = View.GONE
                }
                R.id.nav_notification -> {
                    loadFragment(NotificationFragment())
                    viewBinding.toolbar.visibility = View.GONE
                }
                R.id.nav_add -> {
                    loadFragment(AddFragment())
                    viewBinding.toolbar.visibility = View.GONE
                }
                R.id.nav_search -> {
                    loadFragment(SearchFragment())
                    viewBinding.toolbar.visibility = View.GONE
                }
                R.id.nav_profile -> {
                    loadFragment(ProfileFragment())
                    viewBinding.toolbar.visibility = View.VISIBLE
                }

            }
            true
        }

    }

    private fun loadFragment(fragment : Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.Container,fragment)
            .commit()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.top_menu_item,menu)
        return true
    }

}