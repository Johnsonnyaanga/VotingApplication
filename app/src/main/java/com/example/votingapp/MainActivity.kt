package com.example.votingapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.Toast
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.android.material.button.MaterialButton
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class MainActivity : AppCompatActivity() {
    private val mReference: DatabaseReference = FirebaseDatabase.getInstance().reference.child("votingapp")
    private lateinit var email: TextInputEditText
    private lateinit var password: TextInputEditText
    private lateinit var progressBar:ProgressBar
    private lateinit var SButton: ExtendedFloatingActionButton
    private lateinit var textLinkSignUp:LinearLayout
    private var mAuth: FirebaseAuth? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mAuth = FirebaseAuth.getInstance()
        progressBar = findViewById(R.id.progress)
        email = findViewById(R.id.email)
        password = findViewById(R.id.password)
        SButton = findViewById(R.id.sign_in)
        textLinkSignUp = findViewById(R.id.sign_up_text_link)
        textLinkSignUp.setOnClickListener(View.OnClickListener {
            startActivity(Intent(this@MainActivity,RegisterActivity::class.java))
        })

        SButton.setOnClickListener(View.OnClickListener {
            view -> login(email.text.toString(),password.text.toString())

        })


    }

    private fun login(mail:String , password:String) {

        mAuth?.signInWithEmailAndPassword(mail,password)
                ?.addOnCompleteListener(this, OnCompleteListener {
                    task ->
                    if (task.isSuccessful){
                       // startActivity(Intent(this, AdminDashboard::class.java))
                    }else
                    {
                        //Toast.makeText(this,"not registered", Toast.LENGTH_LONG).show()
                    }

                })?.addOnSuccessListener(this, OnSuccessListener {
                    task->

                 ifAdmin(FirebaseAuth.getInstance().currentUser.uid)



                })


                ?.addOnFailureListener(this, OnFailureListener {
                    exception ->
                    Toast.makeText(this,exception.toString(), Toast.LENGTH_LONG).show()
                })
    }

    fun ifAdmin(currentUsrr:String){
        val currentUsr = currentUsrr
        val ifAdmin = mReference.child("admins").child(currentUsr)
        val eventListener: ValueEventListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    //user is an admin go to adminArea
                    startActivity(Intent(this@MainActivity,AdminDashboard::class.java))


                }else{
                    //user is not admin go to dhasboard
                    startActivity(Intent(this@MainActivity,VoterDashboard::class.java))

                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.d("errors", databaseError.message)
            }
        }
        ifAdmin.addListenerForSingleValueEvent(eventListener)

    }




}