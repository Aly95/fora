package alyhuggan.fora.ui

import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.activity_main.*

private const val TAG = "MainFragment"

open class MainFragment: Fragment() {

    override fun onResume() {
        super.onResume()
        Log.d(TAG, "onDestroy: called")
        activity!!.main_toolbar.visibility = View.VISIBLE
    }
}