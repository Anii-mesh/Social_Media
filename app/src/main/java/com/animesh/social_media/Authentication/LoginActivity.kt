package com.animesh.social_media.Authentication

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.animesh.social_media.MainActivity
import com.animesh.social_media.R
import com.animesh.social_media.databinding.ActivityLoginBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class LoginActivity : AppCompatActivity() {

    private lateinit var binding : ActivityLoginBinding
    private lateinit var auth : FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityLoginBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        auth = FirebaseAuth.getInstance()


        binding.loginButton.setOnClickListener {
            val email = binding.emailET.text.toString()
            val password = binding.passwordET.text.toString()

            auth.signInWithEmailAndPassword(email,password).addOnCompleteListener {task ->
                if (task.isSuccessful){
                    startActivity(Intent(this, MainActivity::class.java))
                    finish()
                }
            }

        }

        binding.goToSignUp.setOnClickListener {

            val intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)
            finish()
        }

    }

    override fun onStart() {
        super.onStart()
        val currentUser = auth.currentUser
        if(currentUser != null){
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
    }
}