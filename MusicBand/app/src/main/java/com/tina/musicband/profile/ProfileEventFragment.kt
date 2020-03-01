package com.tina.musicband.profile


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.tina.musicband.R
import com.tina.musicband.data.Posts
import com.tina.musicband.databinding.FragmentProfileEventBinding
import com.tina.musicband.ext.getVmFactory
import com.tina.musicband.login.UserManager
import com.tina.musicband.main.MainAdapter
import com.tina.musicband.main.MainViewModel
import com.tina.musicband.main.POST_TYPES
import com.tina.musicband.main.PostSealedItem

/**
 * A simple [Fragment] subclass.
 */
class ProfileEventFragment : Fragment() {

    lateinit var binding: FragmentProfileEventBinding

    val viewModel by viewModels<MainViewModel> { getVmFactory() }

    private val postsList = mutableListOf<Posts>()

    val list = mutableListOf<PostSealedItem>()

    private lateinit var adapter: MainAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_profile_event, container, false
        )

        adapter = MainAdapter(viewModel)

        binding.recyclerViewEventProfile.adapter = adapter



//        viewModel.loadProfileAvatar()
        viewModel.getProfileAvatar(UserManager.userToken.toString())

        viewModel.isProfileAvatarPrepared.observe(this, Observer {
            it?.let {
                fetchPosts()
                viewModel.doneReadingProfileAvatar()
            }
        })

        // Inflate the layout for this fragment
        return binding.root
    }


    private fun fetchPosts() {
        FirebaseFirestore.getInstance().collection("posts")
            .orderBy("createdTime", Query.Direction.DESCENDING)
            .whereEqualTo("userId", UserManager.userToken.toString())
            .get()
            .addOnCompleteListener {
                if(it.isSuccessful){


                    for (document in it.result!!){

                        val posts = document.toObject(Posts::class.java)

                        postsList.add(posts)


                        when(posts.type) {
                            POST_TYPES.EVENT.value -> list.add(PostSealedItem.EventItem(posts))
                            POST_TYPES.MUSIC.value -> list.add(PostSealedItem.MusicItem(posts))
                        }

                    }
                    adapter.submitList(list)

                    showHint()
                }
            }
    }

    private fun showHint(){
        if(list.size == 0){
            binding.noPostImage.visibility = View.VISIBLE
            binding.questionMarkImage.visibility = View.VISIBLE
            binding.noPostText.visibility = View.VISIBLE
        }else{
            binding.noPostImage.visibility = View.GONE
            binding.questionMarkImage.visibility = View.GONE
            binding.noPostText.visibility = View.GONE
        }

    }


}
