package alyhuggan.fora.repository.objects.foods

import alyhuggan.fora.repository.objects.user.UserAccount
import alyhuggan.fora.repository.objects.user.UserItems
import android.net.Uri
import android.renderscript.Sampler
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
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
private lateinit var auth: FirebaseAuth
private lateinit var storageRef: StorageReference
private lateinit var userDatabase: DatabaseReference

private var user = UserAccount()

class FoodDaoImplementation : FoodDaoInterface {

    init {
        updateRecyclerViewData()
        getUser()
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
                return
            }
        }

        val key = "${foodItem.brand} & ${foodItem.name}"

        if (foodItem.url != null) {
            val photoRef = storageRef.child(key)
            val uri: Uri = Uri.parse(foodItem.url)

            photoRef.putFile(uri).addOnSuccessListener { taskSnapshot ->

                val url = taskSnapshot.metadata!!.path

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
                    .addOnSuccessListener {
                        uploadToUser(key, foodItem.rating!![0])
                    }
            }
        } else {
            database.child(brand).child(foodItem.name).setValue(foodItem)
                .addOnSuccessListener {
                    //TODO() Quick fix, update to accept nulls
                    if (foodItem.rating == emptyList<FoodItem>()) {
                        uploadToUser(key, 3.0)
                    } else {
                        uploadToUser(key, foodItem.rating!![0])
                    }
                }
        }
    }
}


private fun getUser() {

    auth = FirebaseAuth.getInstance()

    if (auth.currentUser != null) {

        userDatabase.child(auth.currentUser!!.uid)
            .addValueEventListener(object : ValueEventListener {
                override fun onCancelled(p0: DatabaseError) {
                    TODO("Not yet implemented")
                }

                override fun onDataChange(usersSnapshot: DataSnapshot) {
                    if (usersSnapshot.value != null) {
                        user = usersSnapshot.getValue(UserAccount::class.java)!!
                        Log.d(TAG, "user = $user")
                    }
                }
            })
    }
}

private fun uploadToUser(key: String, rating: Double) {

    val uid: String
    var foodList = ArrayList<UserItems>()
    var recipeList = ArrayList<UserItems>()
    val userUpload: UserAccount

    auth = FirebaseAuth.getInstance()
    uid = auth.currentUser!!.uid

    if (user.foodList != emptyList<UserItems>()) {
        foodList = user.foodList as ArrayList<UserItems>
    }
    if (user.recipeList != emptyList<UserItems>()) {
        recipeList = user.recipeList as ArrayList<UserItems>
    }

    foodList.add(
        UserItems(
            key,
            rating
        )
    )

    userUpload = UserAccount(
        uid,
        user.email,
        recipeList,
        foodList
    )
    userDatabase.child(uid).setValue(userUpload).addOnCompleteListener {
        Log.d(TAG, "Item Uploaded")
    }

}


private fun updateRecyclerViewData() {

    database = FirebaseDatabase.getInstance().getReference("food")
    userDatabase = FirebaseDatabase.getInstance().getReference("user")
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

