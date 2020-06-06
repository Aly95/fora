package alyhuggan.fora.ui.misc

import alyhuggan.fora.R
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        inflateMenu()
        val navController = Navigation.findNavController(this, R.id.nav_host_fragment)
        setUpBottomNav(navController)
    }

    private fun inflateMenu() {
        val toolbar: androidx.appcompat.widget.Toolbar = findViewById(R.id.main_toolbar)
        toolbar.inflateMenu(R.menu.menu_add)
    }

    private fun setUpBottomNav(navController: NavController) {
        bottom_nav_bar?.let {
            NavigationUI.setupWithNavController(it, navController)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val navController = Navigation.findNavController(this, R.id.nav_host_fragment)
        val navigated = NavigationUI.onNavDestinationSelected(item!!, navController)
        return navigated || super.onOptionsItemSelected(item)
    }
}