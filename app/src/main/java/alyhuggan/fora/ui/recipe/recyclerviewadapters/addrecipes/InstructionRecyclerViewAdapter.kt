package alyhuggan.fora.ui.recipe.recyclerviewadapters.addrecipes

import alyhuggan.fora.R
import alyhuggan.fora.repository.objects.foods.FoodItem
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class AddInstructionRecyclerViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    val ingredients: TextView = view.findViewById(R.id.ingredient_txt)
    val ingredientsImage: ImageView = view.findViewById<ImageView>(R.id.ingredient_bullet)
    val count: TextView = view.findViewById(R.id.ingredient_count)
}

class InstructionRecyclerViewAdapter(private val list: List<String>) :
    RecyclerView.Adapter<AddRecipeRecyclerViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AddRecipeRecyclerViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_add_ingredient, parent, false)
        return AddRecipeRecyclerViewHolder(
            view
        )
    }

    override fun getItemCount() = list.size

    fun getList() = list

    override fun onBindViewHolder(holder: AddRecipeRecyclerViewHolder, position: Int) {

        val ingredient = holder.ingredients
        val image = holder.ingredientsImage
        val count = holder.count
        val positionHolder: Int

        val instruction = list[position]

        if(instruction != null) {
            ingredient.text = "$instruction"
        }

        image.setImageResource(R.drawable.bullet)
        positionHolder = position+1
        count.text = positionHolder.toString()

    }
}