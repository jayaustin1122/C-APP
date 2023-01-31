package com.example.maharlika_app.admin.news

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
import com.example.maharlika.ui.admin.events.EventAdapter
import com.example.maharlika.ui.admin.events.ModelEvent
import com.example.maharlika_app.admin.events.EditEventActivity
import com.example.maharlika_app.databinding.EventItemRowBinding
import com.example.maharlika_app.user.news.NewsDetailsActivity
import com.google.firebase.database.FirebaseDatabase

class NewsAdapter: RecyclerView.Adapter<NewsAdapter.ViewHolderNews> {
    private lateinit var binding : EventItemRowBinding
    private val context : Context
    public var newsArrayList : ArrayList<ModelNews>

    constructor(context: Context, newsArrayList: ArrayList<ModelNews>){
        this.context = context
        this.newsArrayList = newsArrayList
    }

    inner class ViewHolderNews(itemView: View): RecyclerView.ViewHolder(itemView){
        var title : TextView = binding.tvTitle
        var description : TextView = binding.tvDesc
        var moreBtn : ImageButton = binding.btnMore
        var image : ImageView = binding.imgPicture
        var currentDate : TextView = binding.textViewNoteDate
        var currentTime : TextView = binding.textViewNoteTime

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderNews {
//binding ui row item event
        binding = EventItemRowBinding.inflate(LayoutInflater.from(context),parent,false)
        return ViewHolderNews(binding.root)
    }

    override fun getItemCount(): Int {
        return newsArrayList.size
    }

    override fun onBindViewHolder(holder: ViewHolderNews, position: Int) {
        //get data
        val model = newsArrayList[position]
        val id = model.id
        val newsTitle = model.newsTitle
        val newsDescription = model.newsDescription
        val uid = model.uid
        val timestamp = model.timestamp
        val imageselected = model.image
        val currentDate = model.currentDate
        val currentTime = model.currentTime
        //set data's
        holder.title.text = newsTitle
        holder.description.text = newsDescription
        holder.currentTime.text = currentTime
        holder.currentDate.text = currentDate

        Glide.with(this@NewsAdapter.context)
            .load(imageselected)
            .into(holder.image)


        holder.moreBtn.setOnClickListener {
            moreOptions(model,holder)
        }
        holder.itemView.setOnClickListener {
            val intent = Intent(context, NewsDetailsActivity::class.java)
            intent.putExtra("id",id)//reference to load the other details
            context.startActivity(intent)
        }
    }
    private fun moreOptions(model: ModelNews, holder: NewsAdapter.ViewHolderNews) {
        //get id title
        val newsId = model.id
        val newsTitle = model.newsTitle
        val newsDescription = model.newsDescription
        // show options
        val options = arrayOf("Edit","Delete")
        // show alert dialog
        val  builder = AlertDialog.Builder(context)
        builder.setTitle("Choose Option")
            .setItems(options){dialog,position ->
                //handle item clicked
                if (position == 0 ){
                    //edit btn
                    val intent = Intent(context, EditNewsActivity::class.java)
                    intent.putExtra("id", newsId)
                    intent.putExtra("newsTitle", newsTitle)
                    intent.putExtra("newsDescription", newsDescription)
                    //id as the reference to edit events
                    context.startActivity(intent)

                }
                else if (position == 1){
                    //delete btn
                    //dialog
                    val builder = AlertDialog.Builder(context)
                    builder.setTitle("Delete")
                        .setMessage("Are you sure you want to delete this News?")
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

    private fun deleteEvent(model: ModelNews, holder: NewsAdapter.ViewHolderNews) {
        //id as the reference to delete

        val id = model.id

        val dbRef = FirebaseDatabase.getInstance().getReference("news")
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