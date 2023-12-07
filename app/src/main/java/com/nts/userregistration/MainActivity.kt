package com.nts.userregistration

import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.nts.userregistration.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    lateinit var mainBinding: ActivityMainBinding

    val database: FirebaseDatabase = FirebaseDatabase.getInstance()
    val myReference: DatabaseReference = database.reference.child("MyUsers")

    val userList = ArrayList<Users>()
    lateinit var usersAdapter: UsersAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainBinding = ActivityMainBinding.inflate(layoutInflater)
        val view = mainBinding.root
        setContentView(view)

        mainBinding.floatingActionButton.setOnClickListener {

            val intent = Intent(this, AddUserActivity::class.java)
            startActivity(intent)
        }

        //Deleting Data
        ItemTouchHelper(object :ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT){
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder,
            ): Boolean {

                TODO("Not yet impliment")

            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val id = usersAdapter.getUserId(viewHolder.adapterPosition)

                myReference.child(id).removeValue()

                Toast.makeText(applicationContext
                    , "The User Has Been Deleted"
                    , Toast.LENGTH_SHORT).show()
            }

        }).attachToRecyclerView(mainBinding.recyclerView)

        retrieveDataFromDatabase()

    }

    fun retrieveDataFromDatabase() {

//        ChildEventListener

        myReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {

                userList.clear()


                for (eachuser in snapshot.children) {

                    val user = eachuser.getValue(Users::class.java)

                    if (user != null) {
                        println("userId: ${user.userId}")
                        println("userName: ${user.userName}")
                        println("userAge: ${user.userAge}")
                        println("userEmail: ${user.userEmail}")
                        println("*******************************")

                        userList.add(user)

                    }
                    usersAdapter = UsersAdapter(this@MainActivity, userList)
                    mainBinding.recyclerView.layoutManager = LinearLayoutManager(this@MainActivity)
                    mainBinding.recyclerView.adapter = usersAdapter
                }
            }
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {

        menuInflater.inflate(R.menu.menu_delete_all,menu)

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId==R.id.deleteAll){
            showDialogMsg()
        }
        //SignOut Function
        else if ( item.itemId==R.id.signOut){

            FirebaseAuth.getInstance().signOut()
            val intent=Intent(this@MainActivity,LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
        return super.onOptionsItemSelected(item)
    }

  fun showDialogMsg(){
      val dialogMessage =AlertDialog.Builder(this)
      dialogMessage.setTitle("Delete All Users")
      dialogMessage.setMessage("If Click Yes , All Will be Deleted ,"
               +"If You Want To Delete Specific User, you can swip the item you want to delete right or left ")
      dialogMessage.setNegativeButton("Cancel",DialogInterface.OnClickListener { dialogInterface, which ->

         dialogInterface.cancel()
      })
      dialogMessage.setPositiveButton("Yes",DialogInterface.OnClickListener { dialogInterface, which ->
          myReference.removeValue().addOnCompleteListener { task ->
              if (task.isSuccessful) {

                  usersAdapter.notifyDataSetChanged()

                  Toast.makeText(applicationContext, "All user Deleted", Toast.LENGTH_SHORT).show()
              }
          }
      })

      dialogMessage.create().show()
  }
}