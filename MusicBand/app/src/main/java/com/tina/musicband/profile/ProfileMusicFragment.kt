package com.tina.musicband.profile


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.google.firebase.firestore.FirebaseFirestore
import com.tina.musicband.R
import com.tina.musicband.data.Songs
import com.tina.musicband.databinding.FragmentProfileMusicBinding
import com.tina.musicband.search.SearchMusicAdapter

/**
 * A simple [Fragment] subclass.
 */
class ProfileMusicFragment : Fragment() {

    lateinit var binding : FragmentProfileMusicBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_profile_music, container, false
        )

        val adapter = SearchMusicAdapter()

        binding.recyclerViewMusicProfile.adapter = adapter

        FirebaseFirestore.getInstance().collection("songs").get()
            .addOnCompleteListener {
                if(it.isSuccessful){
                    val songsList = mutableListOf<Songs>()
                    for (document in it.result!!){

                        val songs = document.toObject(Songs::class.java)
                        songsList.add(songs)

                    }
                    adapter.submitList(songsList)
                }
            }

        // Inflate the layout for this fragment
        return binding.root
    }


}
