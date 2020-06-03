package alyhuggan.fora.ui.recipe

import alyhuggan.fora.R
import alyhuggan.fora.repository.objects.foods.FoodItem
import alyhuggan.fora.repository.objects.Quantity
import alyhuggan.fora.repository.objects.recipe.Recipe
import alyhuggan.fora.viewmodels.recipe.RecipeViewModel
import alyhuggan.fora.viewmodels.recipe.RecipeViewModelFactory
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.fragment_top_recipes.*
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.closestKodein
import org.kodein.di.generic.instance

private const val TAG = "TopRecipesFragment"
private lateinit var auth: FirebaseAuth

class TopRecipesFragment : Fragment(), KodeinAware {

    override val kodein by closestKodein()
    private val viewModelFactory by instance<RecipeViewModelFactory>()

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
        auth = FirebaseAuth.getInstance()
        initializeRecyclerAdapter()
    }

    private fun initializeRecyclerAdapter() {

        val viewModel = ViewModelProviders.of(this, viewModelFactory).get(RecipeViewModel::class.java)

        val recipeList = ArrayList<Recipe>()

        Log.d(TAG, "initializeRecyclerAdapter: Starts")

        val list = ArrayList<Recipe>()
        for(i in 0..15) {
            val recipe = sampleData()
            list.add(i, recipe)
        }

        list.sortByDescending { it.rating }

        viewModel.addRecipe(list[1])

        viewModel.getRecipes().observe(viewLifecycleOwner, Observer { recipe ->
            recipeList.clear()
            recipe.forEach {
                Log.d(TAG, "It = $it")
                recipeList.add(it)
            }
            recipes_recycler_view.layoutManager = LinearLayoutManager(context)
            recipes_recycler_view.adapter =
                RecipeRecyclerViewAdapter(recipeList)
            recipes_recycler_view.setHasFixedSize(true)
        })
//        Log.d(TAG, "Fragment recipe = $recipes")

        Log.d(TAG, "List = $list")

//        recipes_recycler_view.layoutManager = LinearLayoutManager(context)
//        recipes_recycler_view.adapter =
//            RecipeRecyclerViewAdapter(recipes)
//        recipes_recycler_view.setHasFixedSize(true)

    }

    private fun sampleData(): Recipe {

        val mutableList = mutableListOf<FoodItem>()

        for(i in 0..8) {

            mutableList.add(
                FoodItem(
                    "carbohydrate",
                    "bread",
                    Quantity(
                        "grams",
                        4.0
                    )
                )
            )
        }

        val list: ArrayList<FoodItem> = mutableList as ArrayList
        val rating: Double = Math.random()*5

        return Recipe(
            "Bread with Love",
            rating,
            null,
            "Dinner",
            list
        )
    }

    private fun setToolbarTitle() {
        val toolbar: TextView = activity!!.findViewById(R.id.maintoolbar_title)
        toolbar.text = "Top Recipes"
    }

}