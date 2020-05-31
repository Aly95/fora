package alyhuggan.fora.repository.database.logic

import alyhuggan.fora.repository.objects.FoodItem
import alyhuggan.fora.repository.objects.Recipe
import androidx.lifecycle.LiveData

interface ForaDaoInterface {

    fun getRecipes(): LiveData<List<Recipe>>
    fun getFoods(): LiveData<List<FoodItem>>

    fun addRecipe(recipe: Recipe)
    fun addFood(foodItem: FoodItem)
}