package alyhuggan.fora.repository.objects.user

import android.renderscript.Sampler
import com.google.firebase.database.*

data class User(val email: String, val password: String, val username: String = "") {

    private lateinit var userDatabase: DatabaseReference


    fun checkUsername(username: String): Boolean {
        var usernameExists = false
        userDatabase = FirebaseDatabase.getInstance().getReference("user")
        userDatabase.addListenerForSingleValueEvent(object: ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {
                TODO("Not yet implemented")
            }

            override fun onDataChange(userAccountList: DataSnapshot) {
                if(userAccountList.exists()) {
                    userAccountList.children.forEach {
                        val account = it.getValue(UserAccount::class.java)
                        if(account!!.userName == username) {
                            usernameExists = true
                        }
                    }
                }
            }
        })
        return usernameExists
    }
}