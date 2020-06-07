package alyhuggan.fora.viewmodels.food

import alyhuggan.fora.repository.objects.foods.FoodDaoInterface
import alyhuggan.fora.repository.objects.foods.FoodItem
import alyhuggan.fora.repository.objects.recipe.Recipe
import alyhuggan.fora.repository.objects.recipe.RecipeDaoInterface
import androidx.lifecycle.ViewModel

class FoodViewModel(private val foodDaoInterface: FoodDaoInterface) : ViewModel() {

    fun getFoods() = foodDaoInterface.getFoods()
    fun addFoods(food: FoodItem) = foodDaoInterface.addFood(food)

}
