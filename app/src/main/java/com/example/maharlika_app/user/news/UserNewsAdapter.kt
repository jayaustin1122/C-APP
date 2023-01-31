package com.example.maharlika_app.user.news

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

import com.example.maharlika_app.admin.news.ModelNews
import com.example.maharlika_app.databinding.UserEventRowBinding
import com.example.maharlika_app.databinding.UserNewsRowBinding
import com.example.maharlika_app.user.events.EventDetailActivity


class UserNewsAdapter : RecyclerView.Adapter<UserNewsAdapter.ViewHolderUserNews> {
    private lateinit var binding : UserNewsRowBinding
    private val context : Context
    public var newsArrayList : ArrayList<ModelNews>


    constructor(context: Context, eventArrayList: ArrayList<ModelNews>) : super() {
        this.context = context
        this.newsArrayList = eventArrayList
    }
    inner class ViewHolderUserNews(itemView: View): RecyclerView.ViewHolder(itemView){
        var title : TextView = binding.newsTitle
        var coverImage : ImageView = binding.newsCover
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderUserNews {
        //binding ui row item event
        binding = UserNewsRowBinding.inflate(LayoutInflater.from(context),parent,false)
        return ViewHolderUserNews(binding.root)
    }

    override fun getItemCount(): Int {
        return newsArrayList.size
    }

    override fun onBindViewHolder(holder: ViewHolderUserNews, position: Int) {
        val model = newsArrayList[position]
        val id = model.id
        val title = model.newsTitle
        val image = model.image

        //set data's
        holder.title.text = title
        Glide.with(this@UserNewsAdapter.context)
            .load(image)
            .into(holder.coverImage)
        holder.itemView.setOnClickListener {
            val intent = Intent(context, NewsDetailsActivity::class.java)
            intent.putExtra("id",id)//reference to load the other details
            context.startActivity(intent)
        }
    }

}