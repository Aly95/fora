package alyhuggan.fora.repository.database.logic

import alyhuggan.fora.repository.objects.FoodItem
import alyhuggan.fora.repository.objects.Recipe
import android.util.Log
import androidx.lifecycle.LiveData

private const val TAG = "ForaDaoImplementation"

class ForaDaoImplementation :
    ForaDaoInterface {

    override fun getRecipes(): LiveData<List<Recipe>> {
        Log.d(TAG, "getRecipes: starts")
     }

    override fun getFoods(): LiveData<List<FoodItem>> {
        Log.d(TAG, "getFoods: starts")
    }

    override fun addRecipe(recipe: Recipe) {
        Log.d(TAG, "addRecipes: starts")
    }

    override fun addFood(foodItem: FoodItem) {
        Log.d(TAG, "addFood: starts")
    }
}