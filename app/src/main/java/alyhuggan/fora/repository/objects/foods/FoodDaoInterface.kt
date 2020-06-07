package alyhuggan.fora.repository.objects.foods

import androidx.lifecycle.LiveData

interface FoodDaoInterface {
    fun getFoods(): LiveData<List<FoodItem>>
    fun addFood(foodItem: FoodItem)
}