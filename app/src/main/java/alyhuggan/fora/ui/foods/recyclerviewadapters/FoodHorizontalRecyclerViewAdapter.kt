package alyhuggan.fora.ui.foods.recyclerviewadapters

import alyhuggan.fora.R
import alyhuggan.fora.repository.objects.foods.FoodItem
import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.items_horizontal_list.view.*

private const val TAG = "HorizontalRVA"

class ExtendedFoodHolder(view: View) : RecyclerView.ViewHolder(view) {
    val title: TextView = view.recipe_title
    val recyclerView: RecyclerView = view.recipes_horizontal_recycler_view
}

class FoodHorizontalRecyclerViewAdapter(
    private val foodList: List<FoodItem>,
    private val context: Context,
    private val activity: Activity
) : RecyclerView.Adapter<ExtendedFoodHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExtendedFoodHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.items_horizontal_list, parent, false)
        return ExtendedFoodHolder(
            view
        )
    }

    override fun getItemCount() = 6

    override fun onBindViewHolder(holder: ExtendedFoodHolder, position: Int) {

        val title = holder.title
        val recyclerView = holder.recyclerView
        var foodRecyclerViewAdapter =
            FoodRecyclerViewAdapter(
                emptyList(),
                activity
            )

        val foodTypeList = mutableListOf<String>()
        foodTypeList.add("")
        foodTypeList.add("Proteins")
        foodTypeList.add("Starches")
        foodTypeList.add("Fruit and Veg")
        foodTypeList.add("Condiments")
        foodTypeList.add("Others")

        recyclerView.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)

        when (position) {
            0 -> {
                title.text = "Top Rated"
                foodRecyclerViewAdapter =
                    FoodRecyclerViewAdapter(
                        getTopRated(),
                        activity
                    )
            }
            else -> {
                title.text = foodTypeList[position]
                foodRecyclerViewAdapter =
                    FoodRecyclerViewAdapter(
                        getList(
                            foodTypeList[position]
                        ),
                        activity
                    )
            }
        }

        recyclerView.adapter = foodRecyclerViewAdapter
        foodRecyclerViewAdapter.notifyDataSetChanged()

        title.visibility = View.VISIBLE
    }

    private fun getTopRated(): List<FoodItem> {
        val topRated = foodList
        return foodList //rating not yet implemented
//        return topRated.sortedByDescending { it.rating }
    }

    private fun getList(typeOfFood: String): List<FoodItem> {

        val typeList = ArrayList<FoodItem>()

        foodList.forEach { food ->
            if (food.type.contains(typeOfFood))
                typeList.add(food)
        }
        if (typeList.isNotEmpty()) {
//            return typeList.sortedByDescending { it.rating }
            return typeList
        } else {
//            Log.d(TAG, "Empty List")
            return typeList
        }
    }

}