package alyhuggan.fora.ui.account.signin

import alyhuggan.fora.R
import alyhuggan.fora.repository.objects.user.User
import alyhuggan.fora.viewmodels.user.UserViewModel
import alyhuggan.fora.viewmodels.user.UserViewModelFactory
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.NavController
import androidx.navigation.Navigation
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_login.*
import kotlinx.android.synthetic.main.fragment_main_account.*
import kotlinx.android.synthetic.main.fragment_register.*
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.closestKodein
import org.kodein.di.generic.instance

private const val TAG = "LoginFragment"

class LoginFragment : AccountParentFragment(), KodeinAware {

    override val kodein by closestKodein()
    private val viewModelFactory by instance<UserViewModelFactory>()
    private lateinit var viewModel: UserViewModel
    private lateinit var navController: NavController

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        navController = Navigation.findNavController(activity!!, R.id.nav_host_fragment)
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(UserViewModel::class.java)

        checkUser()

        login_switch_page.setOnClickListener {
            navController.navigate(R.id.register)
        }

        login_button.setOnClickListener {

            val email = login_email.text.toString().trim()
            val password = login_password.text.toString().trim()

            val list = listOf(email, password)

            Log.d(TAG, "onViewCreated: password = $password")

            if (emptyFieldCheck(list)) {
                val user = User(
                    email,
                    password
                )
                viewModel.login(user)?.observe(viewLifecycleOwner, Observer { string ->
                    if (string != "") {
                        val message = errorMessage(string)
                        Log.d(TAG, "onViewCreated: message = $message")
                        Toast.makeText(context, message, Toast.LENGTH_LONG).show()
                        viewModel.login(user)!!.removeObservers(viewLifecycleOwner)

                    }
                })
            } else {
                Toast.makeText(
                    context,
                    "Please enter an email and password",
                    Toast.LENGTH_SHORT
                ).show()
            }

        }
    }

    private fun errorMessage(error: String): String {
        when (error) {
            getString(R.string.email_format) -> {
                return getString(R.string.email_message)
            }
            getString(R.string.invalid_password) -> {
                return getString(R.string.password_message)
            }
            getString(R.string.invalid_username) -> {
                return getString(R.string.username_message)
            }
            getString(R.string.too_many_requests) -> {
                return getString(R.string.requests_message)
            }
            else -> {
                return getString(R.string.unexpected_error)
            }
        }
    }
}