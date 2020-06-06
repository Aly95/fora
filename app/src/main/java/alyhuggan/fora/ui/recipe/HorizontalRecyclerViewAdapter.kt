package alyhuggan.fora.ui.recipe

import alyhuggan.fora.R
import alyhuggan.fora.repository.objects.recipe.Recipe
import alyhuggan.fora.repository.objects.recipe.recipeList
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.fragment_top_recipes_new.view.*
import kotlinx.android.synthetic.main.items_recipe.view.*
import kotlin.reflect.typeOf

private const val TAG = "HorizontalRVA"

class RecipeViewHolderNew(view: View) : RecyclerView.ViewHolder(view) {
    val title: TextView = view.recipe_title
    val recyclerView: RecyclerView = view.recipe_horizontal_recycler_view
}

class HorizontalRecyclerViewAdapter(
    private val recipeList: List<Recipe>,
    private val context: Context
) : RecyclerView.Adapter<RecipeViewHolderNew>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecipeViewHolderNew {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.fragment_top_recipes_new, parent, false)
        return RecipeViewHolderNew(view)
    }

    override fun getItemCount() = 4

    override fun onBindViewHolder(holder: RecipeViewHolderNew, position: Int) {
        val title = holder.title
        val recyclerView = holder.recyclerView
        var recipeRecyclerViewAdapter = RecipeRecyclerViewAdapter(emptyList())

        val recipeTypeList = mutableListOf<String>()
        recipeTypeList.add("Breakfast")
        recipeTypeList.add("Lunch")
        recipeTypeList.add("Dessert")
        recipeTypeList.add("Savoury")

        recyclerView.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)

        when (position) {
            0 -> {
                title.text = "Top Rated"
                recipeRecyclerViewAdapter = RecipeRecyclerViewAdapter(getTopRated())
            }
            1 -> {
                title.text = recipeTypeList[position]
                recipeRecyclerViewAdapter =
                    RecipeRecyclerViewAdapter(getList(recipeTypeList[position]))
            }
            2 -> {
                title.text = recipeTypeList[position]
                recipeRecyclerViewAdapter =
                    RecipeRecyclerViewAdapter(getList(recipeTypeList[position]))
            }
            3 -> {
                title.text = recipeTypeList[position]
                recipeRecyclerViewAdapter =
                    RecipeRecyclerViewAdapter(getList(recipeTypeList[position]))
            }
        }
        recyclerView.adapter = recipeRecyclerViewAdapter
        recipeRecyclerViewAdapter.notifyDataSetChanged()
    }


//        val recipeRecyclerViewAdapter = RecipeRecyclerViewAdapter(recipeList)
//        title.text = recipeList[position].type
//
//        recyclerView.adapter = recipeRecyclerViewAdapter


    private fun getTopRated(): List<Recipe> {
        val topRated = recipeList
        return topRated.sortedByDescending { it.rating }
    }

    private fun getList(typeOfFood: String): List<Recipe> {

        val typeList = ArrayList<Recipe>()

        recipeList.forEach { recipe ->
            if (recipe.type.contains(typeOfFood))
                typeList.add(recipe)
        }
        if (typeList.isNotEmpty()) {
            return typeList.sortedByDescending { it.rating }
        } else {
            Log.d(TAG, "Empty List")
            return typeList
        }
    }

}