package alyhuggan.fora.repository.objects.recipe

import alyhuggan.fora.repository.objects.foods.FoodItem
import alyhuggan.fora.repository.objects.user.UserAccount
import alyhuggan.fora.repository.objects.user.UserItems
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.math.BigDecimal
import java.math.RoundingMode

@Parcelize

data class Recipe(val title: String = "", val rating: List<Double>? = emptyList(), val photo: String? = null,
                  val type: List<String> = emptyList(), val foods: List<FoodItem>? = null,
                  val method: List<String>? = emptyList(), val id: String = "") :
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
            return round(rating.sum() / rating.count()).toDouble()
        }
        return 1.0
    }

    private fun round(rating: Double) = BigDecimal(rating).setScale(1, RoundingMode.HALF_EVEN)

    fun ratingsCount(): String{
        if(rating!!.size == 1) {
            return "1 Rating"
        } else {
            return "${rating.size} Ratings"
        }
    }

    fun isFavourited(userList: List<UserItems>): Boolean {

        var favourited = false
        userList.forEach { item ->
            if(this.id == item.key && item.favourited) {
                favourited = true
            }
        }
        return favourited
    }

}