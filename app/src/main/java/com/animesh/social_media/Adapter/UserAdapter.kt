package com.animesh.social_media.Adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.animesh.social_media.Model.FollowModel
import com.animesh.social_media.Model.User
import com.animesh.social_media.R
import com.animesh.social_media.databinding.UserSampleBinding
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import java.time.Clock.system
import java.util.Date

// Constructor now accepts a Set for instant follow status checks
class UserAdapter(private val userList: MutableList<User>, private val followingSet: MutableSet<String>
) : RecyclerView.Adapter<UserAdapter.UserViewHolder>() {

    inner class UserViewHolder(val binding: UserSampleBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val binding = UserSampleBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return UserViewHolder(binding)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val user = userList[position]
        val context = holder.itemView.context
        val currentUid = FirebaseAuth.getInstance().uid
        val clickedUserId = user.uid
        if (currentUid == null) return  // safety check

        // --- Profile image and Texts ---
        Glide.with(context)
            .load(user.profilePhoto)
            .placeholder(R.drawable.profile)
            .into(holder.binding.imageProfile)
        holder.binding.name.text = user.name
        holder.binding.profession.text = user.profession


        holder.binding.btnFollow.setOnClickListener {
            val follow = FollowModel(
                followedBy = currentUid,
                followedAt = Date().time
            )
            FirebaseDatabase.getInstance().getReference("users")
                .child(clickedUserId)
                .child("followers")
                .child(currentUid)
                .setValue(follow).addOnSuccessListener {
                    FirebaseDatabase.getInstance().getReference("users")
                        .child(user.uid)
                        .child("followerCount")
                        .setValue(user.followerCount +1 ).addOnSuccessListener {
                            Toast.makeText(context, "You Followed ${user.name}", Toast.LENGTH_SHORT).show()
                        }
                }
        }
    }

    override fun getItemCount(): Int = userList.size

    fun updateList(newList: List<User>) {
        userList.clear()
        userList.addAll(newList)
        notifyDataSetChanged()
    }
}
