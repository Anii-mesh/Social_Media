package com.animesh.social_media.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.animesh.social_media.Model.Post
import com.animesh.social_media.R
import com.animesh.social_media.databinding.DashboradRvSampleBinding
import com.bumptech.glide.Glide


class PostAdapter(
    private val dashboardList: List<Post>,
    requireContext: Context
) : RecyclerView.Adapter<PostAdapter.DashboardViewHolder>() {

    inner class DashboardViewHolder(val binding: DashboradRvSampleBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DashboardViewHolder {
        val binding = DashboradRvSampleBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return DashboardViewHolder(binding)
    }

    override fun onBindViewHolder(holder: DashboardViewHolder, position: Int) {
        val post = dashboardList[position]
        Glide.with(holder.itemView.context)
            .load(post.postImage)
            .placeholder(R.drawable.placeholder)
            .into(holder.binding.postImg)

    }

    override fun getItemCount(): Int = dashboardList.size
}