package com.tina.musicband.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.tina.musicband.R
import com.tina.musicband.data.User
import com.tina.musicband.databinding.DialogBgSelectProfileBinding
import com.tina.musicband.ext.getVmFactory

class BackgroundDialogFragment : DialogFragment() {

    private lateinit var binding: DialogBgSelectProfileBinding
    val viewModel by viewModels<BackgroundDialogViewModel> { getVmFactory() }

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

        viewModel.setting.observe(viewLifecycleOwner, Observer {
            it?.let {
                dismiss()
                viewModel.finishSetting()
            }
        })

        return binding.root
    }


}