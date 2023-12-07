package com.nts.userregistration

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import com.nts.userregistration.databinding.ActivityPhoneBinding
import java.util.concurrent.TimeUnit

class PhoneActivity : AppCompatActivity() {

    val auth: FirebaseAuth = FirebaseAuth.getInstance()
    lateinit var mCallbacks: PhoneAuthProvider.OnVerificationStateChangedCallbacks

    var verificationCode = " "

    lateinit var phoneBinding: ActivityPhoneBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        phoneBinding = ActivityPhoneBinding.inflate(layoutInflater)
        val view = phoneBinding.root
        setContentView(view)
        supportActionBar?.title = "Login With Phone"
        phoneBinding.buttonSendSMSCode.setOnClickListener {

            val userPhoneNumber = phoneBinding.editTextPhoneNumber.text.toString()
            val option = PhoneAuthOptions.newBuilder(auth)
                .setPhoneNumber(userPhoneNumber)
                .setTimeout(60L, TimeUnit.SECONDS)
                .setActivity(this@PhoneActivity)
                .setCallbacks(mCallbacks)
                .build()

            PhoneAuthProvider.verifyPhoneNumber(option)
        }

        phoneBinding.buttonVerify.setOnClickListener {

            signInWithCode()

        }

        mCallbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            override fun onVerificationCompleted(p0: PhoneAuthCredential) {
                TODO("Not yet implemented")
            }

            override fun onVerificationFailed(p0: FirebaseException) {
                TODO("Not yet implemented")
            }

            override fun onCodeSent(p0: String, p1: PhoneAuthProvider.ForceResendingToken) {
                super.onCodeSent(p0, p1)

                verificationCode = p0

            }
        }
    }

    fun signInWithCode() {
        val userEnterCode = phoneBinding.editTextVerificationCode.text.toString()
        val credential = PhoneAuthProvider.getCredential(verificationCode, userEnterCode)
        signInWithPhoneAuthCredential(credential)
    }

    fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
        auth.signInWithCredential(credential).addOnCompleteListener { task ->

            if (task.isSuccessful) {

                val intent = Intent(this@PhoneActivity, MainActivity::class.java)
                startActivity(intent)
                finish()

            } else {

                Toast.makeText(applicationContext, "Invalid Code", Toast.LENGTH_SHORT).show()

            }

        }

    }
}