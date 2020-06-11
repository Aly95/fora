package alyhuggan.fora.ui.recipe

import alyhuggan.fora.R
import alyhuggan.fora.repository.objects.Quantity
import alyhuggan.fora.repository.objects.foods.FoodItem
import alyhuggan.fora.repository.objects.recipe.Recipe
import alyhuggan.fora.ui.recipe.recyclerviewadapters.mainpage.RecipeHorizontalRecyclerViewAdapter
import alyhuggan.fora.viewmodels.recipe.RecipeViewModel
import alyhuggan.fora.viewmodels.recipe.RecipeViewModelFactory
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.view.animation.LayoutAnimationController
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.fragment_top_recipes.*
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.closestKodein
import org.kodein.di.generic.instance
import kotlin.math.roundToInt

private const val TAG = "TopRecipesFragment"
private lateinit var auth: FirebaseAuth

class TopRecipesFragment : Fragment(), KodeinAware {

    override val kodein by closestKodein()
    private val viewModelFactory by instance<RecipeViewModelFactory>()
    private val adapterList = ArrayList<Recipe>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_top_recipes, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setSearchHint()
        auth = FirebaseAuth.getInstance()
        initializeRecyclerAdapter()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
//        outState.putString("recyclerView", "created")
    }

    private fun initializeRecyclerAdapter() {

        val viewModel =
            ViewModelProviders.of(this, viewModelFactory).get(RecipeViewModel::class.java)

        val recipeList = ArrayList<Recipe>()

        val adapter = RecipeHorizontalRecyclerViewAdapter(
            adapterList,
            context!!,
            activity!!
        )

        recipes_recycler_view.adapter = adapter
        recipes_recycler_view.setHasFixedSize(true)

        recipes_recycler_view.layoutManager = LinearLayoutManager(context)

        viewModel.getRecipes().observe(viewLifecycleOwner, Observer { recipe ->
            adapterList.clear()

            recipe.forEach {
                adapterList.add(it)
                val title = it.title.toString()
                Log.d(TAG, "Title = $title")
            }
            val resId = R.anim.example
            val animation: LayoutAnimationController =
                AnimationUtils.loadLayoutAnimation(context, resId)
            recipes_recycler_view.layoutAnimation = animation
            adapter.notifyDataSetChanged()
        })

//        val list = ArrayList<Recipe>()
//        for (i in 0..15) {
//            val recipe = sampleData()
//            list.add(i, recipe)
//        }
//
//        list.sortByDescending { it.rating }
//        Log.d(TAG, "Hello")

//        viewModel.addRecipe(list[1])

    }

    private fun sampleData(): Recipe {

        val mutableList = mutableListOf<FoodItem>()

        for (i in 0..8) {

            if (i % 2 == 0) {
                mutableList.add(
                    FoodItem(
                        "Something",
                        "Flour",
                        null,
                        Quantity(
                            "Cups of",
                            "1 1/2"
                        )
                    )
                )
            } else {
                mutableList.add(
                    FoodItem(
                        "Dessert",
                        "baking soda",
                        null,
                        Quantity(
                            "teaspoon",
                            "1/4"
                        )
                    )
                )
            }
        }
        val list: ArrayList<FoodItem> = mutableList as ArrayList
        val rating: Double = Math.random() * 5

        val rnd: Double = Math.random() * 5
        val name = rnd.roundToInt()
//        Log.d(TAG, "name = $name")
        var title = ""

        val type = mutableListOf<String>()

        when(name) {
            1 -> {
                title = "Rubbish Meal"
                type.add("Lunch")
                type.add("Savoury")
            }
            2 -> {
                title = "Pork Pie"
                type.add("Lunch")
                type.add("Savoury")
            }
            3 -> {
                title = "Nicks Powdered Protein"
                type.add("Sweet")
                type.add("Dessert")
            }
            4 -> {
                title = "Sams Chocolate Sausage"
                type.add("Breakfast")
                type.add("Savoury")
            }
            5 -> {
                title = "Seafood Delight"
                type.add("Lunch")
                type.add("Dessert")
            } else -> {
                title = "Captain Crunch"
            type.add("Lunch")
            type.add("Dessert")
        }
        }

        return Recipe(
            title,
            rating,
            null,
            type,
            list
        )
    }

    private fun setSearchHint() {
        val searchbox: EditText = activity!!.findViewById(R.id.searchbox_text)
        searchbox.hint = "Search Recipes"
    }
}