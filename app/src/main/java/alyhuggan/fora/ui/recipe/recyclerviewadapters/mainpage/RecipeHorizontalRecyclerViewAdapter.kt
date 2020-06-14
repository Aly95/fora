package alyhuggan.fora.ui.recipe.recyclerviewadapters.mainpage

import alyhuggan.fora.R
import alyhuggan.fora.repository.objects.recipe.Recipe
import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.items_horizontal_list.view.*

private const val TAG = "HorizontalRVA"

class RecipeViewHolderNew(view: View) : RecyclerView.ViewHolder(view) {
    val title: TextView = view.recipe_title
    val recyclerView: RecyclerView = view.recipes_horizontal_recycler_view
}

class RecipeHorizontalRecyclerViewAdapter(
    private val recipeList: List<Recipe>,
    private val tagList: ArrayList<String>,
    private val context: Context,
    private val activity: Activity
) : RecyclerView.Adapter<RecipeViewHolderNew>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecipeViewHolderNew {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.items_horizontal_list, parent, false)
        return RecipeViewHolderNew(
            view
        )
    }

    override fun getItemCount() = tagList.size

    override fun onBindViewHolder(holder: RecipeViewHolderNew, position: Int) {

        val title = holder.title
        val recyclerView = holder.recyclerView
        val recipeRecyclerViewAdapter: RecipeRecyclerViewAdapter

        recyclerView.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)

        when (position) {
            0 -> {
                title.text = "Top Rated"
                recipeRecyclerViewAdapter =
                    RecipeRecyclerViewAdapter(
                        getTopRated(),
                        activity
                    )
            }
            else -> {
                title.text = tagList[position]
                recipeRecyclerViewAdapter =
                    RecipeRecyclerViewAdapter(
                        getList(
                            tagList[position]
                        ),
                        activity
                    )
            }
        }

        recyclerView.adapter = recipeRecyclerViewAdapter
        recipeRecyclerViewAdapter.notifyDataSetChanged()

        title.visibility = View.VISIBLE
    }

    private fun getTopRated(): List<Recipe> {
        val topRated = recipeList
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