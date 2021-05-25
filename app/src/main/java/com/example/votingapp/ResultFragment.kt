package com.example.votingapp

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.votingapp.models.Contestants
import com.example.votingapp.viewholders.ContestantsViewHolder
import com.example.votingapp.viewholders.ResultViewHolder
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.squareup.picasso.Picasso


class ResultFragment : Fragment() {
    var thisContext: Context? = null
    var id: String? = null
    private lateinit var databaseReference: DatabaseReference



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_result, container, false)

        thisContext = this.context
        lateinit var recyclerView: RecyclerView
        recyclerView = view.findViewById(R.id.result_recyclerview)
        recyclerView.layoutManager = LinearLayoutManager(thisContext)
        recyclerView.setHasFixedSize(true)
        displayContestants(recyclerView)




        return view
    }
    private fun toasMessage(message: String){
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()

    }

    private fun displayContestants(myrecycler: RecyclerView){
        val options: FirebaseRecyclerOptions<Contestants>
        val adapter: FirebaseRecyclerAdapter<Contestants, ResultViewHolder>
        var mReference: DatabaseReference = FirebaseDatabase.getInstance().reference.child("votingapp")
        databaseReference = mReference.child("contestants")
        var query = databaseReference
        databaseReference.keepSynced(true)

        options = FirebaseRecyclerOptions.Builder<Contestants>()
            .setQuery(query, Contestants::class.java).build()
        adapter = object : FirebaseRecyclerAdapter<Contestants, ResultViewHolder>(options) {
            override fun onBindViewHolder(
                holder: ResultViewHolder,
                position: Int,
                model: Contestants
            ) {
                holder.name.text = model.name
                holder.post.text =model.post
                Picasso.get().load(model.image).into(holder.profileImage)
                holder.numberOfVotes.text = model.totalCount

            }

            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ResultViewHolder {
                val v = LayoutInflater.from(parent.context)
                    .inflate(R.layout.result_view, parent, false)
                return ResultViewHolder(v)
            }
        }
        adapter.startListening()
        myrecycler.adapter = adapter
        adapter.notifyDataSetChanged()
    }



}