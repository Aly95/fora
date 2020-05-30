package alyhuggan.fora.ui.recipe

import alyhuggan.fora.R
import alyhuggan.fora.repository.objects.FoodItem
import alyhuggan.fora.repository.objects.Quantity
import alyhuggan.fora.repository.objects.Recipe
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.GridLayout
import android.widget.TextView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_top_recipes.*

private const val TAG = "TopRecipesFragment"

class TopRecipesFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_top_recipes, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setToolbarTitle()
        initializeRecyclerAdapter()
    }

    private fun initializeRecyclerAdapter() {

        Log.d(TAG, "initializeRecyclerAdapter: Starts")

        val recipe = sampleData()
        val list = ArrayList<Recipe>()
        for(i in 0..15) {
            list.add(i, recipe)
        }

        Log.d(TAG, "List = $list")

        recipes_recycler_view.layoutManager = LinearLayoutManager(context)
        recipes_recycler_view.adapter =
            RecipeRecyclerViewAdapter(list)
        recipes_recycler_view.setHasFixedSize(true)

    }

    private fun sampleData(): Recipe {
        val mutableList = mutableListOf<FoodItem>()

        for(i in 0..8) {
            mutableList.add(
                FoodItem(
                    "protein",
                    "chicken",
                    Quantity(
                        "grams",
                        2.0
                    )
                )
            )
        }

        val list: ArrayList<FoodItem> = mutableList as ArrayList

        return Recipe("Chicken with Chicken", 4.8, null, "Lunch", list)
    }

    private fun setToolbarTitle() {
        val toolbar: TextView = activity!!.findViewById(R.id.maintoolbar_title)
        toolbar.text = "Top Recipes"
    }

}