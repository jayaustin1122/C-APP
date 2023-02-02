package com.example.maharlika_app.user.news

import android.widget.Filter
import com.example.maharlika.ui.admin.events.ModelEvent
import com.example.maharlika_app.admin.news.ModelNews


class FilterNews:Filter {

    //arraylist to use search
    private var filterListNews : ArrayList<ModelNews>

    //adapter
    private var adapterNews : UserNewsAdapter

    constructor(filterListEvent: ArrayList<ModelNews>, adapterEvents: UserNewsAdapter) {
        this.filterListNews = filterListEvent
        this.adapterNews = adapterEvents
    }
    override fun performFiltering(constraint: CharSequence?): Filter.FilterResults {
        var constraint = constraint
        var results  = Filter.FilterResults()


        //value not null or empty
        if (constraint != null && constraint.isNotEmpty()){
            //if search value is null not empty
            //handle case sensitivity
            constraint = constraint.toString().uppercase()
            val filteredModels : ArrayList<ModelNews> = ArrayList()

            for (i in 0 until filterListNews.size){
                //validate
                if (filterListNews[i].newsTitle.uppercase().contains(constraint) ){
                    //add to filtered list

                    filteredModels.add(filterListNews[i])
                }
            }
            results.count = filteredModels.size
            results.values = filteredModels

        }
        else{
            //search value is either empty or null
            results.count = filterListNews.size
            results.values = filterListNews
        }
        return results
    }
    override fun publishResults(constrains : CharSequence?, results : FilterResults?) {
        //apply filter changes
        adapterNews.newsArrayList = results?.values as ArrayList<ModelNews>

        //notify changes
        adapterNews.notifyDataSetChanged()
    }
}