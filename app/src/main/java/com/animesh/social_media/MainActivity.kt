package com.animesh.social_media

import android.content.Intent
import android.graphics.Rect
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import com.animesh.social_media.Authentication.LoginActivity
import com.animesh.social_media.Fragments.AddFragment
import com.animesh.social_media.Fragments.HomeFragment
import com.animesh.social_media.Fragments.NotificationFragment
import com.animesh.social_media.Fragments.ProfileFragment
import com.animesh.social_media.Fragments.SearchFragment
import com.animesh.social_media.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {

    lateinit var viewBinding : ActivityMainBinding
    lateinit var firebaseAuth : FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        firebaseAuth = FirebaseAuth.getInstance()

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

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.setting -> {
                firebaseAuth.signOut()
                startActivity(Intent(this, LoginActivity::class.java))
                finish()
            }
        }
        return true
    }

    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
        if (ev.action == MotionEvent.ACTION_DOWN) {
            val v = currentFocus
            if (v is EditText) {
                val outRect = Rect()
                v.getGlobalVisibleRect(outRect)
                if (!outRect.contains(ev.rawX.toInt(), ev.rawY.toInt())) {
                    v.clearFocus()
                    val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
                    imm.hideSoftInputFromWindow(v.windowToken, 0)
                }
            }
        }
        return super.dispatchTouchEvent(ev)
    }

}