package com.tina.musicband.profile


import android.os.Bundle
import android.os.UserManager
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.google.firebase.firestore.FirebaseFirestore
import com.tina.musicband.R
import com.tina.musicband.avatar.getAvatarDrawable
import com.tina.musicband.data.User
import com.tina.musicband.databinding.FragmentProfileEditBinding
import com.tina.musicband.dialog.getBackgroundDrawable
import com.tina.musicband.ext.getVmFactory
import com.tina.musicband.main.MainViewModel

/**
 * A simple [Fragment] subclass.
 */
class ProfileEditFragment : Fragment() {

    lateinit var binding: FragmentProfileEditBinding

    val viewModel by viewModels<ProfileViewModel> { getVmFactory() }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_profile_edit, container, false
        )

        binding.viewModel = viewModel

        binding.lifecycleOwner = this

        binding.saveButton.setOnClickListener {

            viewModel.updateUserData()
        }

        viewModel.uploadStatus.observe(viewLifecycleOwner, Observer {
            it?.let {
                Toast.makeText(activity, "Save Success", Toast.LENGTH_SHORT).show()
                findNavController().navigate(R.id.action_global_profileFragment)
                viewModel.finishUpdate()
            }

        })

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
