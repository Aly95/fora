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

//    override fun onResume() {
//        super.onResume()
//        activity!!.main_toolbar.visibility = View.GONE
//    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        navController = Navigation.findNavController(activity!!, R.id.nav_host_fragment)
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(UserViewModel::class.java)

        checkUser()

        register_switch_page.setOnClickListener {
            navController.navigate(R.id.account_login)
        }

        register_button.setOnClickListener {

            val email = register_email.text.toString().trim()
            val password = register_password.text.toString().trim()

            if (emptyFieldCheck(email) && emptyFieldCheck(password)) {
                val user = User(
                    email,
                    password
                )
                viewModel.addUser(user)
            }
        }

    }
}