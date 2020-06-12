package alyhuggan.fora.repository.objects.foods

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.database.*

private const val TAG = "FoodDaoImplementation"
private lateinit var database: DatabaseReference

private val foodList = MutableLiveData<List<FoodItem>>()
private val mutableFoodList = mutableListOf<FoodItem>()

private val databaseStructureBrands = MutableLiveData<List<String>>()
private val mutableDatabaseStructureBrands = mutableListOf<String>()

private val databaseStructureFoods = MutableLiveData<List<String>>()
private val mutableDatabaseStructureFoods = mutableListOf<String>()

class FoodDaoImplementation : FoodDaoInterface {

    init {
        updateRecyclerViewData()
        foodList.value = mutableFoodList
        databaseStructureBrands.value = mutableDatabaseStructureBrands
        databaseStructureFoods.value = mutableDatabaseStructureFoods
    }

    override fun getFoods() = foodList

    override fun checkBrand() = databaseStructureBrands

    override fun checkFood() = databaseStructureFoods

    override fun updateRecipe(foodItem: FoodItem) {

        var recipeList = ArrayList<String?>()

        val result = mutableFoodList.forEach {
            if(it.brand == foodItem.brand && it.name == foodItem.name) {
                    recipeList = it.recipeKeyList
                    recipeList.add(foodItem.recipeKeyList[0])
                }
                database.child(foodItem.brand!!).child(foodItem.name).child("recipeKeyList").setValue(recipeList)
        }
    }

    override fun addFood(foodItem: FoodItem) {

        var brand = ""
        brand = foodItem.brand ?: "Generic"
        database.child(brand).child(foodItem.name).setValue(foodItem)
    }
}

private fun updateRecyclerViewData() {
//        Log.d(TAG, "updateRecyclerViewData: starts")

    database = FirebaseDatabase.getInstance().getReference("food")

    database.addValueEventListener(object :
        ValueEventListener {

        override fun onCancelled(p0: DatabaseError) {
//                Log.d(TAG, "getRecipes listener: onCancelled")
        }

        override fun onDataChange(databaseFoods: DataSnapshot) {
            if (databaseFoods.exists()) {
                mutableFoodList.clear()
                mutableDatabaseStructureBrands.clear()
                mutableDatabaseStructureFoods.clear()

                val brandList = databaseFoods.children
                brandList.forEach { brand ->
                    mutableDatabaseStructureBrands.add(brand.key!!)
                    val dbFoodList = brand.children
                    dbFoodList.forEach { food ->
                        mutableFoodList.add(food.getValue(FoodItem::class.java)!!)
                        mutableDatabaseStructureFoods.add(food.key!!)
                    }

                }
            }
        }
    })
}
