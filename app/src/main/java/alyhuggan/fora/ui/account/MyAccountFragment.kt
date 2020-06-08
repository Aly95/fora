package alyhuggan.fora.ui.account

import alyhuggan.fora.R
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.navigation.Navigation
import kotlinx.android.synthetic.main.items_card.view.*

class MyAccountFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_my_account, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setToolbarTitle()
        initializeUi()
    }

    private fun initializeUi() {

        val navController = Navigation.findNavController(activity!!, R.id.nav_host_fragment)

        val cardView = view!!.findViewById<View>(R.id.card_add)
        val imgView = cardView.findViewById<ImageView>(R.id.item_card_image)
        val txtView = cardView.findViewById<TextView>(R.id.item_card_title)

        txtView.text = "Add Recipe"

        imgView.setImageResource(R.drawable.upload)
        imgView.scaleType = ImageView.ScaleType.FIT_CENTER

        imgView.setOnClickListener{
            navController.navigate(R.id.add_recipe)
        }

    }

    private fun setToolbarTitle() {
//        val toolbar: TextView = activity!!.findViewById(R.id.maintoolbar_title)
//        toolbar.text = "Add Foods"
    }
}