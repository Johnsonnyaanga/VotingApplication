package com.example.votingapp

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.votingapp.models.Contestants
import com.example.votingapp.viewholders.ContestantsViewHolder
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.Query
import com.squareup.picasso.Picasso


class ContestantsListFragment : Fragment() {
    var thisContext: Context? = null
    val currentUsr = FirebaseAuth.getInstance().currentUser.uid
    var id: String? = null
    var checkedItemString:String? =null
    private var postsGroup: RadioGroup? = null
    private var radioPostButton: RadioButton? = null
    lateinit var recyclerView: RecyclerView
    private val mReference: DatabaseReference = FirebaseDatabase.getInstance().reference.child("votingapp")
    private lateinit var databaseReference:DatabaseReference



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

      val view = inflater.inflate(R.layout.fragment_contestants_list, container, false)
        thisContext = requireContext()



        databaseReference = mReference.child("contestants")
        recyclerView = view.findViewById(R.id.contestants_recyclerview)
        val query = databaseReference.orderByChild("post").equalTo("Secretary")
        displayContestants(recyclerView,query)


        postsGroup=view.findViewById(R.id.post_type_radio_group)

        databaseReference.keepSynced(true)

        postsGroup?.setOnCheckedChangeListener(object : RadioGroup.OnCheckedChangeListener {
            override fun onCheckedChanged(group: RadioGroup?, checkedId: Int) {
                // checkedId is the RadioButton selected
                val selectedId:Int? = checkedId
                radioPostButton = view.findViewById(selectedId!!)
                Toast.makeText(requireContext(),radioPostButton?.text.toString(),Toast.LENGTH_SHORT).show()
            }
        })

















        return view


}
    private fun toasMessage(message: String){
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()

    }

    private fun displayContestants(myrecycler:RecyclerView,myquery:Query){
        val options: FirebaseRecyclerOptions<Contestants>
        val adapter: FirebaseRecyclerAdapter<Contestants, ContestantsViewHolder>
        myrecycler.layoutManager = LinearLayoutManager(requireContext())
        myrecycler.setHasFixedSize(true)
        options = FirebaseRecyclerOptions.Builder<Contestants>()
            .setQuery(myquery, Contestants::class.java).build()
        adapter = object : FirebaseRecyclerAdapter<Contestants, ContestantsViewHolder>(options) {
            override fun onBindViewHolder(
                holder: ContestantsViewHolder,
                position: Int,
                model: Contestants
            ) {
                holder.name.text = model.name
                holder.post.text =model.post
                Picasso.get().load(model.image).into(holder.profileImage)
                holder.votebox.setOnClickListener(
                    View.OnClickListener {
                        //add one vote to the voted contestant
                        var votedcount = model.totalCount.toInt() + 1
                        getRef(position).child("totalCount").setValue(votedcount.toString())
                        //holder.votebox.setImageResource(R.drawable.vote_tick)

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
        myrecycler.adapter = adapter
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