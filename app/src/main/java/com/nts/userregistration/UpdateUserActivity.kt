package com.nts.userregistration

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.nts.userregistration.databinding.ActivityUpdateUserBinding
import com.squareup.picasso.Picasso

class UpdateUserActivity : AppCompatActivity() {

    val database:FirebaseDatabase=FirebaseDatabase.getInstance()
    val myReference : DatabaseReference=database.reference.child("MyUsers")

    lateinit var updateUserBinding: ActivityUpdateUserBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        updateUserBinding=ActivityUpdateUserBinding.inflate(layoutInflater)
        val view =updateUserBinding.root
        setContentView(view)

        supportActionBar?.title="Update User"

        getAndSetData()

        updateUserBinding.buttonUpdateUser.setOnClickListener {
               updateData()
        }

    }

    fun getAndSetData(){
        val name = intent.getStringExtra("name")
        val age = intent.getIntExtra("age",0).toString()
        val email = intent.getStringExtra("email")
        val imageUrl = intent.getStringExtra("imageUrl").toString()

        updateUserBinding.editTextUpdateName.setText(name)
        updateUserBinding.editTextUpdateAge.setText(age)
        updateUserBinding.editTextUpdateEmailSignUp.setText(email)

        Picasso.get().load(imageUrl).into(updateUserBinding.userUpdateProfileImage)

    }

    fun updateData(){

        val updatedName=updateUserBinding.editTextUpdateName.text.toString()
        val updatedAge=updateUserBinding.editTextUpdateAge.text.toString().toInt()
        val updatedEmail=updateUserBinding.editTextUpdateEmailSignUp.text.toString()

        val userId =intent.getStringExtra("id").toString()


        val userMap = mutableMapOf<String,Any>()
        userMap["userId"]= userId
        userMap["userName"]=updatedName
        userMap["userAge"]=updatedAge
        userMap["userEmail"]=updatedEmail

        myReference.child(userId).updateChildren(userMap).addOnCompleteListener{ task ->

            if(task.isSuccessful){
                Toast.makeText(applicationContext
                    , "The User Has Been Updated"
                    ,Toast.LENGTH_SHORT).show()
                finish()
            }
        }
    }

}