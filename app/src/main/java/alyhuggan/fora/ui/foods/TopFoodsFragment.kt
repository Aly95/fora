package alyhuggan.fora.ui.foods

import alyhuggan.fora.R
import alyhuggan.fora.repository.objects.foods.Quantity
import alyhuggan.fora.repository.objects.foods.FoodItem
import alyhuggan.fora.repository.objects.recipe.Recipe
import alyhuggan.fora.ui.foods.recyclerviewadapters.FoodHorizontalRecyclerViewAdapter
import alyhuggan.fora.viewmodels.food.FoodViewModel
import alyhuggan.fora.viewmodels.food.FoodViewModelFactory
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.view.animation.LayoutAnimationController
import android.widget.EditText
import android.widget.SearchView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.fragment_top_foods.*
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.closestKodein
import org.kodein.di.generic.instance

private const val TAG = "TopFoodsFragment"
private lateinit var auth: FirebaseAuth

class TopFoodsFragment : Fragment(), KodeinAware {

    override val kodein by closestKodein()
    private val viewModelFactory by instance<FoodViewModelFactory>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_top_foods, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initializeUi(null)
        searchFunction()
    }

    private fun initializeUi(query: String?) {

        val viewModel = ViewModelProviders.of(this, viewModelFactory).get(FoodViewModel::class.java)
        val foodList = ArrayList<FoodItem>()
        val tagList = ArrayList<String>()
        val typeCount = ArrayList<String>()

        viewModel.getFoods().observe(viewLifecycleOwner, Observer { foods ->
            foodList.clear()
            tagList.clear()
            if (foods.isNotEmpty()) {
                foods.forEach { foodItem ->
                    if (foodItem.contains(query)) {
                        foodList.add(foodItem)
                        if(foodItem.type != "" && foodItem.type != null) {
                            typeCount.add(foodItem.type)
                        }
                    }
                }

                if(foodList.isNotEmpty()) {
                    val tags = getFrequentTags(typeCount)
                    tagList.add("Top Rated")
                    tags.forEach {
                        tagList.add(it.first)
                    }
                }
            }
        })

        animate()
        foods_recycler_view.layoutManager = LinearLayoutManager(context)
        foods_recycler_view.setHasFixedSize(true)

        foods_recycler_view.adapter =
            FoodHorizontalRecyclerViewAdapter(
                foodList,
                tagList,
                context!!,
                activity!!
            )
    }

    private fun animate() {
        val resId = R.anim.example
        val animation: LayoutAnimationController =
            AnimationUtils.loadLayoutAnimation(context, resId)
        foods_recycler_view.layoutAnimation = animation
    }

    private fun searchFunction() {
        val search = activity!!.findViewById<SearchView>(R.id.searchView)
        search.queryHint = "Search Foods"

        if(search.query != null) {
            initializeUi(search.query.toString())
        }

        search.setOnQueryTextListener(object : SearchView.OnQueryTextListener {

            override fun onQueryTextSubmit(query: String?): Boolean {
                Log.d(TAG, "Query text = $query")
                initializeUi(query)
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                Log.d(TAG, "Query text = $newText")
                initializeUi(newText)
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