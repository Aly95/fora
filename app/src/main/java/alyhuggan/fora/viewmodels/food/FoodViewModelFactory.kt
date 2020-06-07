package alyhuggan.fora.viewmodels.food

import alyhuggan.fora.repository.objects.foods.FoodDaoInterface
import alyhuggan.fora.repository.objects.recipe.RecipeDaoInterface
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class FoodViewModelFactory(private val foodDaoInterface: FoodDaoInterface)
    : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return FoodViewModel(foodDaoInterface) as T
    }
}