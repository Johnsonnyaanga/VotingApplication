package com.example.votingapp.viewholders

import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.votingapp.R
import com.google.android.material.button.MaterialButton


class ContestantsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    var name: TextView
    var post: TextView
    var votebox:ImageView
    var profileImage:ImageView


    init {
        name = itemView.findViewById(R.id.name)
        post = itemView.findViewById(R.id.post)
        votebox = itemView.findViewById(R.id.votebox)
        profileImage = itemView.findViewById(R.id.profile_image)


    }
}