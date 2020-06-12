package alyhuggan.fora.repository.objects.foods

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize

data class FoodItem(val type: String? = "", val name: String = "", val brand: String? = "",
                    val quantity: Quantity? = null, val recipeKeyList: ArrayList<String?> = ArrayList<String?>()) :
    Parcelable {
}