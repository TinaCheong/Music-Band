package com.tina.musicband.avatar


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.tina.musicband.R
import com.tina.musicband.data.User
import com.tina.musicband.databinding.FragmentAvatarSelectBinding
import com.tina.musicband.ext.getVmFactory
import com.tina.musicband.login.UserManager

/**
 * A simple [Fragment] subclass.
 */
class AvatarSelectFragment : Fragment() {

    lateinit var binding: FragmentAvatarSelectBinding
    val viewModel by viewModels<AvatarSelectViewModel> { getVmFactory() }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_avatar_select, container, false
        )

        binding.lifecycleOwner = this

        binding.viewModel = viewModel

        viewModel.setUser(User())

        binding.enterNameText.setText(UserManager.userName)

        viewModel.setting.observe(viewLifecycleOwner, Observer {
            it?.let {
                findNavController().navigate(R.id.action_global_mainFragment)
                viewModel.finishSetting()
            }
        })


        // Inflate the layout for this fragment
        return binding.root
    }


}
