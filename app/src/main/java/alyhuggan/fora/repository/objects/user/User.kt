package alyhuggan.fora.repository.objects.user

import android.renderscript.Sampler
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.database.*

data class User(val email: String, val password: String, val username: String = "") {

    private lateinit var userDatabase: DatabaseReference
    private var usernameExists: String = ""
    private var liveUsernameExists = MutableLiveData<String>("")

    init {
        liveUsernameExists.value = usernameExists
    }

    private val userDao = UserDaoImplementation()

    fun checkUsername(username: String): LiveData<String> {

        userDatabase = FirebaseDatabase.getInstance().getReference("user")
        userDatabase.addListenerForSingleValueEvent(object: ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {
                TODO("Not yet implemented")
            }

            override fun onDataChange(userAccountList: DataSnapshot) {
                if(userAccountList.exists()) {
                    var found = false
                    userAccountList.children.forEach {
                        val account = it.getValue(UserAccount::class.java)
                        if(account!!.userName == username) {
                            found = true
                        }
                    }
                    if(found) {
                        liveUsernameExists.postValue("true")
                    } else {
                        liveUsernameExists.postValue("false")
                    }
                    resetLiveUsername()
                }
            }
        })
        return liveUsernameExists
    }

    private fun resetLiveUsername() {
        liveUsernameExists.value = ""
    }
}