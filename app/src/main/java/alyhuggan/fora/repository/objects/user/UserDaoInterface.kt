package alyhuggan.fora.repository.objects.user

import alyhuggan.fora.repository.objects.recipe.Recipe
import androidx.lifecycle.LiveData

interface UserDaoInterface {

    fun addUser(user: User)
    fun getUser(): LiveData<UserAccount>
    fun getUserRecipes(): LiveData<List<Recipe>>?
    fun login(user: User)
    fun logout()

}