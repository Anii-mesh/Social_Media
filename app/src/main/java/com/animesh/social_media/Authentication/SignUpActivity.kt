package com.animesh.social_media.Authentication

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.animesh.social_media.Model.User
import com.animesh.social_media.databinding.ActivitySignUpBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class SignUpActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var database: FirebaseDatabase
    private lateinit var binding: ActivitySignUpBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()

        binding.signUpButton.setOnClickListener {
            val email = binding.emailET.text.toString().trim()
            val password = binding.passwordET.text.toString().trim()
            val name = binding.nameET.text.toString().trim()
            val profession = binding.professionET.text.toString().trim()

            if (email.isEmpty() || password.isEmpty() || name.isEmpty() || profession.isEmpty()) {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val uid = task.result?.user?.uid
                        Log.d("SignUp", "Firebase Auth Success, UID: $uid")

                        if (uid != null) {
                            val user = User(name, profession, email, password)

                            database.reference.child("users").child(uid)
                                .setValue(user)
                                .addOnSuccessListener {
                                    Toast.makeText(this, "User data saved", Toast.LENGTH_SHORT).show()
                                    // Optional: go to main screen
                                    startActivity(Intent(this, LoginActivity::class.java))
                                    finish()
                                }
                                .addOnFailureListener { e ->
                                    Toast.makeText(this, "DB write failed: ${e.message}", Toast.LENGTH_SHORT).show()
                                }
                        } else {
                            Toast.makeText(this, "User ID not found", Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        val errorMsg = task.exception?.localizedMessage ?: "Something went wrong"
                        Toast.makeText(this, errorMsg, Toast.LENGTH_SHORT).show()
                        Log.e("SignUp", "Auth failed: $errorMsg")
                    }
                }
        }

        binding.goToLogin.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
        }
    }
}
