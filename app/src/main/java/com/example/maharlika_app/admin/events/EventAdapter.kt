package com.example.maharlika.ui.admin.events

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.maharlika_app.admin.AdminHolderActivity
import com.example.maharlika_app.admin.events.EditEventActivity
import com.example.maharlika_app.databinding.EventItemRowBinding
import com.example.maharlika_app.user.events.EventDetailActivity
import com.google.firebase.database.FirebaseDatabase


class EventAdapter : RecyclerView.Adapter<EventAdapter.ViewHolderEvent>,Filterable{
    private lateinit var binding : EventItemRowBinding
    private val context : Context
    public var eventArrayList : ArrayList<ModelEvent>
    private var filterListEvent : ArrayList<ModelEvent>
    private var filter : FilterEvents? = null


    //constructor
    constructor(context: Context, eventArrayList: ArrayList<ModelEvent>) {
        this.context = context
        this.eventArrayList = eventArrayList
        this.filterListEvent = eventArrayList
    }

    //inner class to hold ui in row item event
    inner class ViewHolderEvent(itemView: View): RecyclerView.ViewHolder(itemView){
        var title : TextView = binding.tvTitle
        var description : TextView = binding.tvDesc
        var moreBtn : ImageButton = binding.btnMore
        var image : ImageView = binding.imgPicture
        var currentDate : TextView = binding.textViewNoteDate
        var currentTime : TextView = binding.textViewNoteTime

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderEvent {
        //binding ui row item event
        binding = EventItemRowBinding.inflate(LayoutInflater.from(context),parent,false)
        return ViewHolderEvent(binding.root)
    }

    override fun onBindViewHolder(holder: ViewHolderEvent, position: Int) {
        //get data 
        val model = eventArrayList[position]
        val id = model.id
        val eventsTitle = model.eventsTitle
        val eventsDescription = model.eventsDescription
        val uid = model.uid
        val timestamp = model.timestamp
        val imageselected = model.image
        val currentDate = model.currentDate
        val currentTime = model.currentTime



        //set data's
        holder.title.text = eventsTitle
        holder.description.text = eventsDescription
        holder.currentTime.text = currentTime
        holder.currentDate.text = currentDate

        Glide.with(this@EventAdapter.context)
            .load(imageselected)
            .into(holder.image)


        holder.moreBtn.setOnClickListener {
            moreOptions(model,holder)
        }
        holder.itemView.setOnClickListener {
            val intent = Intent(context, EventDetailActivity::class.java)
            intent.putExtra("id",id)//reference to load the other details
            context.startActivity(intent)
        }
    }

    private fun moreOptions(model: ModelEvent, holder: EventAdapter.ViewHolderEvent) {
        //get id title
        val eventId = model.id
        val eventTitle = model.eventsTitle
        val eventdescription = model.eventsDescription
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
                    val intent = Intent(context, EditEventActivity::class.java)
                    intent.putExtra("id", eventId)
                    intent.putExtra("eventTitle", eventTitle)
                    intent.putExtra("eventdescription", eventdescription)
                    intent.putExtra("image", image)
                    //id as the reference to edit events
                    context.startActivity(intent)

                }
                else if (position == 1){
                    //delete btn
                    //dialog
                    val builder = AlertDialog.Builder(context)
                    builder.setTitle("Delete")
                        .setMessage("Are you sure you want to delete this event?")
                        .setPositiveButton("Confirm"){a,d->
                            Toast.makeText(context,"Deleting...", Toast.LENGTH_SHORT).show()

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

    private fun deleteEvent(model: ModelEvent, holder: ViewHolderEvent) {
        //id as the reference to delete

        val id = model.id

        val dbRef = FirebaseDatabase.getInstance().getReference("events")
        dbRef.child(id.toString())
            .removeValue()
            .addOnSuccessListener {
                Toast.makeText(context,"Deleted", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener {e->
                Toast.makeText(context,"Unable to delete due to ${e.message}", Toast.LENGTH_SHORT).show()

            }

    }

    override fun getItemCount(): Int {
        return eventArrayList.size
    }

    override fun getFilter(): Filter {
        if (filter == null){
            filter = FilterEvents(filterListEvent,this)
        }
        return filter as FilterEvents
    }
}