package alyhuggan.fora.ui.recipe.recyclerviewadapters.extendedview

import alyhuggan.fora.R
import alyhuggan.fora.repository.objects.foods.FoodItem
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.storage.StorageReference
import kotlinx.android.synthetic.main.items_card.view.*
import kotlinx.android.synthetic.main.items_extended_view.view.*

private lateinit var storageRef: StorageReference

class RecyclerViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    val ingredients: TextView = view.extended_view_textview
    val image: ImageView = view.extended_view_bullet
    val count: TextView = view.extended_view_count
}

class ExtendedRecyclerViewAdapter(private val foodList: List<FoodItem>) :
    RecyclerView.Adapter<RecyclerViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.items_extended_view, parent, false)
        return RecyclerViewHolder(
            view
        )
    }

    override fun getItemCount() = foodList.size

    override fun onBindViewHolder(holder: RecyclerViewHolder, position: Int) {

        val ingredients = holder.ingredients
        val imageView = holder.image
        val listCount = holder.count

        imageView.setImageResource(R.drawable.bullet)
        listCount.text = (position+1).toString()

        val foodListPosition = foodList[position]
        val quantity = foodListPosition.quantity

        val name = "${foodListPosition.brand} ${foodListPosition.name}"
        val quantityAmount = quantity!!.quantity
        val quantityType = quantity.quantityType

        ingredients.text = "$quantityAmount $quantityType $name"
    }
}