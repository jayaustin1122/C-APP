package com.example.maharlika_app.adapter

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.maharlika_app.databinding.EventItemRowBinding
import com.example.maharlika_app.model.EventModel
import com.google.firebase.database.FirebaseDatabase

class AdminEventAdapter: RecyclerView.Adapter<AdminEventAdapter.ViewHolderEvent> {
    private lateinit var binding : EventItemRowBinding
    private val context : Context
    var eventArrayList : ArrayList<EventModel>

    constructor(context: Context, eventArrayList: ArrayList<EventModel>) : super() {
        this.context = context
        this.eventArrayList = eventArrayList
    }
    inner class ViewHolderEvent(itemView: View): RecyclerView.ViewHolder(itemView){
        var title : TextView = binding.tvTitle
        var description : TextView = binding.tvDesc
        var moreBtn : ImageButton = binding.btnMore


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderEvent {
//binding ui row item event
        binding = EventItemRowBinding.inflate(LayoutInflater.from(context),parent,false)
        return ViewHolderEvent(binding.root)
    }

    override fun getItemCount(): Int {
        return eventArrayList.size
    }

    override fun onBindViewHolder(holder: ViewHolderEvent, position: Int) {
        //get data
        val model = eventArrayList[position]
        val id = model.id
        val eventsTitle = model.eventsTitle
        val eventsDescription = model.eventsDescription
        val uid = model.uid
        val timestamp = model.timestamp



        //set data's
        holder.title.text = eventsTitle
        holder.description.text = eventsDescription

        holder.moreBtn.setOnClickListener {
            moreOptions(model,holder)
        }
    }

    private fun moreOptions(model: EventModel, holder: AdminEventAdapter.ViewHolderEvent) {
//get id title
        val eventId = model.id
        val eventTitle = model.eventsTitle
        val eventdescription = model.eventsDescription
        // show options
        val options = arrayOf("Edit","Delete")
        // show alert dialog
        val  builder = AlertDialog.Builder(context)
        builder.setTitle("Choose Option")
            .setItems(options){dialog,position ->
                //handle item clicked
                if (position == 0 ){
                    //edit btn
                    Toast.makeText(context,"Edit button Clicked", Toast.LENGTH_SHORT).show()

//                    val intent = Intent(context, EditEventActivity::class.java)
//                    intent.putExtra("id", eventId)//id as the reference to edit events
//                    context.startActivity(intent)

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

    private fun deleteEvent(model: EventModel, holder: AdminEventAdapter.ViewHolderEvent) {
        val id = model.id

        val dbRef = FirebaseDatabase.getInstance().getReference("Events")
        dbRef.child(id)
            .removeValue()
            .addOnSuccessListener {
                Toast.makeText(context,"Deleted", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener {e->
                Toast.makeText(context,"Unable to delete due to ${e.message}", Toast.LENGTH_SHORT).show()

            }
    }
}