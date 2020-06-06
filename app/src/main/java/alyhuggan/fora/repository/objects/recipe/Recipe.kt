package alyhuggan.fora.repository.objects.recipe

import alyhuggan.fora.repository.objects.foods.FoodItem
import android.media.Image
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import kotlinx.android.parcel.RawValue

@Parcelize

data class Recipe(val title: String = "", val rating: Double? = 0.0, val photo: @RawValue Image? = null,
                  val type: List<String> = emptyList(), val foods: ArrayList<FoodItem>? = null) :
    Parcelable {

    override fun toString(): String {
        return "$title, $rating, $photo, $type, $foods"
    }
}