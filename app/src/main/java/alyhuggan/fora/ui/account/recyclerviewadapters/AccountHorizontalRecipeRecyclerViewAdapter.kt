package alyhuggan.fora.ui.account.recyclerviewadapters

import alyhuggan.fora.R
import alyhuggan.fora.repository.objects.foods.FoodItem
import alyhuggan.fora.repository.objects.recipe.Recipe
import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.items_horizontal_list.view.*

private const val TAG = "AccountHorizontalRVA"

class AccountHorizontalRecipeViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    val title: TextView = view.recipe_title
    val recyclerView: RecyclerView = view.recipes_horizontal_recycler_view
}

class AccountRecipeHorizontalRecyclerViewAdapter(
    private val recipeList: List<Recipe>,
    private val foodList: List<FoodItem>,
    private val context: Context,
    private val activity: Activity
) : RecyclerView.Adapter<AccountHorizontalRecipeViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AccountHorizontalRecipeViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.items_horizontal_list, parent, false)
        return AccountHorizontalRecipeViewHolder(
            view
        )
    }

    override fun getItemCount() = 2

    override fun onBindViewHolder(holder: AccountHorizontalRecipeViewHolder, position: Int) {

        val title = holder.title
        val recyclerView = holder.recyclerView
        val recipeRecyclerViewAdapter: AccountRecipeRecyclerViewAdapter
        val foodRecyclerViewAdapter: AccountFoodRecyclerViewAdapter

        recyclerView.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)

        when (position) {
            0 -> {
                title.text = "My Recipes"
                recipeRecyclerViewAdapter =
                    AccountRecipeRecyclerViewAdapter(
                        getTopRated(),
                        activity
                    )
                recyclerView.adapter = recipeRecyclerViewAdapter
                recipeRecyclerViewAdapter.notifyDataSetChanged()
            }
            else -> {
                title.text = "My Foods"
                foodRecyclerViewAdapter =
                    AccountFoodRecyclerViewAdapter(
                        getTopRatedFoods(),
                        activity
                    )
                recyclerView.adapter = foodRecyclerViewAdapter
                foodRecyclerViewAdapter.notifyDataSetChanged()
            }
        }

//        recyclerView.adapter = recipeRecyclerViewAdapter
//        recipeRecyclerViewAdapter.notifyDataSetChanged()

        title.visibility = View.VISIBLE
    }

    private fun getTopRated(): List<Recipe> {
        val topRated = recipeList
        return topRated.sortedByDescending { getRating(it.rating!!)}
    }

    private fun getTopRatedFoods(): List<FoodItem> {
        val topRated = foodList
        return topRated.sortedByDescending { getRating(it.rating!!)}
    }

    private fun getList(typeOfFood: String): List<Recipe> {

        val typeList = ArrayList<Recipe>()

        recipeList.forEach { recipe ->
            if (recipe.type.contains(typeOfFood))
                typeList.add(recipe)
        }
        if (typeList.isNotEmpty()) {
            return typeList.sortedByDescending { getRating(it.rating!!) }
        } else {
            return typeList
        }
    }

    private fun getRating(ratings: List<Double>) = ratings.sum()/ratings.count()

}