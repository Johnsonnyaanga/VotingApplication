package com.example.votingapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.Toast
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth

class RegisterActivity : AppCompatActivity() {
    private lateinit var email: TextInputEditText
    private lateinit var password: TextInputEditText
    private lateinit var progressBar: ProgressBar
    private lateinit var loginTextLink:LinearLayout
    private lateinit var SButton: ExtendedFloatingActionButton
    private var mAuth: FirebaseAuth? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        mAuth = FirebaseAuth.getInstance()
        progressBar = findViewById(R.id.progress)
        email = findViewById(R.id.email)
        password = findViewById(R.id.password)
        SButton = findViewById(R.id.sign_up)
        loginTextLink = findViewById(R.id.sign_in_text_link)

//register user  button click
        SButton.setOnClickListener(View.OnClickListener {
            view -> register(email.text.toString(),password.text.toString())

        })
//link to sign in page
loginTextLink.setOnClickListener(View.OnClickListener {
    startActivity(Intent(this@RegisterActivity,MainActivity::class.java))
})


    }

    private fun register(mail:String , password:String) {

        mAuth?.createUserWithEmailAndPassword(mail,password)
                ?.addOnCompleteListener(this, OnCompleteListener {
                    task ->
                    if (task.isSuccessful){
                        startActivity(Intent(this, MainActivity::class.java))
                    }else
                    {
                        Toast.makeText(this,"error occured, try again", Toast.LENGTH_LONG).show()
                    }

                })?.addOnFailureListener(this, OnFailureListener {
                    exception ->
                    Toast.makeText(this,exception.toString(), Toast.LENGTH_LONG).show()
                })
    }

}