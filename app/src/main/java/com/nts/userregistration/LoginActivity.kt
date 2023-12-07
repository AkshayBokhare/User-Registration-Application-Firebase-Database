package com.nts.userregistration

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.nts.userregistration.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {

    lateinit var loginBinding: ActivityLoginBinding
    val auth : FirebaseAuth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        loginBinding=ActivityLoginBinding.inflate(layoutInflater)
        val view=loginBinding.root
        setContentView(view)
        supportActionBar?.title=" Login User"

        loginBinding.buttonSignin.setOnClickListener {

            val userEmail = loginBinding.editTextEmailSignUp.text.toString()
            val userPassword =loginBinding.editTextPasswordSingUp.text.toString()
            signinWithFirebase(userEmail,userPassword)

        }

        loginBinding.buttonSignUpUser.setOnClickListener {
            val intent =Intent(this@LoginActivity,SignUpActivity::class.java)
            startActivity(intent)

        }

        loginBinding.buttonForgot.setOnClickListener {
            val intent =Intent(this@LoginActivity,ForgotActivity::class.java)
            startActivity(intent)
        }

        loginBinding.buttonSignInWithPhoneNumber.setOnClickListener{
            val intent =Intent(this@LoginActivity,PhoneActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    fun signinWithFirebase(userEmail:String,userPassword:String){
        auth.signInWithEmailAndPassword(userEmail, userPassword)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Toast.makeText(applicationContext,
                        "User Login Is Successful.",
                        Toast.LENGTH_SHORT,
                    ).show()

                    val intent=Intent(this@LoginActivity,MainActivity::class.java)
                    startActivity(intent)
                    finish()

                } else {
                    // If sign in fails, display a message to the user.
                    Toast.makeText(applicationContext,
                        "User Login Faild .",
                        Toast.LENGTH_SHORT,
                    ).show()
                    finish()
                }
            }
    }

    override fun onStart() {
        super.onStart()
        val user=auth.currentUser
        if(user!=null){
            val intent=Intent(this@LoginActivity,MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}