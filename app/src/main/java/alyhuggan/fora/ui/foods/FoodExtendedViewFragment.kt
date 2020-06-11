package alyhuggan.fora.ui.foods

import alyhuggan.fora.R
import alyhuggan.fora.repository.objects.Quantity
import alyhuggan.fora.repository.objects.foods.FoodItem
import alyhuggan.fora.repository.objects.recipe.Recipe
import alyhuggan.fora.ui.recipe.recyclerviewadapters.mainpage.RecipeRecyclerViewAdapter
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_extended_view.*
import kotlinx.android.synthetic.main.fragment_extended_view.extended_view_ingredients

private const val TAG = "FoodExtendedViewFrag"

class FoodExtendedViewFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_extended_view, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (arguments != null) {
            val food: FoodItem = arguments!!.getParcelable<FoodItem>("Hello")!!
            val recipeList = mutableListOf<Recipe>()
            val foodList = mutableListOf<FoodItem>()
            val typeList = mutableListOf<String>()

            extended_view_ingredients.text = "Used in recipes:"

            typeList.add("Type")

            for(i in 0..10) {
                foodList.add(
                    FoodItem(
                        "Sugar sugar",
                        "full fat sugar",
                        null,
                        Quantity(
                            "cups",
                            "5.0"
                        )
                    )
                )
                foodList.add(
                    FoodItem(
                        "Could do",
                        "milk",
                        null,
                        Quantity(
                            "ml",
                            "550"
                        )
                    )
                )
            }

            for (i in 0..10) {
                recipeList.add(
                    Recipe(
                        "Recipe",
                        5.0,
                        null,
                        typeList,
                        foodList
                    )
                )
            }

            val adapter =
                RecipeRecyclerViewAdapter(
                    recipeList,
                    activity!!
                )

            extended_view_title.text = food.name
            extended_view_ratingbar.rating = 4.5.toFloat()
            extended_view_recyclerview.layoutManager =
                LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            extended_view_recyclerview.adapter = adapter
            adapter.notifyDataSetChanged()


//            extended_view_recyclerview.layoutManager = LinearLayoutManager(context)
//            extended_view_recyclerview.adapter = ExtendedRecyclerViewAdapter(recipe.foods!!)
//            extended_view_recyclerview.setHasFixedSize(true)
        } else {
            extended_view_title.text = "Hello"
        }
    }

}