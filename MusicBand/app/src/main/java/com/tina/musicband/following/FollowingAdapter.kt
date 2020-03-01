package com.tina.musicband.following

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore
import com.tina.musicband.avatar.getAvatarDrawable
import com.tina.musicband.data.Following
import com.tina.musicband.data.User
import com.tina.musicband.databinding.ItemFollowProfileBinding
import com.tina.musicband.login.UserManager

class FollowingAdapter :
    ListAdapter<Following, FollowingAdapter.FollowingViewHolder>(FollowingDiffCallback) {
    override fun onBindViewHolder(holder: FollowingViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FollowingViewHolder {
        return FollowingViewHolder.from(parent)
    }


    class FollowingViewHolder private constructor(val binding: ItemFollowProfileBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(following: Following) {

           FirebaseFirestore.getInstance().collection("users")
               .document(following.userId)
               .get()
               .addOnCompleteListener {

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
                    .document(UserManager.userToken.toString()).collection("following")
                    .document(following.userId)
                    .delete()
                    .addOnCompleteListener {

                        if (it.isSuccessful) {

                            FirebaseFirestore.getInstance().collection("users")
                                .document(following.userId).collection("follower")
                                .document(UserManager.userToken.toString())
                                .delete()
                        }

                    }
            }

            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): FollowingViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ItemFollowProfileBinding.inflate(layoutInflater, parent, false)

                return FollowingViewHolder(binding)
            }
        }
    }

    companion object FollowingDiffCallback : DiffUtil.ItemCallback<Following>() {
        override fun areItemsTheSame(
            oldItem: Following,
            newItem: Following
        ): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(
            oldItem: Following,
            newItem: Following
        ): Boolean {
            return oldItem.userId == newItem.userId
        }
    }
}