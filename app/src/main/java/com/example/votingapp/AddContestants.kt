package com.example.votingapp

import android.app.Activity
import android.content.Intent
import android.icu.text.DateTimePatternGenerator.PatternInfo.OK
import android.net.Uri
import android.os.Bundle
import android.provider.ContactsContract
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.MimeTypeMap
import android.widget.*
import androidx.activity.result.ActivityResultRegistry
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.findNavController
import com.example.votingapp.models.Contestants
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView


class AddContestants : Fragment(),AdapterView.OnItemSelectedListener {
private lateinit var btnAddContestants: Button
private lateinit var contestantName:TextInputEditText
private lateinit var Spinner:Spinner
private lateinit var SpinnerItemSelectedPost:String
    private var mImageUri: Uri? = null
    private var mImageView: CircleImageView? = null

//private lateinit var post:TextInputEditText
private val mReference:DatabaseReference = FirebaseDatabase.getInstance().reference.child("votingapp")
    val data = mutableListOf<String>("Chairman","Secretary","Treasurer")


    




    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
       val view = inflater.inflate(R.layout.fragment_add_contestants, container, false)

        contestantName = view.findViewById(R.id.contestant_name_input)
        //post = view.findViewById(R.id.contestant_post_input)
        btnAddContestants = view.findViewById(R.id.add_contestatnts)

        btnAddContestants.setOnClickListener(View.OnClickListener {
            addContestant(contestantName.text.toString()!!,SpinnerItemSelectedPost)
            findNavController().navigate(R.id.action_addContestants_to_adminFragmentList)
        })


        val adapter = ArrayAdapter<String>(
                requireContext(),
                android.R.layout.simple_spinner_item,
                data
        )

        Spinner = view.findViewById(R.id.spinner)
        Spinner.adapter = adapter
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        Spinner.onItemSelectedListener = this


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

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        SpinnerItemSelectedPost = parent?.getItemAtPosition(position).toString()

    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
    }
    private fun selectImage() {
        val gallery = Intent(
            Intent.ACTION_PICK,
            MediaStore.Images.Media.INTERNAL_CONTENT_URI)
        resultLauncher.launch(gallery)
    }
    private fun getFileExtension(uri: Uri?): String? {
        val cR = contentResolver
        val mime = MimeTypeMap.getSingleton()
        return mime.getExtensionFromMimeType(cR.getType(uri!!))
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == AppCompatActivity.RESULT_OK && data != null && data.data != null) {
            mImageUri = data.data
            Picasso.get().load(mImageUri).into(mImageView)
            return
        }
    }
    var resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
        result->
        if (result.resultCode == Activity.RESULT_OK ){
            mImageUri = result.data
            Picasso.get().load(mImageUri).into(mImageView)

        }
    }


}