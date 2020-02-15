package com.tina.musicband.others

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.tina.musicband.profile.ProfileEventFragment
import com.tina.musicband.profile.ProfileMusicFragment

const val TAB_NUMBER = 3

class ProfileOthersViewPager (fragmentManager: FragmentManager) : FragmentPagerAdapter(fragmentManager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    override fun getItem(position: Int): Fragment {
        when(position){
            0 -> return ProfileOthersInfoFragment()
            1-> return ProfileMusicFragment()
            else -> return ProfileEventFragment()

        }
    }


    override fun getCount(): Int {
        return TAB_NUMBER
    }


}