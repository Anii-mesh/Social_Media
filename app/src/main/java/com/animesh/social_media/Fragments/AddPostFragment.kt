package com.animesh.social_media.Fragments

import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import com.animesh.social_media.Model.Post
import com.animesh.social_media.Model.User
import com.animesh.social_media.R
import com.animesh.social_media.databinding.FragmentAddPostBinding
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import java.util.Date
import kotlin.jvm.java


class AddPostFragment : Fragment() {
    lateinit var binding : FragmentAddPostBinding
    private lateinit var auth : FirebaseAuth
    private lateinit var database : FirebaseDatabase
    private lateinit var storage : FirebaseStorage
    private lateinit var uri : Uri

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()
        storage = FirebaseStorage.getInstance()

    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment
        binding = FragmentAddPostBinding.inflate(inflater, container, false)
        val uid: String = auth.currentUser?.uid.toString()
        database.getReference().child("users")
            .child(uid).addListenerForSingleValueEvent(object :ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (!snapshot.exists()) return
                    val user = snapshot.getValue(User::class.java) ?: return
                    binding.name.text = user.name
                    binding.profession.text = user.profession

                    Glide.with(requireContext())
                        .load(user.profilePhoto)
                        .placeholder(R.drawable.placeholder)
                        .into(binding.imageProfile)
                }
                override fun onCancelled(error: DatabaseError) {
                }
            })
        binding.postDescription.addTextChangedListener(object : TextWatcher{
            override fun afterTextChanged(s: Editable?) {

            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                T
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val description : String = binding.postDescription.text.toString()
                if(!description.isEmpty()){
                    binding.postBtn.setBackgroundResource(R.drawable.follow_btn_bg)
                    binding.postBtn.setTextColor(ContextCompat.getColor(context, R.color.green))
                    binding.postBtn.isEnabled = true
                }else{
                    binding.postBtn.setBackgroundResource(R.drawable.follow_active_btn)
                    binding.postBtn.setTextColor(ContextCompat.getColor(context, R.color.grey))
                    binding.postBtn.isEnabled = false
                }
            }

        })
        binding.addImage.setOnClickListener {
            galleryLauncher.launch("image/*")
        }
        binding.postBtn.setOnClickListener {

            val dialog = showLoadingDialog(requireContext())

            val storageRef = storage.getReference().child("posts")
                .child(uid)
                .child(Date().time.toString())

            storageRef.putFile(uri).addOnSuccessListener {
                storageRef.downloadUrl.addOnSuccessListener { downloadUrl ->
                    val post = Post(
                        postId = Date().time.toString(),
                        postImage = downloadUrl.toString(), //  use the actual download URL
                        postedBy = uid,
                        postDescription = binding.postDescription.text.toString(),
                        postedAt = Date().time
                    )
                    database.getReference().child("posts")
                        .push()
                        .setValue(post)
                        .addOnSuccessListener {
                            Toast.makeText(context, "Post Uploaded", Toast.LENGTH_SHORT).show()
                            dialog.dismiss()
                        }
                        .addOnFailureListener {
                            Toast.makeText(context, "Failed to upload post", Toast.LENGTH_SHORT).show()
                        }
                }
            }.addOnFailureListener {
                Toast.makeText(context, "Failed to upload image", Toast.LENGTH_SHORT).show()
            }
        }

        return binding.root
    }

    private val galleryLauncher = registerForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            this.uri = it
            binding.postImage.setImageURI(uri)
            binding.postImage.visibility = View.VISIBLE
            binding.postBtn.setBackgroundResource(R.drawable.follow_btn_bg)
            binding.postBtn.setTextColor(ContextCompat.getColor(context, R.color.green))
            binding.postBtn.isEnabled = true
        }
    }
    fun showLoadingDialog(context: Context): AlertDialog {
        val progressBar = ProgressBar(context)
        val dialog = AlertDialog.Builder(context)
            .setTitle("Post Uploading")
            .setMessage("Please Wait...")
            .setView(progressBar)
            .setCancelable(false)
            .create()
        dialog.show()
        return dialog
    }

}