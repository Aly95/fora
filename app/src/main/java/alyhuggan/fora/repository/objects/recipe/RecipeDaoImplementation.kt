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

private var user = UserAccount()

private val recipeKeyList = MutableLiveData<ArrayList<Recipe>>()
private var snapshot: DataSnapshot? = null

private lateinit var auth: FirebaseAuth

class RecipeDaoImplementation :
    RecipeDaoInterface {

    init {
        updateRecyclerViewData()
        getUserDetails()
        recipeList.value = mutableRecipeList
    }
    private fun getUserDetails() {

        if(auth.currentUser != null) {
            Log.d(TAG, "getUserDetails: ${auth.currentUser}")
            userDatabase.child(auth.currentUser!!.uid).addValueEventListener(object: ValueEventListener{
                override fun onCancelled(p0: DatabaseError) {
                    TODO("Not yet implemented")
                }

                override fun onDataChange(userSnapshot: DataSnapshot) {
                    if(userSnapshot.exists()) {
                        user = userSnapshot.getValue(UserAccount::class.java)!!
                        Log.d(TAG, "onDataChange: user = $user")
                    } else {
                        Log.d(TAG, "onDataChange: null")
                    }
                }
            })
        } else {
            user = UserAccount()
            Log.d(TAG, "getUserDetails: null")
        }
    }

    override fun updateUserAccount() {
        getUserDetails()
    }

    override fun getUser() = user

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
                    if (dataSnapshotAccount.recipeList != emptyList<UserItems>()) {
                        recipeItems = dataSnapshotAccount.recipeList as ArrayList<UserItems>
                    }
                    if (dataSnapshotAccount.foodList != emptyList<UserItems>()) {
                        foodItems = dataSnapshotAccount.foodList as ArrayList<UserItems>
                    }
                }
                recipeItems.add(
                    UserItems(
                        dataId,
                        recipe.rating!![0],
                        true
                    )
                )

                if (recipe.foods != emptyList<FoodItem>()) {
                    recipe.foods!!.forEach { foodItem ->
                        Log.d(TAG, "onDataChange: adding foodItem")
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
            }
        })
    }

    override fun favouriteRecipe(recipe: Recipe) {

        auth = FirebaseAuth.getInstance()
        var userItem: UserItems = UserItems()
        var found: Boolean = false

        var recipeList = ArrayList<UserItems>()

        if (auth.currentUser != null) {
            val uid = auth.currentUser!!.uid
            if (user.recipeList != emptyList<UserItems>()) {
                recipeList = user.recipeList as ArrayList<UserItems>
            }

            recipeList.forEach { listUserItem ->
                if(listUserItem.key == recipe.id) {
                    listUserItem.favourited = true
                    found = true
                }
            }

            if(!found) {
                recipeList.add(
                    UserItems(
                        recipe.id,
                        null,
                        true
                    )
                )
            }

            userDatabase.child(uid).child("recipeList").setValue(recipeList)
        }
    }

    override fun removeFavourite(recipe: Recipe) {

        auth = FirebaseAuth.getInstance()

        var recipeList = ArrayList<UserItems>()

        if (auth.currentUser != null) {
            val uid = auth.currentUser!!.uid
            if (user.recipeList != emptyList<UserItems>()) {
                recipeList = user.recipeList as ArrayList<UserItems>
            }
//            recipeList.remove(UserItems(
//              recipe.id
//            ))

            recipeList.forEach {
                if(it.key == recipe.id) {
                    it.favourited = false
                }
            }

            userDatabase.child(uid).child("recipeList").setValue(recipeList)
        }
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
                        recipe.method,
                        dataId
                    )

                    database.child(dataId).setValue(recipeUpload).addOnCompleteListener {
                        updateUser(recipeUpload, dataId)
                    }
                }
            } else {
                recipeUpload = Recipe(
                    recipe.title,
                    recipe.rating,
                    null,
                    recipe.type,
                    recipe.foods,
                    recipe.method,
                    dataId
                )
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
        auth = FirebaseAuth.getInstance()

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