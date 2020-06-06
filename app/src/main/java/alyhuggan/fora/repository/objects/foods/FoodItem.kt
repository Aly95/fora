package alyhuggan.fora.repository.objects.foods

import alyhuggan.fora.repository.objects.Quantity
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize

data class FoodItem(val type: String = "", val name: String = "", val quantity: Quantity? = null) :
    Parcelable {
}