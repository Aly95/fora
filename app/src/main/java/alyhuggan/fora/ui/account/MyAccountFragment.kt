package alyhuggan.fora.ui.account

import alyhuggan.fora.R
import alyhuggan.fora.repository.objects.foods.FoodItem
import alyhuggan.fora.repository.objects.recipe.Recipe
import alyhuggan.fora.ui.account.recyclerviewadapters.AccountRecipeHorizontalRecyclerViewAdapter
import alyhuggan.fora.viewmodels.food.FoodViewModel
import alyhuggan.fora.viewmodels.food.FoodViewModelFactory
import alyhuggan.fora.viewmodels.recipe.RecipeViewModel
import alyhuggan.fora.viewmodels.recipe.RecipeViewModelFactory
import alyhuggan.fora.viewmodels.user.UserViewModel
import alyhuggan.fora.viewmodels.user.UserViewModelFactory
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_my_account.*
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.closestKodein
import org.kodein.di.generic.instance

private const val TAG = "MyAccountFragment"

class MyAccountFragment : Fragment(), KodeinAware {

    override val kodein by closestKodein()
    private lateinit var viewModel: UserViewModel
    private val viewModelFactory by instance<UserViewModelFactory>()
    private val recipeViewModelFactory by instance<RecipeViewModelFactory>()
    private val foodViewModelFactory by instance<FoodViewModelFactory>()

    private lateinit var navController: NavController

    private val adapterList = ArrayList<Recipe>()
    private val foodList = ArrayList<FoodItem>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_my_account, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        checkUser()
        initializeUi()
        searchFunction()
    }

    private fun initializeUi() {

        getUserRecipes(null)

        account_logout.setOnClickListener {
            viewModel.logOut()
        }
    }

    private fun getRecipes(key: String, query: String?) {

        val recipeViewModel =
            ViewModelProviders.of(this, recipeViewModelFactory).get(RecipeViewModel::class.java)

        Log.d(TAG, "it = $key")
        recipeViewModel.getSingleRecipe(key).observe(viewLifecycleOwner, Observer { recipe ->
            if (recipe != null) {
                if (query != null) {
                    if (recipe.title.toLowerCase().contains(query.toLowerCase())) {
                        adapterList.add(recipe)
                    }
                } else {
                    adapterList.add(recipe)
                }
            }
        })
    }


    private fun getFood(key: String) {

        val foodViewModel =
            ViewModelProviders.of(this, foodViewModelFactory).get(FoodViewModel::class.java)

        foodViewModel.getSingleFood(key).observe(viewLifecycleOwner, Observer { foodItem ->
            if (foodItem != null) {
                foodList.add(foodItem)
            }
        })
    }

    private fun getUserRecipes(query: String?) {

        val adapter =
            AccountRecipeHorizontalRecyclerViewAdapter(
                adapterList,
                foodList,
                context!!,
                activity!!
            )

        account_recyclerview.adapter = adapter
        account_recyclerview.layoutManager = LinearLayoutManager(context)

        val user = viewModel.getUser().observe(viewLifecycleOwner, Observer {
            adapterList.clear()
            foodList.clear()
//            Log.d(TAG, "User account = $it")
            it.recipeList.forEach { recipe ->
                getRecipes(recipe.key, query)
            }
            adapter.notifyDataSetChanged()
            it.foodList.forEach { foodItem ->
                if (query != null) {
                    if (foodItem.key.toLowerCase().contains(query.toLowerCase())) {
                        getFood(foodItem.key)
                    }
                } else {
                    getFood(foodItem.key)
                }
                adapter.notifyDataSetChanged()
            }
//            adapter.notifyDataSetChanged()
        })

    }

    private fun checkUser() {

        navController = Navigation.findNavController(activity!!, R.id.nav_host_fragment)

        viewModel =
            ViewModelProviders.of(this, viewModelFactory).get(UserViewModel::class.java)

        viewModel.getUser().observe(viewLifecycleOwner, Observer { user ->
            if (user.userName == "") {
                Log.d(TAG, "User not signed in")
                navController.navigate(R.id.action_register)
            } else {
                Log.d(TAG, "user email = ${user.email}")
            }
        })
    }

    private fun searchFunction() {
        val search = activity!!.findViewById<SearchView>(R.id.searchView)
        search.queryHint = "Search Your Saved Items"

        if(search.query != null) {
            getUserRecipes(search.query.toString())
        }

        search.setOnQueryTextListener(object : SearchView.OnQueryTextListener {

            override fun onQueryTextSubmit(query: String?): Boolean {
                Log.d(TAG, "Query text = $query")
                getUserRecipes(query)
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                Log.d(TAG, "Query text = $newText")
                getUserRecipes(newText)
                return true
            }
        })
    }
}