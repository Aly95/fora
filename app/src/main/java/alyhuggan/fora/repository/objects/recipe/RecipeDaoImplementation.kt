package alyhuggan.fora.repository.objects.recipe

import alyhuggan.fora.repository.objects.foods.FoodItem
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.database.*

private const val TAG = "ForaDaoImplementation"
private lateinit var database: DatabaseReference

val recipeList = MutableLiveData<List<Recipe>>()
val foodList = MutableLiveData<List<FoodItem>>()

val mutableRecipeList = mutableListOf<Recipe>()
val mutableFoodList = mutableListOf<FoodItem>()

class ForaDaoImplementation :
    RecipeDaoInterface {

    init {
        updateRecyclerViewData()
        recipeList.value = mutableRecipeList
        foodList.value = mutableFoodList
    }

    override fun getRecipes() = recipeList

    override fun getFoods(): LiveData<List<FoodItem>> {
        Log.d(TAG, "getFoods: starts")
        return foodList
    }

    override fun addRecipe(recipe: Recipe) {

        val dataId = database.push().key

        if(dataId != null) {

            database.child(dataId).setValue(recipe).addOnCompleteListener{
//                Log.d(TAG, "Database: Value added")
            }
        } else {
//            Log.d(TAG, "DataId equals null")
        }

    }

    override fun addFood(foodItem: FoodItem) {
//        Log.d(TAG, "addFood: starts")
    }

    private fun updateRecyclerViewData() {
//        Log.d(TAG, "updateRecyclerViewData: starts")

        database = FirebaseDatabase.getInstance().getReference("recipe")

        database.addListenerForSingleValueEvent(object: ValueEventListener{

            override fun onCancelled(p0: DatabaseError) {
//                Log.d(TAG, "getRecipes listener: onCancelled")
            }

            override fun onDataChange(databaseRecipes: DataSnapshot) {
                if(databaseRecipes.exists()) {
                    mutableRecipeList.clear()
                    for(r in databaseRecipes.children) {
                        val recipe = r.getValue(Recipe::class.java)
                        if(recipe != null) {
                            mutableRecipeList.add(recipe)
                        }
                    }
                }
                recipeList.postValue(mutableRecipeList)
            }
        })
    }
}