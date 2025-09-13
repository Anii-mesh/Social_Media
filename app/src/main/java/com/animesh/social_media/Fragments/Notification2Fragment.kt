package com.animesh.social_media.Fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.animesh.social_media.Adapter.NotificationAdapter
import com.animesh.social_media.Model.NotificationModel
import com.animesh.social_media.R


class Notification2Fragment : Fragment() {

    lateinit var notification2Rv : RecyclerView
    lateinit var notificationList : ArrayList<NotificationModel>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view =  inflater.inflate(R.layout.fragment_notification2, container, false)

        notification2Rv = view.findViewById(R.id.notification2Rv)
        notificationList = ArrayList()
        notificationList.add(NotificationModel(R.drawable.profile,"<b>Prakhar Singh</b> mention you in a comment bolo hii",
            "Just Now"))
        notificationList.add(NotificationModel(R.drawable.profile,"<b>Prakhar Singh</b> mention you in a comment bolo hii",
            "Just Now"))
        notificationList.add(NotificationModel(R.drawable.profile,"<b>Prakhar Singh</b> mention you in a comment bolo hii",
            "Just Now"))
        notificationList.add(NotificationModel(R.drawable.profile,"<b>Prakhar Singh</b> mention you in a comment bolo hii",
            "Just Now"))
        notificationList.add(NotificationModel(R.drawable.profile,"<b>Prakhar Singh</b> mention you in a comment bolo hii",
            "Just Now"))
        notificationList.add(NotificationModel(R.drawable.profile,"<b>Prakhar Singh</b> mention you in a comment bolo hii",
            "Just Now"))
        notificationList.add(NotificationModel(R.drawable.profile,"<b>Prakhar Singh</b> mention you in a comment bolo hii",
            "Just Now"))
        notificationList.add(NotificationModel(R.drawable.profile,"<b>Prakhar Singh</b> mention you in a comment bolo hii",
            "Just Now"))


        val notificationLayoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL,false)
        notification2Rv.layoutManager = notificationLayoutManager
        notification2Rv.adapter = NotificationAdapter(notificationList)
        notification2Rv.isNestedScrollingEnabled = false

        return (view)
    }



}