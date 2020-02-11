package com.tina.musicband.profile


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import com.tina.musicband.R
import com.tina.musicband.databinding.FragmentProfileEditBinding

/**
 * A simple [Fragment] subclass.
 */
class ProfileEditFragment : Fragment() {

    lateinit var binding: FragmentProfileEditBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_profile_edit, container, false
        )

        binding.backButton.setOnClickListener {
            findNavController().navigate(R.id.action_global_profileFragment)
        }

        binding.avatarEdit.setOnClickListener {
            findNavController().navigate(R.id.action_global_avatarDialog)
        }

        binding.addBackground.setOnClickListener {
            findNavController().navigate(R.id.action_global_backgroundDialog)
        }

        // Inflate the layout for this fragment
        return binding.root
    }


}
