package alyhuggan.fora.repository.objects.foods

import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

private const val TAG = "FoodDaoImplementation"
private lateinit var database: DatabaseReference

private val foodList = MutableLiveData<List<FoodItem>>()
private val mutableFoodList = mutableListOf<FoodItem>()

private val databaseStructureBrands = MutableLiveData<List<String>>()
private val mutableDatabaseStructureBrands = mutableListOf<String>()

private val databaseStructureFoods = MutableLiveData<List<String>>()
private val mutableDatabaseStructureFoods = mutableListOf<String>()

private var snapshot: DataSnapshot? = null
private lateinit var storageRef: StorageReference

class FoodDaoImplementation : FoodDaoInterface {

    init {
        updateRecyclerViewData()
        foodList.value = mutableFoodList
        databaseStructureBrands.value = mutableDatabaseStructureBrands
        databaseStructureFoods.value = mutableDatabaseStructureFoods
    }

    override fun getFoods() = foodList

    override fun getSingleFood(key: String): LiveData<FoodItem> {
        val splitString = key.split(" & ")
        val brand = splitString[0]
        val name = splitString[1]
        val food = MutableLiveData<FoodItem>()
        food.value = snapshot!!.child(brand).child(name).getValue(FoodItem::class.java)
        return food
    }

    override fun checkBrand() = databaseStructureBrands

    override fun checkFood() = databaseStructureFoods

    override fun updateRecipe(foodItem: FoodItem) {

        var recipeList = ArrayList<String?>()

        val result = mutableFoodList.forEach {
            if (it.brand == foodItem.brand && it.name == foodItem.name) {
                recipeList = it.recipeKeyList
                recipeList.add(foodItem.recipeKeyList[0])
            }
            database.child(foodItem.brand!!).child(foodItem.name).child("recipeKeyList")
                .setValue(recipeList)
        }
    }

    override fun addFood(foodItem: FoodItem) {

        val brand = foodItem.brand ?: "Generic"

        mutableFoodList.forEach { savedFoodItem ->
            if (brand == savedFoodItem.brand && foodItem.name == savedFoodItem.name) {
                Log.d(TAG, "Food item already exists")
            } else {
                val key = "${foodItem.brand} ${foodItem.name}"

                if (foodItem.url != null) {
                    val photoRef = storageRef.child(key)
                    val uri: Uri = Uri.parse(foodItem.url)

                    photoRef.putFile(uri).addOnSuccessListener { taskSnapshot ->

                        val url = taskSnapshot.metadata!!.path
//                        val getFile = Uri.parse(url)

                        val test = storageRef.child("photos").getFile(uri)
                        Log.d(TAG, "Url = $test")

                        val foodUpload = FoodItem(
                            foodItem.type,
                            brand,
                            foodItem.name,
                            null,
                            foodItem.recipeKeyList,
                            foodItem.rating,
                            url
                        )
                        database.child(brand).child(foodItem.name).setValue(foodUpload)
                    }
                } else {
                    database.child(brand).child(foodItem.name).setValue(foodItem)
                }
        }
    }
}
}

private fun updateRecyclerViewData() {
//        Log.d(TAG, "updateRecyclerViewData: starts")

    database = FirebaseDatabase.getInstance().getReference("food")
    storageRef = FirebaseStorage.getInstance().getReference("photos")

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

                snapshot = databaseFoods

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

