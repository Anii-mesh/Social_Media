package com.animesh.social_media.Fragments

import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.animesh.social_media.Adapter.FriendAdapter
import com.animesh.social_media.Model.FriendModel
import com.animesh.social_media.R
import com.animesh.social_media.User
import com.animesh.social_media.databinding.FragmentProfileBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.squareup.picasso.Picasso


class ProfileFragment : Fragment() {

    private lateinit var binding: FragmentProfileBinding
    private lateinit var firebaseStorage: FirebaseStorage
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var database: FirebaseDatabase
    private var selectedImageUri: Uri? = null
    private var imageType: String? = null
    private lateinit var friendRv: RecyclerView
    private lateinit var friendList: ArrayList<FriendModel>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        firebaseStorage = FirebaseStorage.getInstance()
        firebaseAuth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()
    }

    private val galleryLauncher = registerForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            selectedImageUri = it
            val uid = firebaseAuth.currentUser?.uid
            val type = imageType ?: return@let

            if (uid != null) {
                val storageRef: StorageReference = firebaseStorage.getReference().child(type).child(uid)

                // show preview immediately
                when (type) {
                    "coverPhoto" -> binding.coverPhoto.setImageURI(it)
                    "profilePhoto" -> binding.imageProfile.setImageURI(it)
                }

                storageRef.putFile(it)
                    .addOnSuccessListener {
                        storageRef.downloadUrl.addOnSuccessListener { downloadUri ->
                            // save the download URL (not local URI!)
                            database.reference.child("users").child(uid).child(type)
                                .setValue(downloadUri.toString())
                        }
                        Toast.makeText(context, "$type uploaded", Toast.LENGTH_SHORT).show()
                    }
                    .addOnFailureListener {
                        Toast.makeText(context, "Upload failed", Toast.LENGTH_SHORT).show()
                    }
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProfileBinding.inflate(inflater, container, false)
        val view = binding.root

        val uid = firebaseAuth.currentUser?.uid
        if (uid != null) {
            database.getReference().child("users").child(uid)
                .addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        if (snapshot.exists()) {
                            val user = snapshot.getValue(User::class.java)

                            // Load cover photo safely
                            if (!user?.coverPhoto.isNullOrEmpty()) {
                                Picasso.get()
                                    .load(user.coverPhoto)
                                    .placeholder(R.drawable.placeholder)
                                    .error(R.drawable.placeholder)
                                    .into(binding.coverPhoto)
                            } else {
                                binding.coverPhoto.setImageResource(R.drawable.p2)
                            }

                            // Load profile photo safely
                            if (!user?.profilePhoto.isNullOrEmpty()) {
                                Picasso.get()
                                    .load(user.profilePhoto)
                                    .placeholder(R.drawable.placeholder)
                                    .error(R.drawable.placeholder)
                                    .into(binding.imageProfile)
                            } else {
                                binding.imageProfile.setImageResource(R.drawable.profile)
                            }

                            binding.userName.text = user?.name
                            binding.profession.text = user?.profession
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {}
                })
        }

        // friends recycler
        friendRv = binding.friendRv
        friendList = ArrayList()
        friendList.add(FriendModel(R.drawable.profile))
        friendList.add(FriendModel(R.drawable.p1))
        friendList.add(FriendModel(R.drawable.p2))
        friendList.add(FriendModel(R.drawable.profile))
        friendList.add(FriendModel(R.drawable.p1))


        val friendLayoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        friendRv.layoutManager = friendLayoutManager
        friendRv.adapter = FriendAdapter(friendList)
        friendRv.isNestedScrollingEnabled = false

        // click listeners
        binding.changeCoverPhoto.setOnClickListener {
            imageType = "coverPhoto"
            galleryLauncher.launch("image/*")
        }
        binding.verifiedAccount.setOnClickListener {
            imageType = "profilePhoto"
            galleryLauncher.launch("image/*")
        }

        return view
    }
}
