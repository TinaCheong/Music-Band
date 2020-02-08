package com.tina.musicband.login

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.tina.musicband.MainActivity
import com.tina.musicband.R
import com.tina.musicband.databinding.FragmentLoginBinding
import com.tina.musicband.databinding.FragmentSearchMusicBinding

class LoginFragment : Fragment() {

    lateinit var binding: FragmentLoginBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_login, container, false
        )

        binding.facebookLoginButton.setOnClickListener {
            findNavController().navigate(R.id.action_global_avatarSelectFragment)
        }

        (activity as MainActivity).binding.toolbar.visibility = View.GONE
        (activity as MainActivity).binding.bottomNavigation.visibility = View.GONE
        (activity as MainActivity).binding.toolbarLogoSearch.visibility = View.GONE
        (activity as MainActivity).binding.toolbarLogo.visibility = View.GONE

        val fadeInAnimation = AnimationUtils.loadAnimation(activity, R.anim.fade_in_login)

        binding.loginLogo.startAnimation(fadeInAnimation)
        binding.facebookLoginButton.startAnimation(fadeInAnimation)
        binding.googleLoginButton.startAnimation(fadeInAnimation)

        return binding.root
    }
}