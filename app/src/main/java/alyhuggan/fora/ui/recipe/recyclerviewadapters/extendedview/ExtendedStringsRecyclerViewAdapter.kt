package alyhuggan.fora.ui.recipe.recyclerviewadapters.extendedview

import alyhuggan.fora.R
import alyhuggan.fora.repository.objects.foods.FoodItem
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.storage.StorageReference
import kotlinx.android.synthetic.main.items_extended_view.view.*

class StringsRecyclerViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    val strings: TextView = view.extended_view_textview
    val image: ImageView = view.extended_view_bullet
    val count: TextView = view.extended_view_count
}

class ExtendedStringsRecyclerViewAdapter(private val stringList: List<String>) :
    RecyclerView.Adapter<StringsRecyclerViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StringsRecyclerViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.items_extended_view, parent, false)
        return StringsRecyclerViewHolder(
            view
        )
    }

    override fun getItemCount() = stringList.size

    override fun onBindViewHolder(holder: StringsRecyclerViewHolder, position: Int) {

        val listCount = holder.count
        val imageView = holder.image
        imageView.setImageResource(R.drawable.bullet)


        listCount.text = (position+1).toString()
        val strings = holder.strings
        strings.text = stringList[position]
    }
}