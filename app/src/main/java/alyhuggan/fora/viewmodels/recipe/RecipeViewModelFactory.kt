package alyhuggan.fora.viewmodels.recipe

import alyhuggan.fora.repository.objects.recipe.RecipeDaoInterface
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class RecipeViewModelFactory(private val recipeDaoInterface: RecipeDaoInterface)
    : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return RecipeViewModel(recipeDaoInterface) as T
    }
}