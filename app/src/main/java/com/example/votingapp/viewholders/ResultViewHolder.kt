package com.example.votingapp.viewholders

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.votingapp.R

class ResultViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    var name: TextView
    var post: TextView
    var profileImage: ImageView
    var numberOfVotes: TextView


    init {
        name = itemView.findViewById(R.id.name)
        post = itemView.findViewById(R.id.post)
        numberOfVotes = itemView.findViewById(R.id.no_ofVotes)
        profileImage = itemView.findViewById(R.id.profile_image)


    }
}