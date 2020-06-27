package alyhuggan.fora.repository.objects.user

import alyhuggan.fora.repository.objects.recipe.Recipe
import alyhuggan.fora.repository.objects.recipe.RecipeDaoImplementation
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

private const val TAG = "UserDaoImplementation"

private val liveUser = MutableLiveData<UserAccount>()
private var user = UserAccount()

private lateinit var auth: FirebaseAuth
private lateinit var recipeDatabase: DatabaseReference
private lateinit var userDatabase: DatabaseReference

private val recipeList = MutableLiveData<List<Recipe>>()
private val mutableRecipeList = mutableListOf<Recipe>()

class UserDaoImplementation : UserDaoInterface {

    init {
        liveUser.value = user
        auth = FirebaseAuth.getInstance()
        initialize()
    }

    private fun initialize() {
        getUser()
    }

    override fun addUser(user: User) {
        Log.d(TAG, "addUser: called")

        auth.createUserWithEmailAndPassword(user.email, user.password).addOnSuccessListener {
            Log.d(TAG, "addUser() Task Successful")
            getUser()
        }.addOnFailureListener {
            Log.d(TAG, "addUser() Task has failed - exception = $it")
        }
    }

    override fun getUser(): LiveData<UserAccount> {

        userDatabase = FirebaseDatabase.getInstance().getReference("user")

        auth = FirebaseAuth.getInstance()

        if (auth.currentUser != null) {
            Log.d(TAG, "getUser() != null")

            val userDb = userDatabase.child(auth.currentUser!!.uid)
            userDb.addValueEventListener(object: ValueEventListener{
                override fun onCancelled(p0: DatabaseError) {
                    TODO("Not yet implemented")
                }

                override fun onDataChange(usersSnapshot: DataSnapshot) {
                    if(usersSnapshot.exists()) {
//                        Log.d(TAG, "Usersnapshot = $usersSnapshot")
                        val userClass = usersSnapshot.getValue(UserAccount::class.java)

//                        Log.d(TAG, "User = $userClass")

                        user = UserAccount(
                            userClass!!.userName,
                            userClass.email,
                            userClass.recipeList,
                            userClass.foodList
                        )
                    } else {
                        user = UserAccount(
                            auth.currentUser!!.uid,
                            auth.currentUser!!.email!!
                        )
                    }
                    liveUser.postValue(user)
                }
            })

        } else {
            Log.d(TAG, "getUser() No user currently logged in")
            user = UserAccount()
        }
        liveUser.postValue(user)
        return liveUser
    }


    override fun getUserRecipes(): LiveData<List<Recipe>>? {

        val recipe = RecipeDaoImplementation()
        val recipeList = recipe.getRecipes()
        Log.d(TAG, "RecipeList = $recipeList")
        return null
    }

    override fun login(userDetails: User) {
        auth = FirebaseAuth.getInstance()
        auth.signInWithEmailAndPassword(userDetails.email, userDetails.password).addOnSuccessListener {
            getUser()
            Log.d(TAG, "login() User signed in ")
        }.addOnFailureListener {
            Log.d(TAG, "login: Failed with details ${userDetails}")
        }
    }

    override fun logout() {
        auth.signOut()
        getUser()
    }
}