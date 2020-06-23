package alyhuggan.fora.repository.objects.recipe

import alyhuggan.fora.repository.objects.foods.FoodItem
import alyhuggan.fora.repository.objects.recipe.Recipe
import alyhuggan.fora.repository.objects.user.UserAccount
import androidx.lifecycle.LiveData

interface RecipeDaoInterface {
    fun getRecipes(): LiveData<List<Recipe>>
    fun addRecipe(recipe: Recipe): LiveData<String>?
    fun getSingleRecipe(key: String): LiveData<Recipe>
    fun favouriteRecipe(recipe: Recipe)
    fun removeFavourite(recipe: Recipe)
    fun getUser(): UserAccount
}