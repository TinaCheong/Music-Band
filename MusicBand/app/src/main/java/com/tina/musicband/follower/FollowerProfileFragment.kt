package com.tina.musicband.follower


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.google.firebase.firestore.FirebaseFirestore
import com.tina.musicband.R
import com.tina.musicband.data.Follower
import com.tina.musicband.data.Following
import com.tina.musicband.databinding.FragmentFollowerProfileBinding
import com.tina.musicband.databinding.FragmentMainBinding
import com.tina.musicband.following.FollowingAdapter
import com.tina.musicband.login.UserManager

/**
 * A simple [Fragment] subclass.
 */
class FollowerProfileFragment : Fragment() {

    lateinit var binding: FragmentFollowerProfileBinding
    var followerList = mutableListOf<Follower>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_follower_profile, container, false
        )

        binding.recyclerViewFollowerPage.adapter = FollowerAdapter()

        FirebaseFirestore.getInstance().collection("users")
            .document(UserManager.userToken.toString())
            .collection("follower")
            .addSnapshotListener { querySnapshot, firebaseFirestoreException ->

                if (querySnapshot != null) {

                    followerList = querySnapshot.toObjects(Follower::class.java)

                    showHint()
                    (binding.recyclerViewFollowerPage.adapter as FollowerAdapter).submitList(
                        followerList
                    )

                }
            }


        // Inflate the layout for this fragment
        return binding.root
    }

    private fun showHint() {
        if (followerList.size == 0) {
            binding.noFollowerIcon.visibility = View.VISIBLE
            binding.noFollowerText.visibility = View.VISIBLE
            binding.questionMarkIcon.visibility = View.VISIBLE
        } else {
            binding.noFollowerIcon.visibility = View.GONE
            binding.noFollowerText.visibility = View.GONE
            binding.questionMarkIcon.visibility = View.GONE
        }


    }
}
