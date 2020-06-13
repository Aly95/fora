package alyhuggan.fora.ui.foods

import alyhuggan.fora.R
import alyhuggan.fora.repository.objects.foods.FoodItem
import alyhuggan.fora.repository.objects.recipe.Recipe
import alyhuggan.fora.ui.recipe.recyclerviewadapters.mainpage.RecipeRecyclerViewAdapter
import alyhuggan.fora.viewmodels.food.FoodViewModel
import alyhuggan.fora.viewmodels.food.FoodViewModelFactory
import alyhuggan.fora.viewmodels.recipe.RecipeViewModel
import alyhuggan.fora.viewmodels.recipe.RecipeViewModelFactory
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_extended_view.*
import kotlinx.android.synthetic.main.item_list_card.*
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.closestKodein
import org.kodein.di.generic.instance

private const val TAG = "FoodExtendedViewFrag"

class FoodExtendedViewFragment : Fragment(), KodeinAware {

    override val kodein by closestKodein()
    private val viewModelFactory by instance<FoodViewModelFactory>()
    private val recipeViewModelFactory by instance<RecipeViewModelFactory>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_extended_view, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val viewModel = ViewModelProviders.of(this, viewModelFactory).get(FoodViewModel::class.java)
        val recipeViewModel =
            ViewModelProviders.of(this, recipeViewModelFactory).get(RecipeViewModel::class.java)

        if (arguments != null) {

            val food: FoodItem = arguments!!.getParcelable<FoodItem>("Hello")!!
            val recipeList = ArrayList<Recipe>()
            val foodList = mutableListOf<FoodItem>()
            val keyList = ArrayList<String>()

            item_card_title_first.text = "Used in recipes:"
            extended_view_title.text = ("${food.brand} ${food.name}")
            extended_view_ratingbar.rating = 4.5.toFloat()

            viewModel.getFoods().observe(viewLifecycleOwner, Observer { foodItemList ->
                keyList.clear()
                recipeList.clear()
                foodItemList.forEach { foodItem ->
                    if(foodItem.brand == food.brand && foodItem.name == food.name) {
                        foodItem.recipeKeyList.forEach { key ->
                            Log.d(TAG, "Count = $key")
                            if (key != null) {
                                if (food.recipeKeyList.contains(key)) {
                                    recipeViewModel.getSingleRecipe(key)
                                        .observe(viewLifecycleOwner, Observer {
                                            Log.d(TAG, "it = $it")
                                            recipeList.add(it)
                                        })
                                }
                            }
                        }
                    }
//                    setUpRecyclerView(recipeList)
                }
                setUpRecyclerView(recipeList)
            })
            Log.d(TAG, "Keylist = $keyList")
        }
    }

    private fun setUpRecyclerView(recipeList: ArrayList<Recipe>) {
        val adapter =
            RecipeRecyclerViewAdapter(
                recipeList,
                activity!!
            )
        item_card_recyclerview.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        item_card_recyclerview.adapter = adapter
        adapter.notifyDataSetChanged()
    }

}