package alyhuggan.fora.ui.recipe.recyclerviewadapters

import alyhuggan.fora.R
import alyhuggan.fora.repository.objects.recipe.Recipe
import alyhuggan.fora.ui.recipe.RecipeExtendedViewFragment
import android.app.Activity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.fragment.app.FragmentManager
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.items_recipe_new.view.*


private const val TAG = "RecipeRecyclerVA"

class RecipeViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    val title: TextView = view.item_recipe_title
    val rating: RatingBar = view.item_recipe_ratingBar
    val image: ImageView = view.item_recipe_image
}

class RecipeRecyclerViewAdapter(
    private val recipeList: List<Recipe>
    , private val fragmentManager: FragmentManager,
    private val activity: Activity
) : RecyclerView.Adapter<RecipeViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecipeViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.items_recipe_new, parent, false)
        return RecipeViewHolder(
            view
        )
    }

    override fun getItemCount() = recipeList.size

    override fun onBindViewHolder(holder: RecipeViewHolder, position: Int) {
        val title = holder.title
        val rating = holder.rating
        val image = holder.image

        if (recipeList.isEmpty()) {
            Log.d(TAG, "onBindViewHolder - recipeList is")
        } else {
            Log.d(TAG, "onBindViewHolder - recipeList not empty")

            val recipe = recipeList[position]

            title.text = recipe.title
            rating.rating = recipe.rating!!.toFloat()
            image.setImageResource(R.drawable.chicken)

            holder.itemView.setOnClickListener {
                val extendedView = RecipeExtendedViewFragment()
                val args = Bundle()
                args.putParcelable("Hello", recipeList[position])
                extendedView.arguments = args

                val destination = R.id.extendedView

                val navController = Navigation.findNavController(activity, R.id.nav_host_fragment)
                navController.navigate(R.id.extendedView, args)
//                fragmentManager.beginTransaction().show(extendedView).commit()
            }
        }
    }
}