package alyhuggan.fora.ui.recipe

import alyhuggan.fora.R
import alyhuggan.fora.repository.objects.foods.Quantity
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
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap
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
        val typeCount = ArrayList<String>()
        val list = listOf<String>("a", "b", "c")
        val topTypeMap: MutableMap<String, Int> = HashMap()

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
            typeCount.clear()

            recipe.forEach { recipe ->
                recipe.type.forEach {
                    typeCount.add(it)
                }
            }

            /*
//            TODO Get sorting algorithm working
             */
            for (i in typeCount.distinct()) {
                val iCheck = Collections.frequency(typeCount, i)
                for (j in typeCount.distinct()) {
                    val jCheck = Collections.frequency(typeCount, j)
                    if(iCheck > jCheck) {
                        Log.d(TAG, "Testing $iCheck, $jCheck")
                    }
                }
            }





            recipe.forEach {
                adapterList.add(it)
                val title = it.title
                Log.d(TAG, "Title = $title")
            }
            val resId = R.anim.example
            val animation: LayoutAnimationController =
                AnimationUtils.loadLayoutAnimation(context, resId)
            recipes_recycler_view.layoutAnimation = animation
            adapter.notifyDataSetChanged()
        })
    }


    private fun setSearchHint() {
        val searchbox: EditText = activity!!.findViewById(R.id.searchbox_text)
        searchbox.hint = "Search Recipes"
    }
}