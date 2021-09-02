package com.example.votingapp

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Toast
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.votingapp.models.Contestants
import com.example.votingapp.viewholders.ContestantsViewHolder
import com.example.votingapp.viewholders.ResultViewHolder
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.firebase.ui.database.ObservableSnapshotArray
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.squareup.picasso.Picasso


class ResultFragment : Fragment() {
    var thisContext: Context? = null
    val hash= HashMap<String?, Int? >()

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

        val view = inflater.inflate(R.layout.fragment_result, container, false)
        thisContext = this.context
        recyclerView = view.findViewById(R.id.result_recyclerview)
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

        var options: FirebaseRecyclerOptions<Contestants>
        var adapter: FirebaseRecyclerAdapter<Contestants, ResultViewHolder>
        var mReference: DatabaseReference = FirebaseDatabase.getInstance().reference.child("votingapp")
        databaseReference = mReference.child("contestants")
        var query = databaseReference.orderByChild(filter).equalTo(filterkey)
        databaseReference.keepSynced(true)
       // Log.d("ranked",query.orderByChild("totalCount").toString())

        options = FirebaseRecyclerOptions.Builder<Contestants>()
            .setQuery(query, Contestants::class.java)
            .build()




        adapter = object : FirebaseRecyclerAdapter<Contestants, ResultViewHolder>(options) {
            override fun onBindViewHolder(
                holder: ResultViewHolder,
                position: Int,
                model: Contestants
            ) {
                Log.d("malist",this.snapshots[0].totalCount.toString())

                //create a list of result type hashMap
                val name =  model.name
                Log.d("position $name",position.toString())
                hash[model.name] = model.totalCount
                holder.name.text = model.name
                holder.post.text =model.post
                holder.numberOfVotes.text = model.totalCount.toString()
                Picasso.get().load(model.image).into(holder.profileImage)

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
        Log.d("options", adapter.snapshots.toString())
        Log.d("hashmap",hash.toString())

    }



}