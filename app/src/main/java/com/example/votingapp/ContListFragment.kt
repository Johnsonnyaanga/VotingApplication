package com.example.votingapp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton

class ContListFragment : Fragment() {
    lateinit var chairman:ExtendedFloatingActionButton
    lateinit var secretary:ExtendedFloatingActionButton
    lateinit var treasurer:ExtendedFloatingActionButton
    lateinit var navController: NavController




    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view =  inflater.inflate(R.layout.fragment_cont_list, container, false)

        chairman = view.findViewById(R.id.chairman_id)
        secretary = view.findViewById(R.id.secretary_id)
        treasurer = view.findViewById(R.id.treasurer_id)

        chairman.setOnClickListener {
        }
        secretary.setOnClickListener {
        }
        treasurer.setOnClickListener {
        }







        return view
    }


}