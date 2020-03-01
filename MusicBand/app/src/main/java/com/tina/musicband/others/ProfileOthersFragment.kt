package com.tina.musicband.others


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.viewpager.widget.PagerAdapter
import com.google.android.material.tabs.TabLayout
import com.google.firebase.firestore.FirebaseFirestore
import com.tina.musicband.R
import com.tina.musicband.avatar.getAvatarDrawable
import com.tina.musicband.data.Follower
import com.tina.musicband.data.Following
import com.tina.musicband.data.Songs
import com.tina.musicband.data.User
import com.tina.musicband.databinding.FragmentProfileOthersBinding
import com.tina.musicband.dialog.getBackgroundDrawable
import com.tina.musicband.ext.getVmFactory
import com.tina.musicband.login.UserManager

/**
 * A simple [Fragment] subclass.
 */
class ProfileOthersFragment : Fragment() {

    private lateinit var binding: FragmentProfileOthersBinding
    private lateinit var adapter: PagerAdapter
    private var postCount: Int = 0
    private var followingCount: Int = 0
    private var followerCount: Int = 0
    private var selectedUser = User()

    private val viewModel by viewModels<ProfileOthersViewModel> {
        getVmFactory(
            userID
        )
    }

    private val userID by lazy {
        requireArguments().getString("userID")!!
//        ProfileOthersFragmentArgs.fromBundle(arguments!!).userID
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_profile_others, container, false
        )

        binding.lifecycleOwner = this


        changePages()

        setTabLayoutIcons()

        viewModel.userID.value?.apply {

            readUserDataFromSong(this)

            getPostCount(this)

            getFollowerCount(this)

            getFollowingCount(this)

            checkFollower(this)

        }

        binding.followButton.setOnClickListener {
            addFollower(selectedUser)
            Toast.makeText(activity, "Follow Success", Toast.LENGTH_SHORT).show()

        }

        binding.followClickedButton.setOnClickListener {
            unfollow(selectedUser)
            Toast.makeText(activity, "Unfollow User", Toast.LENGTH_SHORT).show()

        }





        return binding.root
    }


    private fun changePages() {

        adapter = ProfileOthersViewPager(childFragmentManager, userID)
        binding.viewpagerProfileOthers.adapter = adapter
        binding.viewpagerProfileOthers.addOnPageChangeListener(
            TabLayout.TabLayoutOnPageChangeListener(
                binding.tabLayoutProfileOthers
            )
        )

    }

    private fun setTabLayoutIcons() {

        binding.tabLayoutProfileOthers.setupWithViewPager(binding.viewpagerProfileOthers)
        binding.tabLayoutProfileOthers.getTabAt(0)?.setIcon(R.drawable.ic_event_title)
        binding.tabLayoutProfileOthers.getTabAt(1)?.setIcon(R.drawable.ic_calender)

    }

    fun readUserDataFromSong(userID: String) {

        FirebaseFirestore.getInstance().collection("users")
            .document(userID).get()
            .addOnCompleteListener {
                if (it.isSuccessful) {

                    val user = it.result!!.toObject(User::class.java)

                    user?.let {
                        binding.apply {

                            this.profileUsername.setText(it.username.toString())
                            this.specialityDetail.setText(it.speciality)
                            this.educationDetail.setText(it.education)
                            this.experienceDetail.setText(it.experience)
                            this.favouriteMusicDetail.setText(it.favouriteMusic)
                            this.positionDetail.setText(it.position)
                            this.introductionDetail.setText(it.introduction)
                            this.profileBackground.setImageDrawable(it.background.getBackgroundDrawable())
                            this.profileAvatar.setImageDrawable(it.avatar.getAvatarDrawable())

                        }

                        selectedUser = user
                    }

                }

            }
    }

    private fun getPostCount(userID: String) {

        FirebaseFirestore.getInstance().collection("posts")
            .whereEqualTo("userId", userID)
            .get()
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    for (document in it.result!!) {
                        postCount++
                    }

                    binding.postsCount.text = postCount.toString()
                }
            }
    }

    private fun getFollowingCount(userID : String) {


        FirebaseFirestore.getInstance().collection("users")
            .document(userID)
            .collection("following")
            .addSnapshotListener { documentSnapshot, firebaseFirestoreException ->

                if (documentSnapshot != null) {

                    val followings = documentSnapshot.toObjects(Following::class.java)

                    for (following in followings) {

                        followingCount++

                    }

                    binding.followingCount.text = followingCount.toString()

                }
            }



    }

    private fun getFollowerCount(userID : String) {

        FirebaseFirestore.getInstance().collection("users")
            .document(userID)
            .collection("follower")
            .addSnapshotListener { documentSnapshot, firebaseFirestoreException ->

                if (documentSnapshot != null) {

                    val followers = documentSnapshot.toObjects(Follower::class.java)

                    for (follower in followers) {

                        followerCount++

                    }

                    binding.followersCount.text = followerCount.toString()
                    followerCount = 0

                }
            }


    }


    private fun checkFollower(userID: String) {

        FirebaseFirestore.getInstance().collection("users")
            .document(userID)
            .collection("follower")
            .get()
            .addOnCompleteListener {
                if (it.isSuccessful) {

                    val followers = it.result!!.toObjects(Follower::class.java)

                    for (follower in followers) {

                        if (follower.userId == UserManager.userToken.toString()) {

                            binding.followClickedButton.visibility = View.VISIBLE
                            break
                        }

                    }


                }

            }


    }


    private fun addFollower(user: User) {

        FirebaseFirestore.getInstance().collection("users")
            .document(UserManager.userToken.toString())
            .collection("following").document(user!!.userId)
            .set(mapOf("userId" to user.userId))
            .addOnSuccessListener {

                FirebaseFirestore.getInstance().collection("users").document(user.userId)
                    .collection("follower").document(UserManager.userToken.toString())
                    .set(mapOf("userId" to UserManager.userToken.toString()))

                binding.followButton.visibility = View.INVISIBLE
                binding.followClickedButton.visibility = View.VISIBLE

            }
    }

    private fun unfollow(user: User) {

        FirebaseFirestore.getInstance().collection("posts")
            .document(UserManager.userToken.toString())
            .collection("following").document(user!!.userId).delete()
            .addOnSuccessListener {

                FirebaseFirestore.getInstance().collection("users")
                    .document(user.userId)
                    .collection("follower")
                    .document(UserManager.userToken.toString())
                    .delete()

                binding.followButton.visibility = View.VISIBLE
                binding.followClickedButton.visibility = View.INVISIBLE


            }
    }
}



