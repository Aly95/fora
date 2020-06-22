package alyhuggan.fora.repository.objects.recipe

import alyhuggan.fora.repository.objects.foods.FoodItem
import alyhuggan.fora.repository.objects.user.UserAccount
import alyhuggan.fora.repository.objects.user.UserItems
import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

private const val TAG = "RecipeDaoImplementation"
private lateinit var database: DatabaseReference
private lateinit var userDatabase: DatabaseReference
private lateinit var storageRef: StorageReference

private val recipeList = MutableLiveData<List<Recipe>>()
private val mutableRecipeList = mutableListOf<Recipe>()
private val key = MutableLiveData<String>()

private val user = UserAccount()

private val recipeKeyList = MutableLiveData<ArrayList<Recipe>>()
private var snapshot: DataSnapshot? = null

private lateinit var auth: FirebaseAuth

class RecipeDaoImplementation :
    RecipeDaoInterface {

    init {
        updateRecyclerViewData()
        recipeList.value = mutableRecipeList
    }

    override fun getRecipes() = recipeList

    override fun getSingleRecipe(key: String): LiveData<Recipe> {
        val recipe = MutableLiveData<Recipe>()
        recipe.value = snapshot!!.child(key).getValue(Recipe::class.java)
        return recipe
    }

    private fun updateUser(recipe: Recipe, dataId: String) {

        var recipeItems: ArrayList<UserItems> = ArrayList<UserItems>()
        var foodItems: ArrayList<UserItems> = ArrayList<UserItems>()
        var userAccount: UserAccount = UserAccount()

        auth = FirebaseAuth.getInstance()

        val userId = auth.currentUser!!.uid
        val email = auth.currentUser!!.email

        userDatabase.child(auth.uid!!).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                TODO("Not yet implemented")
            }

            override fun onDataChange(user: DataSnapshot) {

                val dataSnapshotAccount = user.getValue(UserAccount::class.java)

                if (dataSnapshotAccount != null) {
                    if(dataSnapshotAccount.recipeList != emptyList<UserItems>()) {
                        recipeItems = dataSnapshotAccount.recipeList as ArrayList<UserItems>
                    }
                    if(dataSnapshotAccount.foodList != emptyList<UserItems>()) {
                        foodItems = dataSnapshotAccount.foodList as ArrayList<UserItems>
                    }
                }
                recipeItems.add(
                    UserItems(
                        dataId,
                        recipe.rating!![0]
                    )
                )

                if(recipe.foods != emptyList<FoodItem>()) {
                    recipe.foods!!.forEach { foodItem ->
                        foodItems.add(
                            UserItems(
                                "${foodItem.brand} & ${foodItem.name}"
                            )
                        )
                    }
                }

                userAccount = UserAccount(
                    "Username",
                    email!!,
                    recipeItems,
                    foodItems
                )

                userDatabase.child(userId).setValue(userAccount).addOnCompleteListener {
                    Log.d(TAG, "user updated")
                }.addOnFailureListener {
                    Log.d(TAG, "failure because $it")
                }
                userDatabase.child(userId).setValue(userAccount)
            }
        })
    }

    override fun addRecipe(recipe: Recipe): LiveData<String>? {

//        val userId = auth.currentUser!!.uid
        val dataId = database.push().key

        if (dataId != null) {
            var recipeUpload: Recipe
            if (recipe.photo != null) {
                val photoRef = storageRef.child(dataId)
                val uri: Uri = Uri.parse(recipe.photo)

                photoRef.putFile(uri).addOnSuccessListener { taskSnapshot ->

                    val url = taskSnapshot.metadata!!.path
//                    val getFile = Uri.parse(url)

                    val test = storageRef.child("photos").getFile(uri)

                    Log.d(TAG, "Url = $test")

                    recipeUpload = Recipe(
                        recipe.title,
                        recipe.rating,
                        url,
                        recipe.type,
                        recipe.foods,
                        recipe.method
                    )

                    database.child(dataId).setValue(recipeUpload).addOnCompleteListener {
                        updateUser(recipeUpload, dataId)
                    }
                }
            } else {
                recipeUpload = recipe
                database.child(dataId).setValue(recipeUpload).addOnCompleteListener {
                    updateUser(recipeUpload, dataId)
                }
//                userDatabase.child(userId).setValue(dataId).addOnCompleteListener {
//                }
            }
        }
        key.value = dataId
        return key
    }

    private fun updateRecyclerViewData() {
//        Log.d(TAG, "updateRecyclerViewData: starts")

        database = FirebaseDatabase.getInstance().getReference("recipe")
        userDatabase = FirebaseDatabase.getInstance().getReference("user")
        storageRef = FirebaseStorage.getInstance().getReference("photos")

        database.addValueEventListener(object : ValueEventListener {

            override fun onCancelled(p0: DatabaseError) {
//                Log.d(TAG, "getRecipes listener: onCancelled")
            }

            override fun onDataChange(databaseRecipes: DataSnapshot) {

                snapshot = databaseRecipes

                if (databaseRecipes.exists()) {
                    mutableRecipeList.clear()
                    for (r in databaseRecipes.children) {
                        val recipe = r.getValue(Recipe::class.java)
                        if (recipe != null) {
                            mutableRecipeList.add(recipe)
                        }
                    }
                }
                recipeList.postValue(mutableRecipeList)
            }
        })
    }
}