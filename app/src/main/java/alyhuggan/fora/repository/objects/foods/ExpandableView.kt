package alyhuggan.fora.repository.objects.foods

import android.os.Parcelable
import android.view.View
import kotlinx.android.parcel.Parcelize

data class ExpandableView(val view: View? = null, val viewList: ArrayList<View>)  {
}