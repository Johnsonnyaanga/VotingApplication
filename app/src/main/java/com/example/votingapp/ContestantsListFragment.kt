package com.example.votingapp

import android.content.ContentValues.TAG
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.votingapp.models.Contestants
import com.example.votingapp.models.HasVoted
import com.example.votingapp.viewholders.ContestantsViewHolder
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlin.math.absoluteValue


class ContestantsListFragment : Fragment() {
    var thisContext: Context? = null
    val currentUsr = FirebaseAuth.getInstance().currentUser.uid
    var id: String? = null
    var addContestant:FloatingActionButton? = null
    private val mReference: DatabaseReference = FirebaseDatabase.getInstance().reference.child("votingapp")



    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
      val view = inflater.inflate(R.layout.fragment_contestants_list, container, false)

        thisContext = requireContext()
        val databaseReference = mReference.child("contestants")



        val recyclerView: RecyclerView
        val options: FirebaseRecyclerOptions<Contestants>
        val adapter: FirebaseRecyclerAdapter<Contestants, ContestantsViewHolder>
        databaseReference.keepSynced(true)
        val user = FirebaseAuth.getInstance().currentUser
        databaseReference.keepSynced(true)
        recyclerView = view.findViewById(R.id.contestants_recyclerview)
        recyclerView.layoutManager = LinearLayoutManager(thisContext)
        recyclerView.setHasFixedSize(true)
        options = FirebaseRecyclerOptions.Builder<Contestants>()
                .setQuery(databaseReference, Contestants::class.java).build()
        adapter = object : FirebaseRecyclerAdapter<Contestants, ContestantsViewHolder>(options) {
            override fun onBindViewHolder(holder: ContestantsViewHolder, position: Int, model: Contestants) {
                holder.name.text = model.name
                holder.post.text =model.post
                holder.votebox.setOnClickListener(
                        View.OnClickListener {
                            //add one vote to the voted contestant
                            var votedcount = model.totalCount.toInt()+1
                            getRef(position).child("totalCount").setValue(votedcount.toString())
                            toasMessage(votedcount.toString())
                        }
                )

            }

            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContestantsViewHolder {
                val v = LayoutInflater.from(parent.context)
                        .inflate(R.layout.contest_view, parent, false)
                return ContestantsViewHolder(v)
            }
        }
        adapter.startListening()
        recyclerView.adapter = adapter













        return view


}
    private fun toasMessage(message: String){
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()

    }
/*
    private fun addVote(modela: Contestants?) {

        val hasVotedRef = mReference.child("voted").child(modela!!.post)
        val eventListener: ValueEventListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (!dataSnapshot.exists()) {

                    //throw a Dialog showing voter has already voted
                    toasMessage("voted already")

                }else{
                    //cast vote and record user voted for specific post
                    val initialvotes:Int = Integer.parseInt(modela?.totalCount.toString())
                    val aftervoted:Int = initialvotes + 1
                    Log.d("total",initialvotes.toString())
                    Log.d("totalv",aftervoted.toString())
                    modela?.totalCount = aftervoted
                    addUserHasVoted(modela)

                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.d(TAG, databaseError.message)
            }
        }
        hasVotedRef.child(currentUsr).addListenerForSingleValueEvent(eventListener)



    }

    private fun addUserHasVoted(mod: Contestants?) {
        val hasVoted = mReference.child("voted").child(mod!!.post)
        hasVoted.child(currentUsr)
                .setValue(currentUsr)
                .addOnSuccessListener { OnSuccessListener ->
                    toasMessage("Voted")
                }
    }



    private fun checkVoterHasVoted(voterID: String, modelii: Contestants){
        val hasVotedRef = mReference.child("voted").child(modelii.post)
        val eventListener: ValueEventListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (!dataSnapshot.exists()) {

                    //throw a Dialog showing voter has already voted
                    toasMessage("voted already")

                }else{
                    //cast vote and record user voted for specific post
                    addVote(modelii)
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.d(TAG, databaseError.message)
            }
        }
       hasVotedRef.child(voterID).addListenerForSingleValueEvent(eventListener)

    }*/
}