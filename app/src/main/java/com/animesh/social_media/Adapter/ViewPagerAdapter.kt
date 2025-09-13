package com.animesh.social_media.Adapter

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.animesh.social_media.Fragments.Notification2Fragment
import com.animesh.social_media.Fragments.NotificationFragment
import com.animesh.social_media.Fragments.requestFragment

class ViewPagerAdapter(
    fragment: Fragment
) : FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int = 2 // Number of tabs/fragments

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> Notification2Fragment()
            1 -> requestFragment()
            else -> NotificationFragment() // Fallback
        }
    }



}
