package com.animesh.social_media.Adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.animesh.social_media.Model.FollowModel
import com.animesh.social_media.Model.User
import com.animesh.social_media.R
import com.animesh.social_media.databinding.FriendRvSampleBinding
import com.bumptech.glide.Glide
import com.google.firebase.Firebase
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class FollowerAdapter(private val followerList: List<FollowModel>) :
    RecyclerView.Adapter<FollowerAdapter.FollowerViewHolder>() {

    inner class FollowerViewHolder(val binding: FriendRvSampleBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FollowerViewHolder {
        val binding = FriendRvSampleBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return FollowerViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FollowerViewHolder, position: Int) {
        val follow = followerList[position]

        FirebaseDatabase.getInstance().getReference("users")
            .child(follow.followedBy)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val user = snapshot.getValue(User::class.java) ?: return

                    Glide.with(holder.itemView.context)
                        .load(user.profilePhoto)
                        .placeholder(R.drawable.placeholder)
                        .into(holder.binding.imageProfile)

                }

                override fun onCancelled(error: DatabaseError) {
                    Log.e("FollowerAdapter", "DB error: ${error.message}")
                }
            })
    }

    override fun getItemCount(): Int = followerList.size
}




