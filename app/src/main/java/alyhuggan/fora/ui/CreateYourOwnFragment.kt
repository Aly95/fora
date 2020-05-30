package alyhuggan.fora.ui

import alyhuggan.fora.R
import alyhuggan.fora.repository.objects.FoodItem
import alyhuggan.fora.repository.objects.Quantity
import alyhuggan.fora.repository.objects.Recipe
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

class CreateYourOwnFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_create_your_own, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setToolbarTitle()
        initializeUi()
    }

    private fun initializeUi() {


    }

    private fun setToolbarTitle() {
        val toolbar: TextView = activity!!.findViewById(R.id.maintoolbar_title)
        toolbar.text = "Add Foods"
    }
}