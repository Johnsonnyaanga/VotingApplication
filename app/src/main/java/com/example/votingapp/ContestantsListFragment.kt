package com.example.votingapp

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
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
import com.google.firebase.database.*
import com.squareup.picasso.Picasso
import java.util.zip.Inflater


class ContestantsListFragment : Fragment() {
    var thisContext: Context? = null
    private lateinit var alertDialog: AlertDialog
    var id: String? = null
    private var postsGroup: RadioGroup? = null
    private var radioPostButton: RadioButton? = null
    lateinit var recyclerView: RecyclerView
    private lateinit var databaseReference:DatabaseReference
    var mReference: DatabaseReference = FirebaseDatabase.getInstance().reference.child("votingapp")
    var hasVoted = mReference.child("voted")




    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

      val view = inflater.inflate(R.layout.fragment_contestants_list, container, false)
        thisContext = this.context
        recyclerView = view.findViewById(R.id.contestants_recyclerview)
        recyclerView.layoutManager = LinearLayoutManager(thisContext)
        recyclerView.setHasFixedSize(true)
        /*val posta ="post"
        val chairman = "Secretary"
        displayContestants(recyclerView,posta,chairman)*/


        postsGroup=view.findViewById(R.id.post_type_radio_group)
        postsGroup?.setOnCheckedChangeListener(object : RadioGroup.OnCheckedChangeListener {
            override fun onCheckedChanged(group: RadioGroup?, checkedId: Int) {
                // checkedId is the RadioButton selected
                val selectedId: Int? = checkedId
                radioPostButton = view.findViewById(selectedId!!)
                var fil = radioPostButton?.text.toString()
                displayContestants(recyclerView, "post", fil)

            }
        })

















        return view


}
    private fun toasMessage(message: String){
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()

    }

    private fun displayContestants(myrecycler: RecyclerView, filter: String, filterkey: String){
        val options: FirebaseRecyclerOptions<Contestants>
        val adapter: FirebaseRecyclerAdapter<Contestants, ContestantsViewHolder>
        var mReference: DatabaseReference = FirebaseDatabase.getInstance().reference.child("votingapp")
        databaseReference = mReference.child("contestants")
        var query = databaseReference.orderByChild(filter).equalTo(filterkey)
        databaseReference.keepSynced(true)

        options = FirebaseRecyclerOptions.Builder<Contestants>()
            .setQuery(query, Contestants::class.java).build()
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
                        getConfirmVoteDialog(holder.itemView, model, position)


                    }
                )


            }

            fun getConfirmVoteDialog(view: View, modela: Contestants, positiona: Int){

                val viewGroup: ViewGroup? = view.findViewById(android.R.id.content)


                val dialogView: View =
                    LayoutInflater.from(requireContext())
                        .inflate(R.layout.confirm_vote_dialog, viewGroup, false)

                val builder: AlertDialog.Builder = AlertDialog.Builder(requireContext())
                val cancel = dialogView.findViewById<Button>(R.id.button_cancel)
                val add = dialogView.findViewById<Button>(R.id.button_save)

                cancel.setOnClickListener(View.OnClickListener { view ->
                    alertDialog.dismiss()
                })


                add.setOnClickListener(View.OnClickListener { view ->
                    var checkHasvoted = hasVoted.child(FirebaseAuth.getInstance().currentUser.uid)
                    val eventListener: ValueEventListener = object : ValueEventListener {
                        override fun onDataChange(dataSnapshot: DataSnapshot) {
                            if (dataSnapshot.exists()) {
                                toasMessage("You have voted already")
                            }else{

                                //call add vote function
                                var votedcount = modela.totalCount.toInt() + 1
                                getRef(positiona).child("totalCount").setValue(votedcount.toString())
                                hasVoted.child(FirebaseAuth.getInstance().currentUser.uid).setValue(FirebaseAuth.getInstance().currentUser.uid)

                                //holder.votebox.setImageResource(R.drawable.vote_tick)
                                toasMessage(votedcount.toString())
                                alertDialog.dismiss()

                            }
                        }

                        override fun onCancelled(databaseError: DatabaseError) {
                            Log.d("errors", databaseError.message)
                        }
                    }
                    checkHasvoted.addListenerForSingleValueEvent(eventListener)


                })


                builder.setView(dialogView)
                alertDialog  = builder.create()
                alertDialog.show()


            }




            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContestantsViewHolder {
                val v = LayoutInflater.from(parent.context)
                    .inflate(R.layout.contest_view, parent, false)
                return ContestantsViewHolder(v)
            }
        }
        adapter.startListening()
        myrecycler.adapter = adapter
        adapter.notifyDataSetChanged()
    }



    private fun addVote(modela: Contestants):Int{
        var votedcount = modela.totalCount.toInt() + 1
        toasMessage(votedcount.toString())
        return votedcount
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