package com.tina.musicband.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDialogFragment
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.google.firebase.firestore.FirebaseFirestore
import com.tina.musicband.R
import com.tina.musicband.avatar.AvatarSelectViewModel
import com.tina.musicband.data.User
import com.tina.musicband.databinding.DialogAvatarSelectProfileBinding
import com.tina.musicband.ext.getVmFactory
import com.tina.musicband.login.UserManager

class AvatarDialogFragment : DialogFragment(){

    private lateinit var binding: DialogAvatarSelectProfileBinding
    val viewModel by viewModels<AvatarDialogViewModel> { getVmFactory() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NO_FRAME, R.style.AvatarDialog)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DialogAvatarSelectProfileBinding.inflate(inflater, container, false)

        binding.viewModel = viewModel

        binding.closeButton.setOnClickListener {
            dismiss()
        }

        viewModel.setUser(User())

        viewModel.setting.observe(viewLifecycleOwner, Observer {
            it?.let {
                dismiss()
                viewModel.finishSetting()
            }
        })

        return binding.root
    }



}