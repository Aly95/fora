package alyhuggan.fora.viewmodels.user

import alyhuggan.fora.repository.objects.user.User
import alyhuggan.fora.repository.objects.user.UserAccount
import alyhuggan.fora.repository.objects.user.UserDaoInterface
import androidx.lifecycle.ViewModel

class UserViewModel(private val userDaoInterface: UserDaoInterface) : ViewModel() {

    fun getUser() = userDaoInterface.getUser()
    fun addUser(user: User) = userDaoInterface.addUser(user)
    fun login(user: User) = userDaoInterface.login(user)
    fun logOut() = userDaoInterface.logout()

}
