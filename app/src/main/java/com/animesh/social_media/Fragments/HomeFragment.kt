package com.animesh.social_media.Fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.animesh.social_media.Adapter.DashboardAdapter
import com.animesh.social_media.Adapter.StoryAdapter
import com.animesh.social_media.Model.StoryModel
import com.animesh.social_media.Model.dashboardModel
import com.animesh.social_media.R


class HomeFragment : Fragment() {

    lateinit var storyRv: RecyclerView
    lateinit var storyList: ArrayList<StoryModel>

    lateinit var dashboardRv: RecyclerView
    lateinit var dashboardList: ArrayList<dashboardModel>

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
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        storyRv = view.findViewById(R.id.storyRV)
        storyList = ArrayList()
        storyList.add(StoryModel(R.drawable.p1, R.drawable.profile, R.drawable.ic_live, "Animesh"))
        storyList.add(StoryModel(R.drawable.p2, R.drawable.profile, R.drawable.ic_live, "Animesh"))
        storyList.add(StoryModel(R.drawable.p3, R.drawable.profile, R.drawable.ic_live, "Animesh"))
        storyList.add(StoryModel(R.drawable.p1, R.drawable.profile, R.drawable.ic_live, "Animesh"))
        storyList.add(StoryModel(R.drawable.p2, R.drawable.profile, R.drawable.ic_live, "Animesh"))

        storyRv.adapter = StoryAdapter(storyList, storyRv.context)
        val linearLayoutManager = LinearLayoutManager(
            storyRv.context,
            LinearLayoutManager.HORIZONTAL, false
        )
        storyRv.layoutManager = linearLayoutManager
        storyRv.isNestedScrollingEnabled = false

        dashboardRv = view.findViewById(R.id.dashboardRv)
        dashboardList = ArrayList()
        dashboardList.add(dashboardModel(R.drawable.profile, R.drawable.p1, R.drawable.ic_bookmark, "Animesh", "About Animesh", "1k", "5","20"))
        dashboardList.add(dashboardModel(R.drawable.profile, R.drawable.p2, R.drawable.ic_bookmark, "Rohit", "About Rohit", "2.3k","5","40"))
        dashboardList.add(dashboardModel(R.drawable.profile, R.drawable.p3, R.drawable.ic_bookmark, "Shivam", "About Shivam", "500","4","15"))

        val dashboardLayoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        dashboardRv.layoutManager = dashboardLayoutManager
        dashboardRv.adapter = DashboardAdapter(dashboardList, requireContext())
        dashboardRv.isNestedScrollingEnabled = false

        return (view)
    }



}