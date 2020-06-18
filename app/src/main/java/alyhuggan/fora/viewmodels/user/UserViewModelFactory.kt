package alyhuggan.fora.viewmodels.user

import alyhuggan.fora.repository.objects.recipe.RecipeDaoInterface
import alyhuggan.fora.repository.objects.user.UserDaoInterface
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class UserViewModelFactory(private val userDaoInterface: UserDaoInterface)
    : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return UserViewModel(userDaoInterface) as T
    }
}