package com.example.votingapp

import android.app.Activity
import android.content.Intent
import android.icu.text.DateTimePatternGenerator.PatternInfo.OK
import android.net.Uri
import android.os.Bundle
import android.provider.ContactsContract
import android.provider.MediaStore
import android.util.Log
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
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView
import okhttp3.MediaType


class AddContestants : Fragment(),AdapterView.OnItemSelectedListener {
private lateinit var btnAddContestants: Button
private lateinit var contestantName:TextInputEditText
private lateinit var Spinner:Spinner
private lateinit var uploadImageText:TextView
private lateinit var SpinnerItemSelectedPost:String
val currentUsr = FirebaseAuth.getInstance().currentUser.uid
    private var mImageUri: Uri? = null
    private var mImageView: CircleImageView? = null
    var mStorageRef: StorageReference? = null
    //private lateinit var post:TextInputEditText
private val mReference:DatabaseReference = FirebaseDatabase
        .getInstance().reference.child("votingapp")
    val data = mutableListOf<String>("Chairman","Secretary","Treasurer")


    




    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
       val view = inflater.inflate(R.layout.fragment_add_contestants, container, false)

        mStorageRef = FirebaseStorage.getInstance().getReference("votingapp")
        contestantName = view.findViewById(R.id.contestant_name_input)
        mImageView = view.findViewById(R.id.contestant_image)
        /*mImageView?.setOnClickListener(View.OnClickListener { selectImage() })*/
        //post = view.findViewById(R.id.contestant_post_input)
        btnAddContestants = view.findViewById(R.id.add_contestatnts)
        uploadImageText = view.findViewById(R.id.upload_txt)
        uploadImageText.setOnClickListener(View.OnClickListener {
            selectImage()
        })

        btnAddContestants.setOnClickListener(View.OnClickListener {
            if (mImageUri!=null) {

                val fileReference = mStorageRef!!
                    .child("contestants_images")
                    .child(
                        System.currentTimeMillis().toString() + "." + getFileExtension(mImageUri)
                    )
                fileReference.putFile(mImageUri!!)
                    .addOnSuccessListener {
                        fileReference.downloadUrl.addOnSuccessListener { uri ->
                            val imageuri: String? = uri.toString()

                            addContestant(
                                contestantName.text.toString()!!,
                                SpinnerItemSelectedPost,
                                imageuri!!,
                                currentUsr
                            )
                            findNavController().navigate(R.id.action_addContestants_to_adminFragmentList)

                        }

                    }
                    .addOnFailureListener { failure ->
                        Log.d("errormessage", failure.message.toString())
                    }
            }else{
                toasMessage("Select Image Please")
            }


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

    private fun addContestant(name:String?,post:String?,imageurl:String?,usrID:String?){

        val contestant = Contestants(name,post,imageurl,usrID,0.toString())
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
    private fun toasMessage(message: String){
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()

    }
    private fun selectImage() {
        val gallery = Intent(
            Intent.ACTION_PICK,
            MediaStore.Images.Media.INTERNAL_CONTENT_URI)
        resultLauncher.launch(gallery)
    }
    private fun getFileExtension(uri: Uri?): String? {
        val cR = activity?.contentResolver
        val mime = MimeTypeMap.getSingleton()
        return mime.getExtensionFromMimeType(cR?.getType(uri!!))
    }
    var resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
        result->
        if (result.resultCode == Activity.RESULT_OK && result.data != null ){
            mImageUri = result.data!!.data
            Picasso.get().load(mImageUri).into(mImageView)

        }
    }


}