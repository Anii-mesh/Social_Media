package com.animesh.social_media.Fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager2.widget.ViewPager2
import com.animesh.social_media.Adapter.ViewPagerAdapter
import com.animesh.social_media.databinding.FragmentNotificationBinding
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class NotificationFragment : Fragment() {

    lateinit var _binding: FragmentNotificationBinding
    private val binding get() = _binding!!


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
        _binding = FragmentNotificationBinding.inflate(inflater, container, false)


        val adapter = ViewPagerAdapter(this) // 'this' = current Fragment
        binding.viewPager.adapter = adapter

        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            tab.text = when (position) {
                0 -> "NOTIFICATIONS"     // Tab title 1
                1 -> "REQUESTS"    // Tab title 2
                else -> throw IllegalStateException("Invalid position $position")
            }
        }.attach()

        return binding.root
    }

//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//
//        val adapter = ViewPagerAdapter(this) // 'this' = current Fragment
//        binding.viewPager.adapter = adapter
//
//        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
//            tab.text = when (position) {
//                0 -> "NOTIFICATIONS"     // Tab title 1
//                1 -> "REQUESTS"    // Tab title 2
//                else -> throw IllegalStateException("Invalid position $position")
//            }
//        }.attach()
//    }

}