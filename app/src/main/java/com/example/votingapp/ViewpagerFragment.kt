package com.example.votingapp

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.FragmentActivity
import androidx.navigation.fragment.findNavController
import androidx.viewpager.widget.ViewPager
import com.example.votingapp.viewpageradapters.ViewPagerAdapter
import com.google.android.material.tabs.TabLayout
import com.google.firebase.auth.FirebaseAuth

class ViewpagerFragment : Fragment() {
    var viewPager: ViewPager? = null
    var pagerAdapter: ViewPagerAdapter? = null
    var Titles = arrayOf<CharSequence>("Candidates", "Result")
    var Numboftabs = 2
    lateinit var toolbar:Toolbar


    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {



       val view = inflater.inflate(R.layout.fragment_viewpager, container, false)

        toolbar = view.findViewById(R.id.toolbar)
        (activity as AppCompatActivity?)!!.setSupportActionBar(toolbar)
        (activity as AppCompatActivity?)!!.title = null




        pagerAdapter = ViewPagerAdapter(fragmentManager, Titles, Numboftabs)

        // Assigning ViewPager View and setting the adapter

        // Assigning ViewPager View and setting the adapter
        viewPager = view.findViewById(R.id.admin_viewpager)
        viewPager?.setAdapter(pagerAdapter)

        val tabLayout = view.findViewById<TabLayout>(R.id.admin_tablayout)
        tabLayout.setupWithViewPager(viewPager)

        return view
    }




    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.logout) {
            FirebaseAuth.getInstance().signOut()
            startActivity(Intent(this.activity,MainActivity::class.java))

        } else {
        }
        return super.onOptionsItemSelected(item)
    }








}