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

        binding.usernameEdit.setText(com.tina.musicband.login.UserManager.userName)

        readProfileData()

        binding.saveButton.setOnClickListener {

            val updateData = mapOf(
                "username" to binding.usernameEdit.text.toString(),
                "education" to binding.educationText.text.toString(),
                "favouriteMusic" to binding.favouriteMusicText.text.toString(),
                "introduction" to binding.introductionText.text.toString(),
                "position" to binding.positionText.text.toString(),
                "speciality" to binding.specialityText.text.toString(),
                "experience" to binding.experienceEdit.text.toString()
            )


            FirebaseFirestore.getInstance().collection("users")
                .document(com.tina.musicband.login.UserManager.userToken.toString())
                .update(updateData)
                .addOnCompleteListener {

                    if (it.isSuccessful) {

                        Toast.makeText(activity, "Save Success", Toast.LENGTH_SHORT).show()
                        findNavController().navigate(R.id.action_global_profileFragment)

                    }

                }

        }

        updateUser()



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

    private fun updateUser() {
        FirebaseFirestore.getInstance().collection("users")
            .document(com.tina.musicband.login.UserManager.userToken.toString())
            .addSnapshotListener { documentSnapshot, firebaseFirestoreException ->

                if(documentSnapshot != null){

                    val user = documentSnapshot.toObject(com.tina.musicband.data.User::class.java)

                    binding.profileEditBackground.setImageDrawable(
                        user?.background?.getBackgroundDrawable()
                    )

                    binding.profileEditAvatar.setImageDrawable(
                        user?.avatar?.getAvatarDrawable()
                    )


                }

            }

    }

    private fun readProfileData(){

        FirebaseFirestore.getInstance().collection("users")
            .document(com.tina.musicband.login.UserManager.userToken.toString())
            .get()
            .addOnCompleteListener {
                if(it.isSuccessful){

                    val user = it.result!!.toObject(User::class.java)

                    user?.let {
                        binding.educationText.setText(it.education)
                        binding.favouriteMusicText.setText(it.favouriteMusic)
                        binding.introductionText.setText(it.introduction)
                        binding.positionText.setText(it.position)
                        binding.specialityText.setText(it.speciality)
                        binding.experienceEdit.setText(it.experience)

                    }

                }
            }

    }
}
