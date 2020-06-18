package alyhuggan.fora.ui.account.recyclerviewadapters

import alyhuggan.fora.R
import alyhuggan.fora.repository.objects.recipe.Recipe
import alyhuggan.fora.ui.recipe.RecipeExtendedViewFragment
import alyhuggan.fora.ui.recipe.recyclerviewadapters.mainpage.RecipeViewHolder
import android.app.Activity
import android.graphics.Bitmap
import android.os.Build
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.android.synthetic.main.items_card.view.*
import java.io.ByteArrayOutputStream
import java.math.BigDecimal
import java.math.RoundingMode

private const val TAG = "AccountRecipeRecyclerVA"
private lateinit var storageRef: StorageReference

class AccountRecipeViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    val title: TextView = view.item_card_name
    val rating: TextView = view.item_card_ratings
    val image: ImageView = view.item_card_image
    val rating_LL: LinearLayout = view.card_rating_LL
    val userRatings: TextView = view.item_card_user_ratings
    val bottomLinearLayout: LinearLayout = view.card_bottom_LL

}

class AccountRecipeRecyclerViewAdapter(
    private val recipeList: List<Recipe>,
    private val activity: Activity
) : RecyclerView.Adapter<AccountRecipeViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AccountRecipeViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.items_card, parent, false)
        return AccountRecipeViewHolder(
            view
        )
    }

    override fun getItemCount() = recipeList.size+1

    @RequiresApi(Build.VERSION_CODES.KITKAT)
    override fun onBindViewHolder(holder: AccountRecipeViewHolder, position: Int) {

        val navController = Navigation.findNavController(activity, R.id.nav_host_fragment)

        val title = holder.title
        val rating = holder.rating
        val image = holder.image

        if(position ==0) {
            holder.bottomLinearLayout.visibility = View.GONE
            image.layoutParams = LinearLayout.LayoutParams(475, LinearLayout.LayoutParams.MATCH_PARENT)
            Glide.with(activity).load(R.drawable.upload).centerInside().into(image)

            image.setOnClickListener {
                navController.navigate(R.id.add_recipe)
            }

        } else {
            val recipe = recipeList[position-1]
            title.text = recipe.title
            rating.text = round(
                getRating(recipe.rating!!)
            ).toString()

            if (recipe.photo != null) {
                storageRef = FirebaseStorage.getInstance().reference

                storageRef.child(recipe.photo).downloadUrl.addOnSuccessListener {
//                Log.d(TAG, "It = $it")
                    Glide.with(activity).load(it).centerCrop().into(image)
                }
            } else {
                image.setImageResource(R.drawable.fora)
            }

            holder.itemView.setOnClickListener {

                val extendedView = RecipeExtendedViewFragment()

                image.setDrawingCacheEnabled(true)
                val bitmap: Bitmap = image.getDrawingCache()
                val stream: ByteArrayOutputStream = ByteArrayOutputStream()
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
                val byteArray = stream.toByteArray()

                val args = Bundle()
                args.putParcelable("RecipeList", recipeList[position-1])
                args.putByteArray("Image", byteArray)

                extendedView.arguments = args

                val destination = R.id.extendedView

                navController.navigate(R.id.extendedView, args)
            }
        }
    }

    private fun round(rating: Double) = BigDecimal(rating).setScale(1, RoundingMode.HALF_EVEN)

    private fun getRating(ratings: List<Double>) = ratings.sum() / ratings.count()
}
