package com.animesh.social_media.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.animesh.social_media.Model.dashboardModel
import com.animesh.social_media.databinding.DashboradRvSampleBinding


class DashboardAdapter(
    private val dashboardList: List<dashboardModel>,
    requireContext: Context
) : RecyclerView.Adapter<DashboardAdapter.DashboardViewHolder>() {

    inner class DashboardViewHolder(val binding: DashboradRvSampleBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DashboardViewHolder {
        val binding = DashboradRvSampleBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return DashboardViewHolder(binding)
    }

    override fun onBindViewHolder(holder: DashboardViewHolder, position: Int) {
        val currentItem = dashboardList[position]

        holder.binding.imageProfile.setImageResource(currentItem.profile)
        holder.binding.postImg.setImageResource(currentItem.postImage)
        holder.binding.save.setImageResource(currentItem.save)
        holder.binding.name1.text = currentItem.name1
        holder.binding.about.text = currentItem.about
        holder.binding.like.text = currentItem.like
        holder.binding.comment.text = currentItem.comment
        holder.binding.share.text = currentItem.share
    }

    override fun getItemCount(): Int = dashboardList.size
}