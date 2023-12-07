package com.nts.userregistration

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.nts.userregistration.databinding.UserIemsBinding
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import java.lang.Exception

class UsersAdapter (var context: Context,
                    var userList:ArrayList<Users>) :RecyclerView.Adapter<UsersAdapter.UserViewHolder>() {


   inner class UserViewHolder(val adapterBinding :UserIemsBinding)
       : RecyclerView.ViewHolder(adapterBinding.root) {}



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {

        val binding = UserIemsBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return UserViewHolder(binding)
    }

    override fun getItemCount(): Int {

        return userList.size

    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {

        holder.adapterBinding.textViewName.text=userList[position].userName
        holder.adapterBinding.textViewAge.text= userList[position].userAge.toString()
        holder.adapterBinding.textViewEmail.text= userList[position].userEmail

        val imageUrl = userList[position].url

        Picasso.get().load(imageUrl).into(holder.adapterBinding.imageView,object : Callback {

            override fun onSuccess() {
                holder.adapterBinding.progressBar.visibility = View.INVISIBLE
            }
            override fun onError(e: Exception?) {
                Toast.makeText(context,e?.localizedMessage, Toast.LENGTH_SHORT).show()
            }

        })
        holder.adapterBinding.linearLayout.setOnClickListener{
            val intent= Intent(context,UpdateUserActivity::class.java)
            intent.putExtra("id",userList[position].userId)
            intent.putExtra("name",userList[position].userName)
            intent.putExtra("age",userList[position].userAge)
            intent.putExtra("email",userList[position].userEmail)
            intent.putExtra("imageUrl",imageUrl)
            intent.putExtra("imageName",userList[position].imageName)

            context.startActivity(intent)
        }
    }

    fun getUserId(position: Int):String{

        return userList[position].userId
    }

}
