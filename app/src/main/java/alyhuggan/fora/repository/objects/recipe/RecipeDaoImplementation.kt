package alyhuggan.fora.repository.objects.recipe

import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask

private const val TAG = "ForaDaoImplementation"
private lateinit var database: DatabaseReference
private lateinit var userDatabase: DatabaseReference
private lateinit var storageRef: StorageReference

private val recipeList = MutableLiveData<List<Recipe>>()
private val mutableRecipeList = mutableListOf<Recipe>()
private val key = MutableLiveData<String>()

private val recipeKeyList = MutableLiveData<ArrayList<Recipe>>()
private var snapshot: DataSnapshot? = null

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

    override fun addRecipe(recipe: Recipe): LiveData<String>? {

        val dataId = database.push().key
        val userId = "User"

        if(dataId != null) {
            var recipeUpload: Recipe
            if (recipe.photo != null) {
                val photoRef = storageRef.child(dataId)
                val uri: Uri = Uri.parse(recipe.photo)

                photoRef.putFile(uri).addOnSuccessListener { taskSnapshot ->

                    val url = taskSnapshot.metadata!!.path
                    val getFile = Uri.parse(url)

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
                    }
                    userDatabase.child(userId).setValue(dataId).addOnCompleteListener {
                    }
                }
            } else {
                recipeUpload = recipe
                database.child(dataId).setValue(recipeUpload).addOnCompleteListener {
                }
                userDatabase.child(userId).setValue(dataId).addOnCompleteListener {
                }
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