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
    private var postCount: Int = 0
    private var followingCount: Int = 0
    private var followersCount: Int = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_profile, container, false
        )

        binding.lifecycleOwner = this

        changePages()
        setTabLayoutIcons()
        viewModel.getAvatarAndBackground()

        binding.profileEditIcon.setOnClickListener {
            findNavController().navigate(R.id.action_global_profileEditFragment)
        }


        viewModel.profileAvatar.observe(this, Observer {
            it?.let {
                binding.profileAvatar.setImageDrawable(
                    viewModel.profileAvatar.value?.getAvatarDrawable()
                )
            }

        })

        viewModel.profileBackground.observe(this, Observer {
            it.let {
                binding.profileBackground.setImageDrawable(
                    viewModel.profileBackground.value?.getBackgroundDrawable()
                )
            }
        })

        binding.profileUsername.setText(UserManager.userName)


        getPostCount()
        getFollowerCount()
        getFollowingCount()

        binding.followersCount.setOnClickListener {
            findNavController().navigate(R.id.action_global_followerProfileFragment)
        }

        binding.followingCount.setOnClickListener {
            findNavController().navigate(R.id.action_global_followingProfileFragment)
        }


        // Inflate the layout for this fragment
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

    private fun getPostCount(){

        FirebaseFirestore.getInstance().collection("posts")
            .whereEqualTo("userId", UserManager.userToken.toString())
            .get()
            .addOnCompleteListener {
                if(it.isSuccessful){
                    for(document in it.result!!){
                        postCount++
                    }

                    binding.postsCount.text = postCount.toString()
                    postCount = 0
                }
            }

    }

    private fun getFollowingCount(){

        FirebaseFirestore.getInstance().collection("users")
            .document(UserManager.userToken.toString())
            .collection("following")
            .get()
            .addOnCompleteListener {
                if(it.isSuccessful){
                    for(document in it.result!!){
                        followingCount++
                    }

                    binding.followingCount.text = followingCount.toString()
                    followingCount = 0
                }
            }

    }

    private fun getFollowerCount(){

        FirebaseFirestore.getInstance().collection("users")
            .document(UserManager.userToken.toString())
            .collection("follower")
            .get()
            .addOnCompleteListener {
                if(it.isSuccessful){
                    for(document in it.result!!){
                        followersCount++

                    }

                    binding.followersCount.text = followersCount.toString()
                    followersCount = 0
                }
            }

    }


}
