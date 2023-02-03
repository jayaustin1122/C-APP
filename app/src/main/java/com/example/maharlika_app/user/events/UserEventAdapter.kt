package com.example.maharlika_app.user.events

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.maharlika.ui.admin.events.ModelEvent
import com.example.maharlika_app.databinding.EventItemRowBinding
import com.example.maharlika_app.databinding.UserEventRowBinding

class UserEventAdapter : RecyclerView.Adapter<UserEventAdapter.ViewHolderUserEvent>,Filterable {
    private lateinit var binding : UserEventRowBinding
    private val context : Context
    public var eventArrayList : ArrayList<ModelEvent>
    private var filterListEvent : ArrayList<ModelEvent>
    private var filter : FilterEvents? = null

    constructor(context: Context, eventArrayList: ArrayList<ModelEvent>){
        this.context = context
        this.eventArrayList = eventArrayList
        this.filterListEvent = eventArrayList
    }


    inner class ViewHolderUserEvent(itemView: View): RecyclerView.ViewHolder(itemView){
        var title : TextView = binding.eventTitle
        var coverImage : ImageView = binding.eventCover

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderUserEvent {
        //binding ui row item event
        binding = UserEventRowBinding.inflate(LayoutInflater.from(context),parent,false)
        return ViewHolderUserEvent(binding.root)
    }

    override fun getItemCount(): Int {
        return eventArrayList.size
    }

    override fun onBindViewHolder(holder: ViewHolderUserEvent, position: Int) {

        val model = eventArrayList[position]
        val id = model.id
        val title = model.eventsTitle
        val image = model.image

        //set data's
        holder.title.text = title
        Glide.with(this@UserEventAdapter.context)
            .load(image)
            .into(holder.coverImage)
        holder.itemView.setOnClickListener {
            val intent = Intent(context, EventDetailActivity::class.java)
            intent.putExtra("id",id)//reference to load the other details
            context.startActivity(intent)
        }

    }
    override fun getFilter(): Filter {
        if (filter == null){
            filter = FilterEvents(filterListEvent,this)
        }
        return filter as FilterEvents
    }
}