package com.tina.musicband.others


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.viewpager.widget.PagerAdapter
import com.google.android.material.tabs.TabLayout
import com.tina.musicband.MainActivity
import com.tina.musicband.R
import com.tina.musicband.databinding.FragmentProfileBinding
import com.tina.musicband.databinding.FragmentProfileOthersBinding
import com.tina.musicband.profile.ViewPagerAdapter

/**
 * A simple [Fragment] subclass.
 */
class ProfileOthersFragment : Fragment() {

    private lateinit var binding: FragmentProfileOthersBinding
    private lateinit var adapter: PagerAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_profile_others, container, false
        )

        binding.lifecycleOwner = this

        changePages()

        setTabLayoutIcons()


        return binding.root
    }

    private fun changePages(){

        adapter = ProfileOthersViewPager(childFragmentManager)
        binding.viewpagerProfileOthers.adapter = adapter
        binding.viewpagerProfileOthers.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(binding.tabLayoutProfileOthers))

    }

    private fun setTabLayoutIcons(){

        binding.tabLayoutProfileOthers.setupWithViewPager(binding.viewpagerProfileOthers)
        binding.tabLayoutProfileOthers.getTabAt(0)?.setIcon(R.drawable.ic_others_info)
        binding.tabLayoutProfileOthers.getTabAt(1)?.setIcon(R.drawable.ic_event_title)
        binding.tabLayoutProfileOthers.getTabAt(2)?.setIcon(R.drawable.ic_calender)

    }


}
