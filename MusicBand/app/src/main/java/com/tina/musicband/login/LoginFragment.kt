package com.tina.musicband.login

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.facebook.login.LoginManager
import com.google.firebase.auth.FirebaseAuth
import com.tina.musicband.MainActivity
import com.tina.musicband.MusicBandApplication
import com.tina.musicband.R
import com.tina.musicband.databinding.FragmentLoginBinding
import com.tina.musicband.databinding.FragmentSearchMusicBinding
import com.tina.musicband.ext.getVmFactory
import com.tina.musicband.network.LoadApiStatus

class LoginFragment : Fragment() {

    lateinit var binding: FragmentLoginBinding
    private val viewModel by viewModels<LoginViewModel> { getVmFactory() }
    private var auth = FirebaseAuth.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_login, container, false
        )

        binding.viewModel = viewModel

        binding.lifecycleOwner = this


        val fadeInAnimation = AnimationUtils.loadAnimation(activity, R.anim.fade_in_login)

        binding.loginLogo.startAnimation(fadeInAnimation)

        if (auth.currentUser == null) {
            binding.googleLoginButton.visibility = View.VISIBLE
            binding.facebookLoginButton.visibility = View.VISIBLE
            binding.facebookLoginButton.startAnimation(fadeInAnimation)
            binding.googleLoginButton.startAnimation(fadeInAnimation)
            binding.enterButton.visibility = View.INVISIBLE

            binding.facebookLoginButton.setOnClickListener {
                viewModel.fbLogin(MusicBandApplication.instance.user)
            }
        } else {
            binding.googleLoginButton.visibility = View.INVISIBLE
            binding.facebookLoginButton.visibility = View.INVISIBLE
            binding.enterButton.visibility = View.VISIBLE
            binding.enterButton.startAnimation(fadeInAnimation)

            binding.enterButton.setOnClickListener {
                findNavController().navigate(R.id.action_global_mainFragment)
            }
        }

        viewModel.status.observe(this, Observer {
            if(it == LoadApiStatus.LOADING){
                binding.loginProgressBar.visibility = View.VISIBLE
                binding.facebookLoginButton.visibility = View.INVISIBLE
            }
        })


        viewModel.didFinishLogin.observe(viewLifecycleOwner, Observer {
            it?.let {
                findNavController().navigate(R.id.action_global_avatarSelectFragment)
                viewModel.doneNavigateToAvatarPage()
            }
        })

        viewModel.loginFacebook.observe(this, Observer {
            it?.let {
                LoginManager.getInstance().logInWithReadPermissions(this, listOf("email"))
                viewModel.onLoginFbCompleted()
            }
        })

        binding.googleLoginButton.setOnClickListener {
            Toast.makeText(activity, "Coming Soon", Toast.LENGTH_SHORT).show()
        }

        return binding.root
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        viewModel.fbCallbackManager.onActivityResult(requestCode, resultCode, data)
    }
}