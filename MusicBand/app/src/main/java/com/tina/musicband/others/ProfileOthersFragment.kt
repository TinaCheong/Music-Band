package com.tina.musicband.others


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.viewpager.widget.PagerAdapter
import com.google.android.material.tabs.TabLayout
import com.google.firebase.firestore.FirebaseFirestore
import com.tina.musicband.R
import com.tina.musicband.avatar.getAvatarDrawable
import com.tina.musicband.data.Follower
import com.tina.musicband.data.Following
import com.tina.musicband.data.Songs
import com.tina.musicband.data.User
import com.tina.musicband.databinding.FragmentProfileOthersBinding
import com.tina.musicband.dialog.getBackgroundDrawable
import com.tina.musicband.ext.getVmFactory
import com.tina.musicband.login.UserManager

/**
 * A simple [Fragment] subclass.
 */
class ProfileOthersFragment : Fragment() {

    private lateinit var binding: FragmentProfileOthersBinding
    private lateinit var adapter: PagerAdapter

    private val viewModel by viewModels<ProfileOthersViewModel> { getVmFactory(userID) }

    private val userID by lazy { requireArguments().getString("userID")!! }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_profile_others, container, false
        )

        binding.lifecycleOwner = this

        binding.viewModel = viewModel

        viewModel.setUserID(userID)

        changePages()

        setTabLayoutIcons()

        return binding.root
    }


    private fun changePages() {

        adapter = ProfileOthersViewPager(childFragmentManager, userID)
        binding.viewpagerProfileOthers.adapter = adapter
        binding.viewpagerProfileOthers.addOnPageChangeListener(
            TabLayout.TabLayoutOnPageChangeListener(
                binding.tabLayoutProfileOthers
            )
        )

    }

    private fun setTabLayoutIcons() {

        binding.tabLayoutProfileOthers.setupWithViewPager(binding.viewpagerProfileOthers)
        binding.tabLayoutProfileOthers.getTabAt(0)?.setIcon(R.drawable.ic_event_title)
        binding.tabLayoutProfileOthers.getTabAt(1)?.setIcon(R.drawable.ic_calender)

    }


}



