package alyhuggan.fora.ui.recipe.recyclerviewadapters.mainpage

import alyhuggan.fora.R
import alyhuggan.fora.repository.objects.recipe.Recipe
import alyhuggan.fora.repository.objects.user.UserItems
import alyhuggan.fora.ui.recipe.RecipeExtendedViewFragment
import android.app.Activity
import android.graphics.Bitmap
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.android.synthetic.main.items_card.view.*
import java.io.ByteArrayOutputStream
import java.math.BigDecimal
import java.math.RoundingMode

private const val TAG = "RecipeRecyclerVA"
private lateinit var storageRef: StorageReference

class RecipeViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    val title: TextView = view.item_card_name
    val rating: TextView = view.item_card_ratings
    val image: ImageView = view.item_card_image
    val favourited: ImageView = view.item_card_favourited
}

class RecipeRecyclerViewAdapter(
    private val recipeList: List<Recipe>,
    private val userRecipeList: List<UserItems>?,
    private val activity: Activity
) : RecyclerView.Adapter<RecipeViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecipeViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.items_card, parent, false)
        return RecipeViewHolder(
            view
        )
    }

    override fun getItemCount() = recipeList.size

    override fun onBindViewHolder(holder: RecipeViewHolder, position: Int) {
        val title = holder.title
        val rating = holder.rating
        val image = holder.image
        val favourited = holder.favourited

        val recipe = recipeList[position]

        title.text = recipe.title
        rating.text = recipe.recipeRating().toString()

        if (recipe.photo != null) {
            storageRef = FirebaseStorage.getInstance().reference

            storageRef.child(recipe.photo).downloadUrl.addOnSuccessListener {
//                Log.d(TAG, "It = $it")
                Glide.with(activity).load(it).centerCrop().into(image)
            }
        } else {
            image.setImageResource(R.drawable.fora)
        }

        if(userRecipeList != null) {
            if(!recipe.isFavourited(userRecipeList)) {
                Log.d(TAG, "onBindViewHolder: favourited = false")
            } else {
                favourited.visibility = View.VISIBLE
            }
        }

        holder.itemView.setOnClickListener {

            val extendedView = RecipeExtendedViewFragment()

            image.setDrawingCacheEnabled(true)
            val bitmap: Bitmap = image.getDrawingCache()
            val stream: ByteArrayOutputStream = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
            val byteArray = stream.toByteArray()

            val args = Bundle()
            args.putParcelable("RecipeList", recipeList[position])
            args.putByteArray("Image", byteArray)

            extendedView.arguments = args

            val destination = R.id.extendedView

            val navController = Navigation.findNavController(activity, R.id.nav_host_fragment)
            navController.navigate(R.id.extendedView, args)
        }
    }

    private fun round(rating: Double) = BigDecimal(rating).setScale(1, RoundingMode.HALF_EVEN)

    private fun getRating(ratings: List<Double>) = ratings.sum() / ratings.count()
}
