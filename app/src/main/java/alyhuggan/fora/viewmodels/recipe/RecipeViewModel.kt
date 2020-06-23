package alyhuggan.fora.viewmodels.recipe

import alyhuggan.fora.repository.objects.recipe.Recipe
import alyhuggan.fora.repository.objects.recipe.RecipeDaoInterface
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel

class RecipeViewModel(private val recipeDaoInterface: RecipeDaoInterface) : ViewModel() {

    fun getRecipes() = recipeDaoInterface.getRecipes()
    fun addRecipe(recipe: Recipe) = recipeDaoInterface.addRecipe(recipe)
    fun getSingleRecipe(key: String) = recipeDaoInterface.getSingleRecipe(key)
    fun favouriteRecipe(recipe: Recipe) = recipeDaoInterface.favouriteRecipe(recipe)
    fun removeFavourite(recipe: Recipe) = recipeDaoInterface.removeFavourite(recipe)
    fun getUser() = recipeDaoInterface.getUser()

}
