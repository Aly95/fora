package alyhuggan.fora.ui.foods.recyclerviewadapters

import alyhuggan.fora.R
import alyhuggan.fora.repository.objects.recipe.Recipe
import alyhuggan.fora.ui.recipe.recyclerviewadapters.mainpage.RecipeRecyclerViewAdapter
import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.fragment_extended_view.view.*
import kotlinx.android.synthetic.main.item_list_card.view.*

class RecyclerViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    val view: RecyclerView = view.item_card_recyclerview
}

class FoodExtendedRecyclerViewAdapter(private val recipeList: List<Recipe>, private val context: Context, private val activity: Activity) :
    RecyclerView.Adapter<RecyclerViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.items_card, parent, false)
        return RecyclerViewHolder(view)
    }

    override fun getItemCount() = recipeList.size

    override fun onBindViewHolder(holder: RecyclerViewHolder, position: Int) {

        val recyclerView = holder.view
        var foodRecyclerViewAdapter =
            RecipeRecyclerViewAdapter(
                emptyList(),
                activity
            )

        recyclerView.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)

                foodRecyclerViewAdapter =
                    RecipeRecyclerViewAdapter(
                        recipeList,
                        activity
                    )

        recyclerView.adapter = foodRecyclerViewAdapter
        foodRecyclerViewAdapter.notifyDataSetChanged()

    }
}