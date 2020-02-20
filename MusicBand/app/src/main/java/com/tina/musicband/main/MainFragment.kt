package com.tina.musicband.main


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.tina.musicband.MainActivity
import com.tina.musicband.R
import com.tina.musicband.data.Comments
import com.tina.musicband.data.Posts
import com.tina.musicband.data.Songs
import com.tina.musicband.databinding.FragmentMainBinding
import com.tina.musicband.ext.getVmFactory
import java.sql.Timestamp

enum class POST_TYPES(val value: String) {
    MUSIC("music"),
    EVENT("event")
}

/**
 * A simple [Fragment] subclass.
 */
class MainFragment : Fragment() {

    private var isOpen = false

    lateinit var binding: FragmentMainBinding

    val postsList = mutableListOf<Posts>()

    val viewModel by viewModels<MainViewModel> { getVmFactory() }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_main, container, false
        )

        binding.lifecycleOwner = this

        // Implement the FAB Animation
        binding.mainFab.setOnClickListener {
            fabAnimation()
        }

        // Navigate to the Add Music Fragment
        binding.musicFab.setOnClickListener {
            findNavController().navigate(R.id.action_global_addMusicFragment)
        }

        binding.eventFab.setOnClickListener {
            findNavController().navigate(R.id.action_global_addEventFragment)
        }


        val mainAdapter = MainAdapter(viewModel)

        binding.recyclerViewMainPage.adapter = mainAdapter

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
                    showHint()
                    mainAdapter.submitList(list)
                }
            }




        return binding.root
    }

    // FAB Animation Settings

    private fun fabAnimation() {

        val fabOpen = AnimationUtils.loadAnimation(activity, R.anim.fab_open)
        val fabClose = AnimationUtils.loadAnimation(activity, R.anim.fab_close)
        val fabRotateForward = AnimationUtils.loadAnimation(activity, R.anim.fab_rotate_forward)
        val fabRotateBackward = AnimationUtils.loadAnimation(activity, R.anim.fab_rotate_backward)

        if (isOpen) {

            binding.mainFab.startAnimation(fabRotateForward)
            binding.musicFab.startAnimation(fabClose)
            binding.eventFab.startAnimation(fabClose)
            binding.musicFab.setClickable(false)
            binding.eventFab.setClickable(false)

            isOpen = false

        } else {

            binding.mainFab.startAnimation(fabRotateBackward)
            binding.musicFab.startAnimation(fabOpen)
            binding.eventFab.startAnimation(fabOpen)
            binding.musicFab.setClickable(true)
            binding.eventFab.setClickable(true)

            isOpen = true
        }

    }

    private fun showHint(){
        if(postsList.size == 0){
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
