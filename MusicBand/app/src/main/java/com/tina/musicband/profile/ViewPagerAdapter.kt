package com.tina.musicband.profile

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter

const val TAB_NUMBER = 2

class ViewPagerAdapter (fragmentManager: FragmentManager) : FragmentPagerAdapter(fragmentManager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    override fun getItem(position: Int): Fragment {
        when(position){
            0 -> return ProfileMusicFragment()
            else -> return ProfileEventFragment()

        }
    }


    override fun getCount(): Int {
        return TAB_NUMBER
    }


}