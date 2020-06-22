package alyhuggan.fora.repository.objects.recipe

import alyhuggan.fora.repository.objects.foods.FoodItem
import android.media.Image
import android.net.Uri
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import kotlinx.android.parcel.RawValue

@Parcelize

data class Recipe(val title: String = "", val rating: List<Double>? = emptyList(), val photo: String? = null,
                  val type: List<String> = emptyList(), val foods: List<FoodItem>? = null,
                  val method: List<String>? = emptyList()) :
    Parcelable {

    override fun toString(): String {
        return "$title, $rating, $photo, $type, $foods"
    }

    fun contains(query: String?): Boolean{
        if(query != null) {
            if (title.contains(query, ignoreCase = true)) {
                return true
            }
        }
        return false
    }

    fun recipeRating(): Double {
        if (rating != null) {
            return rating.sum() / rating.count()
        }
        return 1.0
    }

    fun ratingsCount(): String{
        if(rating!!.size == 1) {
            return "1 Rating"
        } else {
            return "${rating.size} Ratings"
        }
    }
}