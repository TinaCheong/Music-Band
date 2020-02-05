package com.tina.musicband

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.animation.AnimationUtils
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import com.tina.musicband.databinding.ActivityMainBinding
import com.tina.musicband.login.LoginFragment

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding

    var isOpen = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        binding.lifecycleOwner = this

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

    }
}