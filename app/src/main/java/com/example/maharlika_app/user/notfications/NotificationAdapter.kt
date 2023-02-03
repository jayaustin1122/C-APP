package com.example.maharlika_app.user.notfications

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.maharlika_app.admin.news.ModelNews
import com.example.maharlika_app.databinding.EventItemRowBinding
import com.example.maharlika_app.databinding.NotificationRowBinding
import com.example.maharlika_app.user.news.NewsDetailsActivity

class NotificationAdapter: RecyclerView.Adapter<NotificationAdapter.ViewHolderNotifications> {
    private lateinit var binding :  NotificationRowBinding
    private val context : Context
    public var notifArrayList : ArrayList<ModelNotif>

    constructor(context: Context, notifArrayList: ArrayList<ModelNotif>) : super() {
        this.context = context
        this.notifArrayList = notifArrayList
    }


    inner class ViewHolderNotifications(itemView: View): RecyclerView.ViewHolder(itemView){
        var title : TextView = binding.tvNotifTitle
        var image : ImageView = binding.imgNotif
        var currentDate : TextView = binding.tvDatePosted

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderNotifications {
        //binding ui row item event
        binding = NotificationRowBinding.inflate(LayoutInflater.from(context),parent,false)
        return ViewHolderNotifications(binding.root)
    }

    override fun getItemCount(): Int {
        return notifArrayList.size
    }

    override fun onBindViewHolder(holder: ViewHolderNotifications, position: Int) {
        //get data
        val model = notifArrayList[position]
        val id = model.id
        val notifTitle = model.NotifTitle
        val imageselected = model.imageNotif
        val currentDate = model.currentDateNotif

        //set data's
        holder.title.text = notifTitle
        holder.currentDate.text = currentDate

        Glide.with(this@NotificationAdapter.context)
            .load(imageselected)
            .into(holder.image)

        holder.itemView.setOnClickListener {
            val intent = Intent(context, NotificationDetailsActivity::class.java)
            intent.putExtra("id",id)//reference to load the other details
            context.startActivity(intent)
        }
    }
}