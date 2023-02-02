package com.example.maharlika_app.user.events

import android.widget.Filter
import com.example.maharlika.ui.admin.events.ModelEvent

class FilterEvents:Filter {

    //arraylist to use search
    private var filterListEvent : ArrayList<ModelEvent>

    //adapter
    private var adapterEvents : UserEventAdapter

    // constructor
    constructor(filterListEvent: ArrayList<ModelEvent>, adapterEvents: UserEventAdapter) : super() {
        this.filterListEvent = filterListEvent
        this.adapterEvents = adapterEvents
    }

    override fun performFiltering(constraint: CharSequence?): FilterResults {
        var constraint = constraint
        var results  = FilterResults()


        //value not null or empty
        if (constraint != null && constraint.isNotEmpty()){
            //if search value is null not empty
            //handle case sensitivity
            constraint = constraint.toString().uppercase()
            val filteredModels : ArrayList<ModelEvent> = ArrayList()

            for (i in 0 until filterListEvent.size){
                //validate
                if (filterListEvent[i].eventsTitle.uppercase().contains(constraint) ){
                    //add to filtered list
                    
                    filteredModels.add(filterListEvent[i])
                }
            }
            results.count = filteredModels.size
            results.values = filteredModels

        }
        else{
            //search value is either empty or null
            results.count = filterListEvent.size
            results.values = filterListEvent
        }
        return results
    }

    override fun publishResults(constrains : CharSequence?, results : FilterResults?) {
        //apply filter changes
        adapterEvents.eventArrayList = results?.values as ArrayList<ModelEvent>

        //notify changes
        adapterEvents.notifyDataSetChanged()
    }
}