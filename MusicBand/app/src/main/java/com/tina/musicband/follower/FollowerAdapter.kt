package com.tina.musicband.follower

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore
import com.tina.musicband.avatar.getAvatarDrawable
import com.tina.musicband.data.Follower
import com.tina.musicband.data.User
import com.tina.musicband.databinding.ItemFollowProfileBinding
import com.tina.musicband.login.UserManager

class FollowerAdapter(private val viewModel: FollowerProfileViewModel) : ListAdapter<Follower, FollowerAdapter.FollowerViewHolder>(FollowerDiffCallback) {

    override fun onBindViewHolder(holder: FollowerViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FollowerViewHolder {
        return FollowerViewHolder.from(parent)
    }


    class FollowerViewHolder private constructor(val binding: ItemFollowProfileBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(follower: Follower) {

            FirebaseFirestore.getInstance().collection("users")
                .document(follower.userId)
                .get().addOnCompleteListener {

                    if (it.isSuccessful) {

                        val user = it.result!!.toObject(User::class.java)

                        user?.let {
                            binding.userAvatar.setImageDrawable(
                                it.avatar.getAvatarDrawable()
                            )
                            binding.username.setText(it.username)
                            binding.userIntroduction.setText(it.introduction)

                        }


                    }

                }


            binding.deleteButton.setOnClickListener {

                FirebaseFirestore.getInstance().collection("users")
                    .document(UserManager.userToken.toString()).collection("follower")
                    .document(follower.userId)
                    .delete()
                    .addOnCompleteListener {

                        if (it.isSuccessful) {

                            FirebaseFirestore.getInstance().collection("users")
                                .document(follower.userId).collection("following")
                                .document(UserManager.userToken.toString())
                                .delete()
                        }
        }


    }

    binding.executePendingBindings()
}

companion object {
    fun from(parent: ViewGroup): FollowerViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemFollowProfileBinding.inflate(layoutInflater, parent, false)

        return FollowerViewHolder(binding)
    }
}
}

companion object FollowerDiffCallback : DiffUtil.ItemCallback<Follower>() {
    override fun areItemsTheSame(oldItem: Follower, newItem: Follower): Boolean {
        return oldItem === newItem
    }

    override fun areContentsTheSame(oldItem: Follower, newItem: Follower): Boolean {
        return oldItem.userId == newItem.userId
    }
}
}