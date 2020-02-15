package com.tina.musicband.avatar


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.tina.musicband.MainActivity
import com.tina.musicband.R
import com.tina.musicband.databinding.FragmentAvatarSelectBinding

/**
 * A simple [Fragment] subclass.
 */
class AvatarSelectFragment : Fragment() {

    lateinit var binding: FragmentAvatarSelectBinding
    private val viewModel: AvatarSelectViewModel by lazy{
        ViewModelProviders.of(this).get(AvatarSelectViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_avatar_select, container, false
        )

        binding.saveButton.setOnClickListener {
            findNavController().navigate(R.id.action_global_mainFragment)
        }

        binding.viewModel = viewModel


        // Inflate the layout for this fragment
        return binding.root
    }


}
