package com.animesh.social_media.Fragments

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.exifinterface.media.ExifInterface
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.animesh.social_media.Adapter.FollowerAdapter
import com.animesh.social_media.Model.FollowModel
import com.animesh.social_media.Model.User
import com.animesh.social_media.R
import com.animesh.social_media.databinding.FragmentProfileBinding
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.io.ByteArrayOutputStream

class ProfileFragment : Fragment() {

    private lateinit var binding: FragmentProfileBinding
    private lateinit var firebaseStorage: FirebaseStorage
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var database: FirebaseDatabase
    private var imageType: String? = null

    private lateinit var followerRv: RecyclerView
    private lateinit var followerList: ArrayList<FollowModel>
    private lateinit var followerAdapter: FollowerAdapter

    private val galleryLauncher = registerForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            val uid = firebaseAuth.currentUser?.uid ?: return@let
            val type = imageType ?: return@let
            uploadImage(uid, type, it)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProfileBinding.inflate(inflater, container, false)

        // Init Firebase
        firebaseStorage = FirebaseStorage.getInstance()
        firebaseAuth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()

        setupFollowersRecyclerView()
        fetchUserData()
        fetchFollowers()

        binding.changeCoverPhoto.setOnClickListener {
            imageType = "coverPhoto"
            galleryLauncher.launch("image/*")
        }
        binding.verifiedAccount.setOnClickListener {
            imageType = "profilePhoto"
            galleryLauncher.launch("image/*")
        }

        return binding.root
    }

    private fun setupFollowersRecyclerView() {
        followerRv = binding.followRv
        followerList = ArrayList()
        followerAdapter = FollowerAdapter(followerList)

        followerRv.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        followerRv.adapter = followerAdapter
        followerRv.isNestedScrollingEnabled = false
    }

    private fun fetchUserData() {
        val uid = firebaseAuth.currentUser?.uid ?: return
        database.getReference("users").child(uid)
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (!isAdded || !snapshot.exists()) return
                    val user = snapshot.getValue(User::class.java) ?: return

                    binding.userName.text = user.name
                    binding.profession.text = user.profession

                    Glide.with(requireContext())
                        .load(user.coverPhoto)
                        .placeholder(R.drawable.placeholder)
                        .into(binding.coverPhoto)

                    Glide.with(requireContext())
                        .load(user.profilePhoto)
                        .placeholder(R.drawable.profile)
                        .into(binding.imageProfile)
                }

                override fun onCancelled(error: DatabaseError) {}
            })
    }

    private fun fetchFollowers() {
        val currentUserUid = firebaseAuth.currentUser?.uid ?: return
        val followersRef = database.getReference("users").child(currentUserUid).child("followers")

        followersRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                followerList.clear()
                for (ds in snapshot.children) {
                    val followModel = ds.getValue(FollowModel::class.java)
                    if (followModel != null) {
                        followerList.add(followModel)
                    }
                }
                followerAdapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {}
        })
    }


    private fun uploadImage(uid: String, type: String, uri: Uri) {
        val storageRef: StorageReference = firebaseStorage.getReference().child(type).child(uid)

        // Show preview immediately
        when (type) {
            "coverPhoto" -> binding.coverPhoto.setImageURI(uri)
            "profilePhoto" -> binding.imageProfile.setImageURI(uri)
        }

        val bitmap = getCorrectlyOrientedBitmap(requireContext(), uri) ?: return

        val baos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 70, baos)
        val compressedData = baos.toByteArray()

        storageRef.putBytes(compressedData)
            .addOnSuccessListener {
                storageRef.downloadUrl.addOnSuccessListener { downloadUri ->
                    database.reference.child("users").child(uid).child(type)
                        .setValue(downloadUri.toString())
                }
            }
    }

    private fun getCorrectlyOrientedBitmap(context: Context, imageUri: Uri): Bitmap? {
        val inputStream = context.contentResolver.openInputStream(imageUri) ?: return null
        val bitmap = BitmapFactory.decodeStream(inputStream)
        inputStream.close()

        context.contentResolver.openInputStream(imageUri)?.use { exifInputStream ->
            val exif = ExifInterface(exifInputStream)
            val orientation = exif.getAttributeInt(
                ExifInterface.TAG_ORIENTATION,
                ExifInterface.ORIENTATION_NORMAL
            )
            val matrix = Matrix()
            when (orientation) {
                ExifInterface.ORIENTATION_ROTATE_90 -> matrix.postRotate(90f)
                ExifInterface.ORIENTATION_ROTATE_180 -> matrix.postRotate(180f)
                ExifInterface.ORIENTATION_ROTATE_270 -> matrix.postRotate(270f)
            }
            return Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
        }
        return bitmap
    }
}
