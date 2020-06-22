package alyhuggan.fora.ui.foods.recyclerviewadapters

import alyhuggan.fora.R
import alyhuggan.fora.repository.objects.foods.FoodItem
import alyhuggan.fora.ui.foods.FoodExtendedViewFragment
import android.app.Activity
import android.os.Bundle
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
import java.math.BigDecimal
import java.math.RoundingMode

private lateinit var storageRef: StorageReference
private const val TAG = "FoodRecyclerVA"

class FoodViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    val title: TextView = view.item_card_name
    val rating: TextView = view.item_card_ratings
    val image: ImageView = view.item_card_image
}

class FoodRecyclerViewAdapter(
    private val foodList: List<FoodItem>,
    private val activity: Activity
) : RecyclerView.Adapter<FoodViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FoodViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.items_card, parent, false)
        return FoodViewHolder(
            view
        )
    }

    override fun getItemCount() = foodList.size

    override fun onBindViewHolder(holder: FoodViewHolder, position: Int) {

        storageRef = FirebaseStorage.getInstance().reference

        val title = holder.title
        val rating = holder.rating
        val image = holder.image

        if (foodList.isEmpty()) {
//            Log.d(TAG, "onBindViewHolder - recipeList is")
        } else {
//            Log.d(TAG, "onBindViewHolder - recipeList not empty")

            val foodItem = foodList[position]

            if (foodItem.brand != "Generic") {
                title.text = "${foodItem.brand} ${foodItem.name}"
            } else {
                title.text = "${foodItem.name}"

            }

            if (foodItem.rating != emptyList<Double>()) {
                rating.text = round(foodItem.foodRating()).toString()
            } else {
                rating.text = 3.0.toString()
        }

            if (foodItem.url != null) {
                storageRef = FirebaseStorage.getInstance().reference

                storageRef.child(foodItem.url).downloadUrl.addOnSuccessListener {
                    Glide.with(activity).load(it).centerCrop().into(image)
                }
            } else {
                image.setImageResource(R.drawable.fora)
            }

        holder.itemView.setOnClickListener {

            val extendedView = FoodExtendedViewFragment()
            val args = Bundle()
            args.putParcelable("Hello", foodList[position])
            extendedView.arguments = args

            val navController = Navigation.findNavController(activity, R.id.nav_host_fragment)
            navController.navigate(R.id.extendedView, args)
//                fragmentManager.beginTransaction().show(extendedView).commit()
        }
    }
}

private fun round(rating: Double) = BigDecimal(rating).setScale(1, RoundingMode.HALF_EVEN)

private fun getRating(ratings: List<Double>) = ratings.sum() / ratings.count()

}