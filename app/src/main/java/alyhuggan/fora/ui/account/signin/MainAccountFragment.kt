package alyhuggan.fora.ui.account.signin

import alyhuggan.fora.R
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import kotlinx.android.synthetic.main.fragment_main_account.*

class MainAccountFragment: AccountParentFragment() {

    private lateinit var navController: NavController

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_main_account, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        navController = Navigation.findNavController(activity!!, R.id.nav_host_fragment)

        mainpage_login_button.setOnClickListener {
            navController.navigate(R.id.account_login)
        }

        mainpage_register_button.setOnClickListener {
            navController.navigate(R.id.register)
        }
    }




}