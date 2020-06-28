package alyhuggan.fora.repository.objects.recipe

import alyhuggan.fora.repository.objects.foods.FoodItem
import alyhuggan.fora.repository.objects.user.UserAccount
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

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

    fun isFavourited(recipe: Recipe, userAccount: UserAccount): Boolean {

        var favourited = false
        userAccount.recipeList.forEach { userRecipe ->
            if(recipe.id == userRecipe.key) {
                favourited = true
            }
        }
        return favourited
    }

}