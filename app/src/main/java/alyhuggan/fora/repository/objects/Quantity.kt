package alyhuggan.fora.repository.objects

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize

data class Quantity(val quantityType: String = "", val quantity: Double = 0.0) : Parcelable {
}