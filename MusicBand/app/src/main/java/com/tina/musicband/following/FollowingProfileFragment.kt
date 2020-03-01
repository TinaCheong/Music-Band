package com.tina.musicband.following


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.google.firebase.firestore.FirebaseFirestore
import com.tina.musicband.R
import com.tina.musicband.data.Following
import com.tina.musicband.databinding.FragmentFollowerProfileBinding
import com.tina.musicband.databinding.FragmentFollowingProfileBinding
import com.tina.musicband.login.UserManager

/**
 * A simple [Fragment] subclass.
 */
class FollowingProfileFragment : Fragment() {

    lateinit var binding: FragmentFollowingProfileBinding
    var followingList = mutableListOf<Following>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_following_profile, container, false
        )

        binding.recyclerViewFollowingPage.adapter = FollowingAdapter()



        FirebaseFirestore.getInstance().collection("users")
            .document(UserManager.userToken.toString())
            .collection("following")
            .addSnapshotListener { querySnapshot, firebaseFirestoreException ->

                if (querySnapshot != null) {

                    followingList = querySnapshot.toObjects(Following::class.java)

                    showHint()
                    (binding.recyclerViewFollowingPage.adapter as FollowingAdapter).submitList(followingList)

                }
            }

        // Inflate the layout for this fragment
        return binding.root
    }

    private fun showHint(){
        if(followingList.size == 0) {
            binding.noFollowingIcon.visibility = View.VISIBLE
            binding.noFollowingText.visibility = View.VISIBLE
            binding.questionMarkIcon.visibility = View.VISIBLE
        } else {
            binding.noFollowingIcon.visibility = View.GONE
            binding.noFollowingText.visibility = View.GONE
            binding.questionMarkIcon.visibility = View.GONE
        }

    }

}
