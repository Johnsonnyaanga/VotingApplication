package com.example.votingapp

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.votingapp.models.Contestants
import com.example.votingapp.viewholders.ContestantsAdminViewHolder
import com.example.votingapp.viewholders.ContestantsViewHolder
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.squareup.picasso.Picasso


class AdminFragmentList : Fragment() {
    private var thisContext: Context? = null
    private val currentUsr = FirebaseAuth.getInstance().currentUser.uid
    private var id: String? = null
    private var addContestant: FloatingActionButton? = null
    private val mReference: DatabaseReference = FirebaseDatabase.getInstance().reference.child("votingapp")


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_admin_list, container, false)

        thisContext = requireContext()
        val databaseReference = mReference.child("contestants")
        addContestant =  view.findViewById(R.id.add_contestatnt_id_admin)
        addContestant?.setOnClickListener(View.OnClickListener {
            findNavController().navigate(R.id.action_adminFragmentList_to_addContestants)

        })



        val recyclerView: RecyclerView
        val options: FirebaseRecyclerOptions<Contestants>
        val adapter: FirebaseRecyclerAdapter<Contestants, ContestantsAdminViewHolder>
        databaseReference.keepSynced(true)
        val user = FirebaseAuth.getInstance().currentUser
        databaseReference.keepSynced(true)
        recyclerView = view.findViewById(R.id.contestants_recyclerview)
        recyclerView.layoutManager = LinearLayoutManager(thisContext)
        recyclerView.setHasFixedSize(true)
        options = FirebaseRecyclerOptions.Builder<Contestants>()
                .setQuery(databaseReference, Contestants::class.java).build()
        adapter = object : FirebaseRecyclerAdapter<Contestants, ContestantsAdminViewHolder>(options) {
            override fun onBindViewHolder(holder: ContestantsAdminViewHolder, position: Int, model: Contestants) {
                holder.name.text = model.name
                holder.post.text =model.post
                Picasso.get().load(model.image).into(holder.profileImage)
               holder.deleteContestant.setOnClickListener{
                   getRef(position).removeValue()
               }

            }

            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContestantsAdminViewHolder {
                val v = LayoutInflater.from(parent.context)
                        .inflate(R.layout.contestants_view, parent, false)
                return ContestantsAdminViewHolder(v)
            }
        }
        adapter.startListening()
        recyclerView.adapter = adapter



        return view
    }
    private fun toasMessage(message: String){
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()

    }


}