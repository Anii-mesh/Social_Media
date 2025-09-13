package com.animesh.social_media.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.animesh.social_media.Model.StoryModel
import com.animesh.social_media.databinding.StoryRvDesignBinding

class StoryAdapter(private val storyList: List<StoryModel>, context: Context) :
    RecyclerView.Adapter<StoryAdapter.StoryViewHolder>() {

    inner class StoryViewHolder(val binding: StoryRvDesignBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StoryViewHolder {
        val binding = StoryRvDesignBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return StoryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: StoryViewHolder, position: Int) {
        val item = storyList[position]

        with(holder.binding) {
            story.setImageResource(item.story)
            imageProfile.setImageResource(item.profileImage)
            name.text = item.name
            storyType.setImageResource(item.storyType)
        }
    }

    override fun getItemCount(): Int = storyList.size
}