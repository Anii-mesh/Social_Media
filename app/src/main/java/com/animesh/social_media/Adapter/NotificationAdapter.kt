package com.animesh.social_media.Adapter

import android.text.Html
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.animesh.social_media.Model.NotificationModel
import com.animesh.social_media.databinding.Notification2SampleBinding


class NotificationAdapter(
    private val notificationList: List<NotificationModel>
) : RecyclerView.Adapter<NotificationAdapter.NotificationViewHolder>() {

    inner class NotificationViewHolder(val binding: Notification2SampleBinding)
        : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotificationViewHolder {
        val binding = Notification2SampleBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return NotificationViewHolder(binding)
    }

    override fun onBindViewHolder(holder: NotificationViewHolder, position: Int) {
        val item = notificationList[position]
        holder.binding.apply {
            imageProfile.setImageResource(item.profile)
            notification.text = Html.fromHtml(item.notification, Html.FROM_HTML_MODE_COMPACT)
            time.text = item.time
        }
    }

    override fun getItemCount(): Int = notificationList.size
}