package alyhuggan.fora.ui.recipe

import alyhuggan.fora.R
import alyhuggan.fora.repository.objects.recipe.Recipe
import alyhuggan.fora.ui.MainFragment
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
import android.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.fragment_top_recipes.*
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.closestKodein
import org.kodein.di.generic.instance
import kotlin.collections.ArrayList

private const val TAG = "TopRecipesFragment"
private lateinit var auth: FirebaseAuth

class TopRecipesFragment : MainFragment(), KodeinAware {

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
        auth = FirebaseAuth.getInstance()
        initializeRecyclerAdapter(null)
        searchFunction()
    }

    private fun initializeRecyclerAdapter(query: String?) {

        val viewModel =
            ViewModelProviders.of(this, viewModelFactory).get(RecipeViewModel::class.java)

        val userRecipeList = viewModel.getUser().recipeList
        val typeCount = ArrayList<String>()
        val tagList = ArrayList<String>()

        val adapter = RecipeHorizontalRecyclerViewAdapter(
            adapterList,
            tagList,
            userRecipeList,
            context!!,
            activity!!
        )

        recipes_recycler_view.adapter = adapter
        recipes_recycler_view.setHasFixedSize(true)

        recipes_recycler_view.layoutManager = LinearLayoutManager(context)

        viewModel.getRecipes().observe(viewLifecycleOwner, Observer { recipeList ->
            adapterList.clear()
            typeCount.clear()
            tagList.clear()

            recipeList.forEach { recipe ->
                if (recipe.contains(query)) {
                    recipe.type.forEach {
                        typeCount.add(it)
                    }
                    adapterList.add(recipe)
                }
            }

            if(adapterList.isNotEmpty()) {
                val tags = getFrequentTags(typeCount)
                tagList.add("Top Rated")
                tags.forEach {
                    tagList.add(it.first)
                }
            }

            animate()
            adapter.notifyDataSetChanged()
        })
    }

    private fun animate() {
        val resId = R.anim.example
        val animation: LayoutAnimationController =
            AnimationUtils.loadLayoutAnimation(context, resId)
        recipes_recycler_view.layoutAnimation = animation
    }

    private fun searchFunction() {

        val search = activity!!.findViewById<SearchView>(R.id.searchView)
        search.queryHint = "Search Recipes"

        if(search.query != null) {
            initializeRecyclerAdapter(search.query.toString())
        }

        search.setOnQueryTextListener(object : SearchView.OnQueryTextListener {

            override fun onQueryTextSubmit(query: String?): Boolean {
                Log.d(TAG, "Query text = $query")
                initializeRecyclerAdapter(query)
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                Log.d(TAG, "Query text = $newText")
                initializeRecyclerAdapter(newText)
                return true
            }
        })
    }

    private fun getFrequentTags(typeCount: ArrayList<String>): ArrayList<Pair<String, Int>> {

        val tagChecker = ArrayList<Pair<String, Int>>()

        typeCount.distinct().forEach {
            var count = 0
            for(i in typeCount.indices) {
                if(typeCount[i] == it) {
                    count++
                }
            }
            tagChecker.add(Pair(it, count))
        }
        tagChecker.sortByDescending { it.second }
        return tagChecker
    }
}