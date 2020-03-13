package com.tina.musicband.profile


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.viewpager.widget.PagerAdapter
import com.google.android.material.tabs.TabLayout
import com.google.firebase.firestore.FirebaseFirestore
import com.tina.musicband.R
import com.tina.musicband.avatar.getAvatarDrawable
import com.tina.musicband.databinding.FragmentProfileBinding
import com.tina.musicband.dialog.getBackgroundDrawable
import com.tina.musicband.ext.getVmFactory
import com.tina.musicband.login.UserManager

/**
 * A simple [Fragment] subclass.
 */
class ProfileFragment : Fragment() {

    private lateinit var binding: FragmentProfileBinding
    private lateinit var adapter: PagerAdapter
    val viewModel by viewModels<ProfileViewModel> { getVmFactory() }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_profile, container, false
        )

        binding.lifecycleOwner = this

        binding.viewModel = viewModel

        changePages()
        setTabLayoutIcons()
        viewModel.retrievePostsCountResult()
        viewModel.retrieveFollowingsCountResult()
        viewModel.retrieveFollowersCountResult()

        binding.profileEditIcon.setOnClickListener {
            findNavController().navigate(R.id.action_global_profileEditFragment)
        }

        binding.followersCount.setOnClickListener {
            findNavController().navigate(R.id.action_global_followerProfileFragment)
        }

        binding.followingCount.setOnClickListener {
            findNavController().navigate(R.id.action_global_followingProfileFragment)
        }

        return binding.root
    }

    private fun changePages() {

        adapter = ViewPagerAdapter(childFragmentManager)
        binding.viewpagerProfile.adapter = adapter
        binding.viewpagerProfile.addOnPageChangeListener(
            TabLayout.TabLayoutOnPageChangeListener(
                binding.tabLayoutProfile
            )
        )
    }

    private fun setTabLayoutIcons() {

        binding.tabLayoutProfile.setupWithViewPager(binding.viewpagerProfile)
        binding.tabLayoutProfile.getTabAt(0)?.setIcon(R.drawable.ic_event_title)
        binding.tabLayoutProfile.getTabAt(1)?.setIcon(R.drawable.ic_calender)

    }
}
