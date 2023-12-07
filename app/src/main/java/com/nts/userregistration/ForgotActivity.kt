package com.nts.userregistration

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.nts.userregistration.databinding.ActivityForgotBinding

class ForgotActivity : AppCompatActivity() {

    lateinit var forgotBinding: ActivityForgotBinding
    val auth : FirebaseAuth = FirebaseAuth.getInstance()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        forgotBinding=ActivityForgotBinding.inflate(layoutInflater)
        val view = forgotBinding.root
        setContentView(view)

        forgotBinding.buttonReset.setOnClickListener {

            val email = forgotBinding.editTextReset.text.toString()

            auth.sendPasswordResetEmail(email)
                .addOnCompleteListener{ task ->

                if (task.isSuccessful){
                    Toast.makeText(applicationContext,
                        "Password Reset Link Sent TO Your Register Email Address.",
                        Toast.LENGTH_SHORT,
                    ).show()
                    finish()
                }

                }
        }
    }
}