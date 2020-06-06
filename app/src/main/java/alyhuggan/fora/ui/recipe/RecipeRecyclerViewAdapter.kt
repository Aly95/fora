package alyhuggan.fora.ui.recipe

import alyhuggan.fora.R
import alyhuggan.fora.repository.objects.recipe.Recipe
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.items_recipe_new.view.*

private const val TAG = "RecipeRecyclerVA"

class RecipeViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    val title: TextView = view.item_recipe_title
    val rating: RatingBar = view.item_recipe_ratingBar
    val image: ImageView = view.item_recipe_image
}

class RecipeRecyclerViewAdapter(private val recipeList: List<Recipe>): RecyclerView.Adapter<RecipeViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecipeViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.items_recipe_new, parent, false)
        return RecipeViewHolder(view)
    }

    override fun getItemCount() = recipeList.size

    override fun onBindViewHolder(holder: RecipeViewHolder, position: Int) {
        val title = holder.title
        val rating = holder.rating
        val image = holder.image

        if(recipeList.isEmpty()) {
            Log.d(TAG, "onBindViewHolder - recipeList is")
        } else {
            Log.d(TAG, "onBindViewHolder - recipeList not empty")

            val recipe = recipeList[position]

            title.text = recipe.title
            rating.rating = recipe.rating!!.toFloat()
            image.setImageResource(R.drawable.chicken)
        }
    }
}