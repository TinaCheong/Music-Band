package com.tina.musicband.profile


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.tina.musicband.R
import com.tina.musicband.data.Comments
import com.tina.musicband.data.Posts
import com.tina.musicband.data.Songs
import com.tina.musicband.databinding.FragmentProfileEventBinding
import com.tina.musicband.ext.getVmFactory
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

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_profile_event, container, false
        )

        val adapter = MainAdapter(viewModel)

        binding.recyclerViewEventProfile.adapter = adapter

        FirebaseFirestore.getInstance().collection("posts")
            .orderBy("createdTime", Query.Direction.DESCENDING)
            .get()
            .addOnCompleteListener {
                if(it.isSuccessful){

                    val list = mutableListOf<PostSealedItem>()

                    for (document in it.result!!){

                        val posts = document.toObject(Posts::class.java)
                        postsList.add(posts)

                        when(posts.type) {
                            POST_TYPES.EVENT.value -> list.add(PostSealedItem.EventItem(posts))
                            POST_TYPES.MUSIC.value -> list.add(PostSealedItem.MusicItem(posts))
                        }

                    }
                    adapter.submitList(list)
                }
            }

        // Inflate the layout for this fragment
        return binding.root
    }


}
