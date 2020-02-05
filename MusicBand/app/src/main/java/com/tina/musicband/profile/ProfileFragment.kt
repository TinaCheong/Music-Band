package com.tina.musicband.profile


import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import androidx.viewpager.widget.PagerAdapter
import com.google.android.material.tabs.TabLayout
import com.tina.musicband.MainActivity
import com.tina.musicband.R
import com.tina.musicband.databinding.FragmentProfileBinding

/**
 * A simple [Fragment] subclass.
 */
class ProfileFragment : Fragment() {

    private lateinit var binding: FragmentProfileBinding
    private lateinit var adapter: PagerAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_profile, container, false
        )

        binding.lifecycleOwner = this


        (activity as MainActivity).binding.toolbarLogoSearch.visibility = View.GONE
        (activity as MainActivity).binding.toolbarLogo.visibility = View.GONE
        (activity as MainActivity).binding.toolbarLogoProfile.visibility = View.VISIBLE

        changePages()
        setTabLayoutIcons()

        binding.profileEditIcon.setOnClickListener {
            findNavController().navigate(R.id.action_global_profileEditFragment)
        }


        // Inflate the layout for this fragment
        return binding.root
    }

    private fun changePages(){

        adapter = ViewPagerAdapter(childFragmentManager)
        binding.viewpagerProfile.adapter = adapter
        binding.viewpagerProfile.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(binding.tabLayoutProfile))

    }

    private fun setTabLayoutIcons(){

        binding.tabLayoutProfile.setupWithViewPager(binding.viewpagerProfile)
        binding.tabLayoutProfile.getTabAt(0)?.setIcon(R.drawable.ic_event_title)
        binding.tabLayoutProfile.getTabAt(1)?.setIcon(R.drawable.ic_calender)

    }


}
