package com.example.votingapp.viewholders

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.votingapp.R

class ContestantsAdminViewHolder(itemView:View):RecyclerView.ViewHolder(itemView) {
    var name: TextView
    var post: TextView
    var deleteContestant: ImageView
    var profileImage: ImageView


    init {
        name = itemView.findViewById(R.id.name)
        post = itemView.findViewById(R.id.post)
        deleteContestant = itemView.findViewById(R.id.delete)
        profileImage = itemView.findViewById(R.id.profile_image)


    }
}