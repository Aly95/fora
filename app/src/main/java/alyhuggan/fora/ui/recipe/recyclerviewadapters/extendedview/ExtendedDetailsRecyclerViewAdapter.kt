package alyhuggan.fora.ui.recipe.recyclerviewadapters.extendedview

import alyhuggan.fora.R
import alyhuggan.fora.repository.objects.foods.FoodItem
import alyhuggan.fora.repository.objects.recipe.Details
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.storage.StorageReference
import kotlinx.android.synthetic.main.item_details.view.*
import kotlinx.android.synthetic.main.items_extended_view.view.*

class DetailsRecyclerViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    val rating: TextView = view.details_rating
    val ratingCount: TextView = view.details_rating_count
    val createdBy: TextView = view.details_created_by
    val createdOn: TextView = view.details_created_on
    val favourite: TextView = view.details_favourite
}

class ExtendedDetailsRecyclerViewAdapter(private val details: Details) :
    RecyclerView.Adapter<DetailsRecyclerViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DetailsRecyclerViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_details, parent, false)
        return DetailsRecyclerViewHolder(
            view
        )
    }

    override fun getItemCount() = 1

    override fun onBindViewHolder(holder: DetailsRecyclerViewHolder, position: Int) {

        val rating = holder.rating
        val ratingCount = holder.ratingCount
        val createdBy = holder.createdBy
        val createdOn = holder.createdOn
        val favourite = holder.favourite

        rating.text = "Rated: ${details.rating.toString()}"
        ratingCount.text = "Rated by: ${details.ratingsCount} users"
        createdBy.text = "Created by: ${details.created.createdBy}"
        createdOn.text = "Created on: ${details.created.createdOn}"
        favourite.text = "Favourited by: ${details.favourites} users"

    }
}