package alyhuggan.fora.repository.objects.foods

import androidx.lifecycle.LiveData

interface FoodDaoInterface {
    fun getFoods(): LiveData<List<FoodItem>>
    fun getSingleFood(key: String): LiveData<FoodItem>
    fun checkBrand(): LiveData<List<String>>
    fun checkFood(): LiveData<List<String>>
    fun addFood(foodItem: FoodItem)
    fun updateRecipe(foodItem: FoodItem)
}