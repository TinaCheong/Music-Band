package com.tina.musicband.others


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.viewpager.widget.PagerAdapter
import com.google.android.material.tabs.TabLayout
import com.google.firebase.firestore.FirebaseFirestore
import com.tina.musicband.R
import com.tina.musicband.avatar.getAvatarDrawable
import com.tina.musicband.data.Follower
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
    private var followingCount : Int = 0
    private var followerCount : Int = 0

    private val viewModel by viewModels<ProfileOthersViewModel> { getVmFactory(ProfileOthersFragmentArgs.fromBundle(arguments!!).song) }

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

        viewModel.song.value?.apply {

            readUserDataFromSong(this)

            getPostCount(this)

            getFollowerCount(this)

            getFollowingCount(this)

            checkFollower(this)

        }




                return binding.root
            }


    private fun changePages(){

        adapter = ProfileOthersViewPager(childFragmentManager)
        binding.viewpagerProfileOthers.adapter = adapter
        binding.viewpagerProfileOthers.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(binding.tabLayoutProfileOthers))

    }

    private fun setTabLayoutIcons(){

        binding.tabLayoutProfileOthers.setupWithViewPager(binding.viewpagerProfileOthers)
        binding.tabLayoutProfileOthers.getTabAt(0)?.setIcon(R.drawable.ic_event_title)
        binding.tabLayoutProfileOthers.getTabAt(1)?.setIcon(R.drawable.ic_calender)

    }

    fun readUserDataFromSong(songs: Songs){

        FirebaseFirestore.getInstance().collection("users")
            .document(songs.userId!!).get()
            .addOnCompleteListener {
                if(it.isSuccessful){

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
                    }

                    binding.followButton.setOnClickListener {
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


                    binding.followClickedButton.setOnClickListener {
                        FirebaseFirestore.getInstance().collection("posts")
                            .document(UserManager.userToken.toString())
                            .collection("following").document(user!!.userId).delete()
                            .addOnSuccessListener {

                                FirebaseFirestore.getInstance().collection("users").document(user.userId)
                                    .collection("follower").document(UserManager.userToken.toString())
                                    .delete()

                                binding.followButton.visibility = View.VISIBLE
                                binding.followClickedButton.visibility = View.INVISIBLE

                            }
                    }


                }

            }
    }

    private fun getPostCount(song: Songs){

        FirebaseFirestore.getInstance().collection("posts")
            .whereEqualTo("userId", song.userId)
            .get()
            .addOnCompleteListener {
                if(it.isSuccessful){
                    for(document in it.result!!){
                        postCount++
                    }

                    binding.postsCount.text = postCount.toString()
                }
            }
    }

    private fun getFollowingCount(song: Songs){

        FirebaseFirestore.getInstance().collection("users")
            .document(song.userId!!)
            .collection("following")
            .get()
            .addOnCompleteListener {
                if(it.isSuccessful){
                    for(document in it.result!!){
                        followingCount++
                    }

                    binding.followingCount.text = followingCount.toString()
                }
            }

    }

    private fun getFollowerCount(song: Songs){

        FirebaseFirestore.getInstance().collection("users")
            .document(song.userId!!)
            .collection("follower")
            .get()
            .addOnCompleteListener {
                if(it.isSuccessful){
                    for(document in it.result!!){
                        followerCount++

                    }

                    binding.followersCount.text = followerCount.toString()
                }
            }

    }

    private fun checkFollower(song: Songs){

        FirebaseFirestore.getInstance().collection("users")
            .document(song.userId!!)
            .collection("follower")
            .get()
            .addOnCompleteListener {
                if(it.isSuccessful){

                    val followers = it.result!!.toObjects(Follower::class.java)

                    for(follower in followers){

                        if(follower.userId == UserManager.userToken.toString()){

                            binding.followClickedButton.visibility = View.VISIBLE
                            break
                        }

                    }


                    }

                }


            }


    }



