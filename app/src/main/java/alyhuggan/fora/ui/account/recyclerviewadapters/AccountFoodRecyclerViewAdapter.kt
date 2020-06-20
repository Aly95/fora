package alyhuggan.fora.ui.account.recyclerviewadapters

import alyhuggan.fora.R
import alyhuggan.fora.repository.objects.foods.FoodItem
import alyhuggan.fora.ui.foods.FoodExtendedViewFragment
import alyhuggan.fora.ui.foods.recyclerviewadapters.FoodViewHolder
import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RatingBar
import android.widget.TextView
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.items_card.view.*


private const val TAG = "FoodRecyclerVA"

class AccountFoodViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    val title: TextView = view.item_card_name
    val rating: RatingBar = view.item_card_ratingbar
    val image: ImageView = view.item_card_image
    val bottomLinearLayout: LinearLayout = view.card_bottom_LL
}

class AccountFoodRecyclerViewAdapter(
    private val foodList: List<FoodItem>,
    private val activity: Activity
) : RecyclerView.Adapter<AccountFoodViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AccountFoodViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.items_card, parent, false)
        return AccountFoodViewHolder(
            view
        )
    }

    override fun getItemCount() = foodList.size+1

    override fun onBindViewHolder(holder: AccountFoodViewHolder, position: Int) {

        val navController = Navigation.findNavController(activity, R.id.nav_host_fragment)

        val title = holder.title
        val rating = holder.rating
        val image = holder.image

        if(position ==0) {
            holder.bottomLinearLayout.visibility = View.GONE
            image.layoutParams = LinearLayout.LayoutParams(475, LinearLayout.LayoutParams.MATCH_PARENT)
            Glide.with(activity).load(R.drawable.upload).centerInside().into(image)

            image.setOnClickListener {
                navController.navigate(R.id.addFood)
            }

        } else {
            val foodItem = foodList[position]

            if(foodItem.brand != "Generic") {
                title.text = "${foodItem.brand} ${foodItem.name}"
            } else {
                title.text = "${foodItem.name}"

            }
            rating.rating = 4.5.toFloat()
//            image.setImageResource(R.drawable.fora)
            Glide.with(activity).load(R.drawable.fora).centerInside().into(image)

            holder.itemView.setOnClickListener {

                val extendedView = FoodExtendedViewFragment()
                val args = Bundle()
                args.putParcelable("Hello", foodList[position])
                extendedView.arguments = args

                val navController = Navigation.findNavController(activity, R.id.nav_host_fragment)
                navController.navigate(R.id.extendedFood, args)
//                fragmentManager.beginTransaction().show(extendedView).commit()
            }
        }
    }
}