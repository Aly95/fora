package alyhuggan.fora.ui.account.signin

import alyhuggan.fora.R
import alyhuggan.fora.viewmodels.user.UserViewModel
import alyhuggan.fora.viewmodels.user.UserViewModelFactory
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.NavController
import androidx.navigation.Navigation
import kotlinx.android.synthetic.main.activity_main.*
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.closestKodein
import org.kodein.di.generic.instance

private const val TAG = "AccountParentFragment"

open class AccountParentFragment : Fragment(), KodeinAware {

    override val kodein by closestKodein()
    private val viewModelFactory by instance<UserViewModelFactory>()
    private lateinit var viewModel: UserViewModel
    private lateinit var navController: NavController

    override fun onResume() {
        super.onResume()
        Log.d(TAG, "onResume: called")
        activity!!.main_toolbar.visibility = View.GONE
    }

    fun emptyFieldCheck(textList: List<String>): Boolean {
        textList.forEach { text ->
            if(text == "") {
                return true
            }
        }
        return false
    }

    fun checkUser() {

        viewModel =
            ViewModelProviders.of(this, viewModelFactory).get(UserViewModel::class.java)

        viewModel.getUser().observe(viewLifecycleOwner, Observer {
            if (it.email != "") {
                navigateToAccount()
            }
        })
    }

    fun navigateToAccount() {
        navController = Navigation.findNavController(activity!!, R.id.nav_host_fragment)
        navController.navigate(R.id.login)
    }
}