package alyhuggan.fora.repository.objects.foods

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize

data class FoodItem(
    val type: String? = "", val name: String = "", val brand: String? = "",
    val quantity: Quantity? = null, val recipeKeyList: ArrayList<String?> = ArrayList<String?>(),
    val rating: List<Double>? = emptyList(), val url: String? = null
) :
    Parcelable {

    fun contains(query: String?): Boolean {
        if (query != null) {
            if (name.contains(query, ignoreCase = true) || brand!!.contains(
                    query,
                    ignoreCase = true
                )
            ) {
                return true
            }
        }
        return false
    }

    fun getFoodRating(): Double {
        if (rating != null) {
            return rating.sum() / rating.count()
        }
        return 1.0
    }
}