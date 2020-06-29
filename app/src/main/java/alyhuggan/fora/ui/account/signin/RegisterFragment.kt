package alyhuggan.fora.ui.account.signin

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import alyhuggan.fora.R
import alyhuggan.fora.repository.objects.user.User
import alyhuggan.fora.viewmodels.user.UserViewModel
import alyhuggan.fora.viewmodels.user.UserViewModelFactory
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.NavController
import androidx.navigation.Navigation
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_register.*
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.closestKodein
import org.kodein.di.generic.instance

private const val TAG = "RegisterFragment"

class RegisterFragment : AccountParentFragment(), KodeinAware {

    override val kodein by closestKodein()
    private val viewModelFactory by instance<UserViewModelFactory>()
    private lateinit var viewModel: UserViewModel
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_register, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        navController = Navigation.findNavController(activity!!, R.id.nav_host_fragment)
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(UserViewModel::class.java)

        checkUser()

        register_switch_page.setOnClickListener {
            navController.navigate(R.id.account_login)
        }

        register_button.setOnClickListener {

            val username = register_username.text.toString().trim()
            val email = register_email.text.toString().trim()
            val password = register_password.text.toString().trim()

            val listToCheck = listOf(username, email, password)

            if (emptyFieldCheck(listToCheck)) {
                val user = User(
                    email,
                    password,
                    username
                )
                val check = user.checkUsername(username)
                Log.d(TAG, "onViewCreated: $check")
                
                viewModel.addUser(user)!!.observe(viewLifecycleOwner, Observer { exception ->
                    if (exception != "" && exception != null) {
                        Log.d(TAG, "onViewCreated: exception = $exception")
                        val message = errorMessage(exception)
                        Toast.makeText(context, message, Toast.LENGTH_LONG).show()
                        viewModel.addUser(user)!!.removeObservers(viewLifecycleOwner)
                    }
                })
            } else {
                Toast.makeText(
                    context,
                    "Please don't leave any fields empty",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun errorMessage(error: String): String? {
        return when (error) {
            getString(R.string.email_format) -> {
                getString(R.string.email_message)
            }
            getString(R.string.email_exists) -> {
                getString(R.string.register_email_message)
            }
            getString(R.string.password_format) -> {
                getString(R.string.register_password_message)
            }
            else -> {
                getString(R.string.unexpected_error)
            }
        }
    }
}