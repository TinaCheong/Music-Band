package com.tina.musicband

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.animation.AnimationUtils
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.findNavController
import com.tina.musicband.databinding.ActivityMainBinding
import com.tina.musicband.ext.getVmFactory
import com.tina.musicband.login.LoginFragment
import com.tina.musicband.search.SearchMusicFragment
import com.tina.musicband.search.SearchMusicViewModel
import com.tina.musicband.util.CurrentFragmentType

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding

    val viewModel by viewModels<MusicBandViewModel> { getVmFactory() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        binding.lifecycleOwner = this

        binding.viewModel = viewModel

        //Bottom Navigation Settings

        binding.bottomNavigation.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.navigation_main -> {
                    findNavController(R.id.myNavHostFragment).navigate(R.id.action_global_mainFragment)
                    true
                }
                R.id.navigation_music_search -> {
                    findNavController(R.id.myNavHostFragment).navigate(R.id.action_global_searchMusicFragment)
                    true
                }
                R.id.navigation_quick_match -> {
                    findNavController(R.id.myNavHostFragment).navigate(R.id.action_global_quickMatchFragment)
                    true
                }
                R.id.navigation_profile -> {
                    findNavController(R.id.myNavHostFragment).navigate(R.id.action_global_profileFragment)
                    true
                }
                else -> false

            }
        }

        setupNavController()

        binding.backButton.setOnClickListener {
            findNavController(R.id.myNavHostFragment).navigateUp()
        }

    }
    private fun setupNavController() {
        findNavController(R.id.myNavHostFragment).addOnDestinationChangedListener { navController: NavController, _: NavDestination, _: Bundle? ->
            viewModel.currentFragmentType.value = when (navController.currentDestination?.id) {
                R.id.loginFragment -> CurrentFragmentType.LOGIN
                R.id.mainFragment -> CurrentFragmentType.MAIN
                R.id.searchMusicFragment -> CurrentFragmentType.SEARCH
                R.id.quickMatchFragment -> CurrentFragmentType.MATCH
                R.id.profileFragment -> CurrentFragmentType.PROFILE
                R.id.avatarSelectFragment -> CurrentFragmentType.SELECT_AVATAR
                R.id.addEventFragment -> CurrentFragmentType.ADD_EVENT
                R.id.addMusicFragment -> CurrentFragmentType.ADD_MUSIC
                R.id.profileOthersFragment -> CurrentFragmentType.OTHERS_PROFILE
                R.id.followerProfileFragment -> CurrentFragmentType.FOLLOWER
                R.id.followingProfileFragment -> CurrentFragmentType.FOLLOWING

                else -> viewModel.currentFragmentType.value
            }
        }
    }
}