package alyhuggan.fora.repository.objects.foods

import androidx.lifecycle.MutableLiveData
import com.google.firebase.database.*

private const val TAG = "ForaDaoImplementation"
private lateinit var database: DatabaseReference

private val foodList = MutableLiveData<List<FoodItem>>()
private val mutableFoodList = mutableListOf<FoodItem>()

class FoodDaoImplementation: FoodDaoInterface {

    init {
        updateRecyclerViewData()
        foodList.value = mutableFoodList
    }

    override fun getFoods() = foodList

    override fun addFood(foodItem: FoodItem) {

        val dataId = database.push().key
        if(dataId != null) {
            database.child(dataId).setValue(foodItem).addOnCompleteListener{
//                Log.d(TAG, "Database: Value added")
            }
        } else {
//            Log.d(TAG, "DataId equals null")
        }
    }

    private fun updateRecyclerViewData() {
//        Log.d(TAG, "updateRecyclerViewData: starts")

        database = FirebaseDatabase.getInstance().getReference("food")

        database.addListenerForSingleValueEvent(object:
            ValueEventListener {

            override fun onCancelled(p0: DatabaseError) {
//                Log.d(TAG, "getRecipes listener: onCancelled")
            }

            override fun onDataChange(databaseFoods: DataSnapshot) {
                if(databaseFoods.exists()) {
                    mutableFoodList.clear()
                    for(f in databaseFoods.children) {
                        val food = f.getValue(FoodItem::class.java)
                        if(food != null) {
                            mutableFoodList.add(food)
                        }
                    }
                }
                foodList.postValue(mutableFoodList)
            }
        })
    }
}