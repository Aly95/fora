package alyhuggan.fora.ui.recipe

import alyhuggan.fora.R
import alyhuggan.fora.repository.objects.recipe.Recipe
import alyhuggan.fora.ui.recipe.recyclerviewadapters.ExtendedRecyclerViewAdapter
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_extended_view.*
import kotlinx.android.synthetic.main.items_horizontal_list.*

private const val TAG = "RecipeExtendedViewFrag"

class RecipeExtendedViewFragment: Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_extended_view, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if(arguments != null) {

            val recipe: Recipe = arguments!!.getParcelable<Recipe>("RecipeList")!!
            val byteArray: ByteArray = arguments!!.getByteArray("Image")!!

            val imageView: ImageView = view.findViewById<ImageView>(R.id.extended_view_imageview)
            imageView.setImageBitmap(convertBitmap(byteArray))

            extended_view_title.text = recipe.title
            extended_view_ratingbar.rating = recipe.rating!!.toFloat()

            extended_view_recyclerview.layoutManager = LinearLayoutManager(context)
            extended_view_recyclerview.adapter = ExtendedRecyclerViewAdapter(recipe.foods!!)
            extended_view_recyclerview.setHasFixedSize(true)
        } else {
//            Log.d(TAG, "Recipe = empty")
        }
    }

    private fun convertBitmap(byteArray: ByteArray) = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)

}