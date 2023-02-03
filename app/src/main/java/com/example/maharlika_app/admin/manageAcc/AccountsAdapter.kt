package com.example.maharlika_app.admin.manageAcc

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.maharlika_app.admin.news.EditNewsActivity
import com.example.maharlika_app.admin.news.ModelNews
import com.example.maharlika_app.admin.news.NewsAdapter
import com.example.maharlika_app.databinding.AccountRowItemBinding
import com.example.maharlika_app.databinding.EventItemRowBinding
import com.example.maharlika_app.model.UserModel
import com.google.firebase.database.FirebaseDatabase

class AccountsAdapter: RecyclerView.Adapter<AccountsAdapter.ViewHolderAccounts> {

    private lateinit var binding : AccountRowItemBinding
    private val context : Context
    var accountsArrayList : ArrayList<UserModel>

    constructor(context: Context, accountsArrayList: ArrayList<UserModel>){
        this.context = context
        this.accountsArrayList = accountsArrayList
    }
    inner class ViewHolderAccounts(itemView: View): RecyclerView.ViewHolder(itemView){
        var name : TextView = binding.tvName
        var moreBtn : ImageButton = binding.btnMore
        var image : ImageView = binding.imgPicture
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderAccounts {
        //binding ui row item event
        binding = AccountRowItemBinding.inflate(LayoutInflater.from(context),parent,false)
        return ViewHolderAccounts(binding.root)
    }

    override fun getItemCount(): Int {
        return accountsArrayList.size
    }

    override fun onBindViewHolder(holder: ViewHolderAccounts, position: Int) {
        //get data
        val model = accountsArrayList[position]
        val fullname = model.fullName
        val password = model.password
        val imageselected = model.image

        holder.name.text = fullname
        Glide.with(this@AccountsAdapter.context)
            .load(imageselected)
            .into(holder.image)

        holder.moreBtn.setOnClickListener {
            moreOptions(model,holder)
        }

    }
    private fun moreOptions(model: UserModel, holder: AccountsAdapter.ViewHolderAccounts) {
        //get id title
        val accId = model.id
        val fullname = model.fullName
        val pass = model.password
        val image = model.image
        // show options
        val options = arrayOf("Edit","Delete")
        // show alert dialog
        val  builder = AlertDialog.Builder(context)
        builder.setTitle("Choose Option")
            .setItems(options){dialog,position ->
                //handle item clicked
                if (position == 0 ){
                    //edit btn
                    val intent = Intent(context, EditAccountsActivity::class.java)
                    intent.putExtra("id", accId)
                    intent.putExtra("fullname", fullname)
                    intent.putExtra("pass", pass)
                    intent.putExtra("image", image)
                    //id as the reference to edit events
                    context.startActivity(intent)

                }
                else if (position == 1){
                    //delete btn
                    //dialog
                    val builder = AlertDialog.Builder(context)
                    builder.setTitle("Delete")
                        .setMessage("Are you sure you want to delete this Account?")
                        .setPositiveButton("Confirm"){a,d->
                            Toast.makeText(context,"Account Deleted", Toast.LENGTH_SHORT).show()

                            deleteEvent(model,holder)
                        }
                        .setNegativeButton("Cancel"){a,d->
                            a.dismiss()
                        }
                        .show()
                }
            }
            .show()

    }

    private fun deleteEvent(model: UserModel, holder: AccountsAdapter.ViewHolderAccounts) {
        //id as the reference to delete

        val id = model.uid

        val dbRef = FirebaseDatabase.getInstance().getReference("Users")
        dbRef.child(id.toString())
            .removeValue()
            .addOnSuccessListener {
                Toast.makeText(context,"Deleted", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener {e->
                Toast.makeText(context,"Unable to delete due to ${e.message}", Toast.LENGTH_SHORT).show()

            }
    }
}