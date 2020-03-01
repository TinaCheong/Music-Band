package com.tina.musicband.match


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.PagerSnapHelper
import com.google.firebase.firestore.FirebaseFirestore
import com.tina.musicband.R
import com.tina.musicband.data.Following
import com.tina.musicband.data.User
import com.tina.musicband.databinding.FragmentQuickMatchBinding
import com.tina.musicband.login.UserManager
import java.util.*

/**
 * A simple [Fragment] subclass.
 */
class QuickMatchFragment : Fragment() {

    lateinit var binding: FragmentQuickMatchBinding
    lateinit var adapter: QuickMatchAdapter
    val userList = mutableListOf<User>()
    var matchList = mutableListOf<User>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_quick_match, container, false
        )

        adapter = QuickMatchAdapter()

        binding.recyclerViewQuickMatch.adapter = adapter

        setDotIndicator()

        FirebaseFirestore.getInstance().collection("users")
            .get()
            .addOnCompleteListener {
                if (it.isSuccessful) {

                    it.result?.toObjects(User::class.java)?.let {list ->
                        userList.addAll(list)
                    }

                    FirebaseFirestore.getInstance().collection("users")
                        .document(UserManager.userToken.toString())
                        .collection("following")
                        .get()
                        .addOnCompleteListener {

                            if (it.isSuccessful) {

                                var followings = listOf<String>()

                                it.result?.toObjects(Following::class.java)?.let {allFollowings ->
                                    followings = allFollowings.map { following -> following.userId }
                                }

                                val tempList
                                        = userList
                                    .filter { !(followings.contains(it.userId) || it.userId == UserManager.userToken) }
                                    .toMutableList()

//                                var tempList = mutableListOf<User>()
//
//                                for (checkUser in userList) {
//
//                                    var isContain = false
//
//                                    for (following in followings) {
//
//                                        if (following.userId == checkUser.userId) {
//
//                                            isContain = true
//
//                                            break
//
//                                        }
//
//                                    }
//
//                                    if (isContain == false) {
//
//                                        tempList.add(checkUser)
//
//                                    }
//
//                                }


                                if (tempList.size <= 5) {

                                    adapter.submitList(tempList.shuffled())

                                } else {

                                    var listSize = tempList.size

                                    for (i in 0 until 5){

                                        val randomNumber =  Random().nextInt(listSize)

                                        val randomData = tempList.get(randomNumber)

                                        matchList.add(randomData)

                                        tempList.removeAt(randomNumber)

                                        listSize--
                                    }

                                    adapter.submitList(matchList)

                                }

                                showHint()

                            }

                        }

                }

            }


        // Inflate the layout for this fragment
        return binding.root
    }

    private fun setDotIndicator(){

        val pagerSnapHelper = PagerSnapHelper()
        pagerSnapHelper.attachToRecyclerView(binding.recyclerViewQuickMatch)

        binding.dotIndicator.attachToRecyclerView(binding.recyclerViewQuickMatch, pagerSnapHelper)

        adapter.registerAdapterDataObserver(binding.dotIndicator.adapterDataObserver)


    }

    private fun showHint(){
        if(matchList.size == 0){
            binding.addAllFriend.visibility = View.VISIBLE
            binding.addAllFriendText.visibility = View.VISIBLE
        }else{
            binding.addAllFriend.visibility = View.GONE
            binding.addAllFriendText.visibility = View.GONE
        }

    }
}




