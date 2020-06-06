package alyhuggan.fora.ui.recipe

import alyhuggan.fora.R
import alyhuggan.fora.repository.objects.recipe.Recipe
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment

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
            val recipe: Recipe = arguments!!.getParcelable<Recipe>("Hello")!!
            Log.d(TAG, "Recipe = $recipe")
        } else {
            Log.d(TAG, "Recipe = empty")
        }
    }

}