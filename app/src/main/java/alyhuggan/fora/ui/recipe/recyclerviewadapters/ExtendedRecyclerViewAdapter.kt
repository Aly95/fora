package alyhuggan.fora.ui.recipe.recyclerviewadapters

import alyhuggan.fora.R
import alyhuggan.fora.repository.objects.foods.FoodItem
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.items_extended_view.view.*

class RecyclerViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    val ingredients: TextView = view.extended_view_ingredients
}

class ExtendedRecyclerViewAdapter(private val foodList: List<FoodItem>) :
    RecyclerView.Adapter<RecyclerViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.items_extended_view, parent, false)
        return RecyclerViewHolder(view)
    }

    override fun getItemCount() = foodList.size

    override fun onBindViewHolder(holder: RecyclerViewHolder, position: Int) {

        val ingredients = holder.ingredients

        val foodListPosition = foodList[position]
        val quantity = foodListPosition.quantity

        val name = foodListPosition.name
        val quantityAmount = quantity!!.quantity
        val quantityType = quantity.quantityType

        ingredients.text = "$quantityAmount $quantityType $name"
    }
}