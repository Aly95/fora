package alyhuggan.fora.repository.objects.recipe

import alyhuggan.fora.repository.objects.foods.FoodItem
import android.media.Image

data class Recipe(val title: String = "", val rating: Double? = 0.0, val photo: Image? = null,
                  val type: List<String> = emptyList(), val foods: ArrayList<FoodItem>? = null){

    override fun toString(): String {
        return "$title, $rating, $photo, $type, $foods"
    }
}