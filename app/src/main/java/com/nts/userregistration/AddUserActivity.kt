package com.nts.userregistration

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.nts.userregistration.databinding.ActivityAddUserBinding
import com.squareup.picasso.Picasso
import java.util.UUID

class AddUserActivity : AppCompatActivity() {

    lateinit var addUserBinding: ActivityAddUserBinding

    //Acsses FireBase database
    val database: FirebaseDatabase = FirebaseDatabase.getInstance()
    val myReference: DatabaseReference = database.reference.child("MyUsers")
    lateinit var activityResultLauncher: ActivityResultLauncher<Intent>
    var imageUri: Uri? = null
    //Fire base Storage
    val firebaseStorage: FirebaseStorage = FirebaseStorage.getInstance()
    // Create a storage reference from our app
    var storageReference: StorageReference = firebaseStorage.reference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        addUserBinding = ActivityAddUserBinding.inflate(layoutInflater)
        val view = addUserBinding.root
        setContentView(view)

        supportActionBar?.title = "Add User"

        //Register
        registerActivityForResult()

        addUserBinding.buttonAddUser.setOnClickListener {

            uploadPhoto()

        }

        addUserBinding.userProfileImage.setOnClickListener {

            chooseImage()
        }

    }


    fun chooseImage() {

        if (ContextCompat.checkSelfPermission(
                this, Manifest.permission.READ_EXTERNAL_STORAGE
            )
            != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), 1
            )

        } else {

            val intent = Intent()
            intent.type = "image/*"
            intent.action = Intent.ACTION_GET_CONTENT

            activityResultLauncher.launch(intent)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray,
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 1 && grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

            val intent = Intent()
            intent.type = "image/*"
            intent.action = Intent.ACTION_GET_CONTENT

            activityResultLauncher.launch(intent)

        } else {

            Toast.makeText(
                this,
                " Exeption ",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    fun registerActivityForResult() {

        activityResultLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult(),
            { result ->
                val requestCode = result.resultCode
                val imageData = result.data
                if (requestCode == RESULT_OK && imageData != null) {

                    imageUri = imageData.data

                    //Picasso liabrary to show any image in image wiew
                    imageUri.let {
                        Picasso.get().load(it).into(addUserBinding.userProfileImage)
                    }
                }
            })
    }

    fun addUserToDatabase(url : String,imageName:String) {

        val name: String = addUserBinding.editTextName.text.toString()

        val age: Int = addUserBinding.editTextAge.text.toString().toInt()

        // val age: Int = addUserBinding.editTextAge.toString().toInt()
        val email: String = addUserBinding.editTextEmailSignUp.text.toString()

        val id: String = myReference.push().key.toString()

        val user = Users(id, name, age, email,url,imageName)

        myReference.child(id).setValue(user).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Toast.makeText(
                    applicationContext,
                    "The New User Has Been Added To The Database",
                    Toast.LENGTH_SHORT).show()

                addUserBinding.buttonAddUser.isClickable = true
                addUserBinding.progressBarAddUser.visibility = View.INVISIBLE

                finish()

            } else {
                Toast.makeText(
                    this, task.exception.toString(), Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun uploadPhoto() {

        addUserBinding.buttonAddUser.isClickable = false
        addUserBinding.progressBarAddUser.visibility = View.INVISIBLE

        //UUID
        val imageName = UUID.randomUUID().toString()  //println("RANDOM NUMBER FOR ID : $imageName")
        val imageReference = storageReference.child("images").child(imageName)

        imageUri?.let { uri ->

            imageReference.putFile(uri).addOnSuccessListener {
                Toast.makeText(applicationContext,"Image Upload Succesfuly", Toast.LENGTH_SHORT).show()


                val myUploadedImageReference = storageReference.child("images").child(imageName)
                myUploadedImageReference.downloadUrl.addOnSuccessListener { url ->
                    val imageURL = url.toString()
                    addUserToDatabase(imageURL,imageName)
                }

                 finish()
            }.addOnFailureListener {
                Toast.makeText(applicationContext, it.localizedMessage, Toast.LENGTH_SHORT).show()
            }

        }

    }
}