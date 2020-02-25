package com.tina.musicband.match


import android.os.Bundle
import android.os.UserManager
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.PagerSnapHelper
import com.google.firebase.firestore.FirebaseFirestore
import com.tina.musicband.MainActivity
import com.tina.musicband.R
import com.tina.musicband.data.Matching
import com.tina.musicband.data.Posts
import com.tina.musicband.data.User
import com.tina.musicband.databinding.FragmentQuickMatchBinding
import kotlin.random.Random

/**
 * A simple [Fragment] subclass.
 */
class QuickMatchFragment : Fragment() {

    lateinit var binding: FragmentQuickMatchBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_quick_match, container, false
        )

        val adapter = QuickMatchAdapter()

        binding.recyclerViewQuickMatch.adapter = adapter

        FirebaseFirestore.getInstance().collection("users")
            .get()
            .addOnCompleteListener {
            if(it.isSuccessful){
                val userList = mutableListOf<User>()
//                it.result?.toObjects(User::class.java)

                for(document in it.result!!){
                    val user = document.toObject(User::class.java)
                    if(user.username == com.tina.musicband.login.UserManager.userName) continue
                    userList.add(user)
                }

                val size = userList.size
                val randomUserList = mutableListOf<User>()

                for( i in 0..size){

                    val randomNumber = Random.nextInt(userList.size)

                    val randomData = userList.removeAt(randomNumber)

                    randomUserList.add(randomData)

                    if(randomUserList.size == 5) break
                }

                adapter.submitList(randomUserList)

            }

        }




        val pagerSnapHelper = PagerSnapHelper()
        pagerSnapHelper.attachToRecyclerView(binding.recyclerViewQuickMatch)

        binding.dotIndicator.attachToRecyclerView(binding.recyclerViewQuickMatch, pagerSnapHelper)

        adapter.registerAdapterDataObserver(binding.dotIndicator.adapterDataObserver)

        // Inflate the layout for this fragment
        return binding.root
    }


}
