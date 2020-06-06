package alyhuggan.fora.repository.objects.recipe

import alyhuggan.fora.repository.objects.foods.FoodItem
import alyhuggan.fora.repository.objects.recipe.Recipe
import androidx.lifecycle.LiveData

interface RecipeDaoInterface {

    fun getRecipes(): LiveData<List<Recipe>>
    fun getFoods(): LiveData<List<FoodItem>>

    fun addRecipe(recipe: Recipe)
    fun addFood(foodItem: FoodItem)
}