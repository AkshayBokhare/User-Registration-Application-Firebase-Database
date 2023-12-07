package com.nts.userregistration

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.nts.userregistration.databinding.ActivitySignUpBinding

class SignUpActivity : AppCompatActivity() {

    lateinit var signUpBinding: ActivitySignUpBinding

val auth :FirebaseAuth=FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        signUpBinding = ActivitySignUpBinding.inflate(layoutInflater)
        val view = signUpBinding.root
        setContentView(view)
        supportActionBar?.title = "Create User"

        signUpBinding.buttonSignUpUser.setOnClickListener {

            val userEmail = signUpBinding.editTextEmailSignUp.text.toString()
            val userPassword = signUpBinding.editTextPasswordSingUp.text.toString()
            signupWithFireBase(userEmail,userPassword)
        }

    }

    fun signupWithFireBase(userEmail:String,userPassword:String){

        auth.createUserWithEmailAndPassword(userEmail, userPassword)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                  //  Log.d(TAG, "createUserWithEmail:success")
                    Toast.makeText(applicationContext,
                        "User Account Has Been Created.",
                        Toast.LENGTH_SHORT,
                    ).show()
                    finish()


                } else {
                    // If sign in fails, display a message to the user.
                    Toast.makeText(applicationContext,
                        "User Account Creation Failed .",
                        Toast.LENGTH_SHORT,
                    ).show()
                    finish()

                }
            }

    }
}