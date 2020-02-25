package com.tina.musicband.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProviders
import com.tina.musicband.R
import com.tina.musicband.data.User
import com.tina.musicband.databinding.DialogAvatarSelectProfileBinding
import com.tina.musicband.databinding.DialogBgSelectProfileBinding

class BackgroundDialog : DialogFragment() {

    private lateinit var binding: DialogBgSelectProfileBinding
    private val viewModel: BackgroundDialogViewModel by lazy{
        ViewModelProviders.of(this).get(BackgroundDialogViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NO_FRAME, R.style.BackgroundDialog)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DialogBgSelectProfileBinding.inflate(inflater, container, false)

        binding.viewModel = viewModel

        viewModel.setUser(User())

        binding.closeButton.setOnClickListener {
            dismiss()
        }

        binding.saveButton.setOnClickListener {
            viewModel.save()
            dismiss()
        }

        return binding.root
    }


}