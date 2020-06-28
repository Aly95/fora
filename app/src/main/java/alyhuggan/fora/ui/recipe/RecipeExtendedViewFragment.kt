package alyhuggan.fora.ui.recipe

import alyhuggan.fora.R
import alyhuggan.fora.repository.objects.foods.FoodItem
import alyhuggan.fora.repository.objects.recipe.Created
import alyhuggan.fora.repository.objects.recipe.Details
import alyhuggan.fora.repository.objects.recipe.Recipe
import alyhuggan.fora.repository.objects.user.UserAccount
import alyhuggan.fora.ui.recipe.recyclerviewadapters.extendedview.ExtendedDetailsRecyclerViewAdapter
import alyhuggan.fora.ui.recipe.recyclerviewadapters.extendedview.ExtendedRecyclerViewAdapter
import alyhuggan.fora.ui.recipe.recyclerviewadapters.extendedview.ExtendedStringsRecyclerViewAdapter
import alyhuggan.fora.viewmodels.recipe.RecipeViewModel
import alyhuggan.fora.viewmodels.recipe.RecipeViewModelFactory
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.fragment_extended_view.*
import kotlinx.android.synthetic.main.item_list_card.*
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.closestKodein
import org.kodein.di.generic.instance

private const val TAG = "RecipeExtendedViewFrag"
private lateinit var auth: FirebaseAuth

class RecipeExtendedViewFragment : Fragment(), KodeinAware {

    override val kodein by closestKodein()
    private val viewModelFactory by instance<RecipeViewModelFactory>()
    private lateinit var currentlySelected: TextView

    //    private val ingredient = item_card_heading_ingredients
    private lateinit var method: TextView
    private lateinit var details: TextView
    private lateinit var recyclerView: RecyclerView

    private lateinit var ingredientList: List<FoodItem>
    private lateinit var methodList: List<String>
    private lateinit var detailsList: Details

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
            var recipeHolder = Recipe()

            currentlySelected = item_card_heading_ingredients
            auth = FirebaseAuth.getInstance()
            val viewModel =
                ViewModelProviders.of(this, viewModelFactory).get(RecipeViewModel::class.java)

            val recipe: Recipe = arguments!!.getParcelable<Recipe>("RecipeList")!!
            val byteArray: ByteArray = arguments!!.getByteArray("Image")!!

            val imageView: ImageView = view.findViewById<ImageView>(R.id.extended_view_imageview)
            imageView.setImageBitmap(convertBitmap(byteArray))

            val ingredient = item_card_heading_ingredients

            recyclerView = item_card_recyclerview
            method = item_card_heading_method
            details = item_card_heading_details

            if (recipe.foods != null && recipe.method != null) {
                ingredientList = recipe.foods
                methodList = recipe.method
                detailsList = Details(
                    getRating(recipe.rating!!),
                    recipe.rating.size,
                    3,
                    Created(
                        "Aly Huggan",
                        "14/06/2020"
                    )
                )
            } else {
                methodList = emptyList()
                ingredientList = emptyList()
            }

            extended_view_title.text = recipe.title
//            extended_view_ratingbar.rating = recipe.rating!!.toFloat()
            ingredient.text = "Ingredients"

            recyclerView.layoutManager = LinearLayoutManager(context)
            if (recipe.foods != null) {
                ingredientRecyclerView(recipe.foods)
            }
            recyclerView.setHasFixedSize(true)

            setHeadingsOnClickListeners(
                listOf<TextView>(
                    ingredient,
                    method,
                    details
                )
            )

            val favourite: ImageView = view.findViewById(R.id.extended_view_favourite)

            val user = viewModel.getUser()
            var favourited = false

            user.recipeList.forEach {
                    if(it.key == recipe.id) {
                        favourited = true
                    }
                }
            
            val test = recipe.isFavourited(recipe, user)
            Log.d(TAG, "onViewCreated: test = $test")

            if(favourited == true) {
                favourite.setImageResource(R.drawable.ic_heart_filled)
                favourite.tag = "Filled"
            } else {
                favourite.setImageResource(R.drawable.ic_heart)
                favourite.tag = "Heart"
            }

            favourite.setOnClickListener {
                if(favourite.tag == "Heart") {
                    favourite.setImageResource(R.drawable.ic_heart_filled)
                    favourite.tag = "Filled"
                    viewModel.favouriteRecipe(recipe)
                } else {
                    favourite.setImageResource(R.drawable.ic_heart)
                    favourite.tag = "Heart"
                    viewModel.removeFavourite(recipe)
                }
            }
        }
    }

    private fun getRating(ratings: List<Double>) = ratings.sum() / ratings.count()

    private fun ingredientRecyclerView(foodList: List<FoodItem>) {
        recyclerView.adapter =
            ExtendedRecyclerViewAdapter(
                foodList
            )
    }

    private fun stringsRecyclerViewData(list: List<String>) {
        recyclerView.adapter =
            ExtendedStringsRecyclerViewAdapter(
                list
            )
    }

    private fun detailsRecyclerViewData(detailList: Details) {
        recyclerView.adapter = ExtendedDetailsRecyclerViewAdapter(
            detailList
        )
    }

    private fun setHeadingsOnClickListeners(viewList: List<TextView>) {
        viewList.forEach { view ->
            view.setOnClickListener { heading ->
                setHighlightedBackground(heading as TextView)
            }
        }
    }

    private fun setHighlightedBackground(view: TextView) {
        if (view != currentlySelected) {
            when (view) {
                item_card_heading_ingredients -> {
                    ingredientRecyclerView(ingredientList)
                }
                method -> {
                    stringsRecyclerViewData(methodList)
                }
                details -> {
                    detailsRecyclerViewData(detailsList)
                }
            }
            view.setBackgroundResource(R.drawable.white_background)
            view.setTextColor(resources.getColor(R.color.primary_dark))
            currentlySelected.setBackgroundResource(R.drawable.green_menu_background)
            currentlySelected.setTextColor(Color.WHITE)
            currentlySelected = view
        }
    }

    private fun convertBitmap(byteArray: ByteArray) =
        BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)

}