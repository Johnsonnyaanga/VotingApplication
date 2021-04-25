package com.example.votingapp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.example.votingapp.models.Contestants
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase


class AddContestants : Fragment() {
private lateinit var btnAddContestants: Button
private lateinit var contestantName:TextInputEditText
private lateinit var post:TextInputEditText
private val mReference:DatabaseReference = FirebaseDatabase.getInstance().reference.child("votingapp")





    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
       val view = inflater.inflate(R.layout.fragment_add_contestants, container, false)

        contestantName = view.findViewById(R.id.contestant_name_input)
        post = view.findViewById(R.id.contestant_post_input)
        btnAddContestants = view.findViewById(R.id.add_contestatnts)

        btnAddContestants.setOnClickListener(View.OnClickListener {
            addContestant(contestantName.text.toString()!!,post.text.toString()!!)
            findNavController().navigate(R.id.action_addContestants_to_adminFragmentList)
        })


        return view
    }

    private fun addContestant(name:String?,post:String?){

        val contestant = Contestants(name,post,"null","null",0.toString())
        val contest = mReference.child("contestants")
        contest.child(contest.push().key!!).setValue(contestant)
            .addOnSuccessListener {
                success ->
                Toast.makeText(requireContext(),"Successiful upload",Toast.LENGTH_SHORT).show()

            }

    }


}