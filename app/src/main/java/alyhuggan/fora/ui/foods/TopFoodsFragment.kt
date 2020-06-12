package alyhuggan.fora.ui.foods

import alyhuggan.fora.R
import alyhuggan.fora.repository.objects.foods.Quantity
import alyhuggan.fora.repository.objects.foods.FoodItem
import alyhuggan.fora.ui.foods.recyclerviewadapters.FoodHorizontalRecyclerViewAdapter
import alyhuggan.fora.viewmodels.food.FoodViewModel
import alyhuggan.fora.viewmodels.food.FoodViewModelFactory
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.view.animation.LayoutAnimationController
import android.widget.EditText
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.fragment_top_foods.*
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.closestKodein
import org.kodein.di.generic.instance

private const val TAG = "TopFoodsFragment"
private lateinit var auth: FirebaseAuth

class TopFoodsFragment : Fragment(), KodeinAware {

    override val kodein by closestKodein()
    private val viewModelFactory by instance<FoodViewModelFactory>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_top_foods, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setSearchHint()
        initializeUi()
    }

    private fun initializeUi() {

        val viewModel = ViewModelProviders.of(this, viewModelFactory).get(FoodViewModel::class.java)
        val foodList = ArrayList<FoodItem>()

        viewModel.getFoods().observe(viewLifecycleOwner, Observer { foods ->
            foodList.clear()
            if(foods.isNotEmpty()) {
               foods.forEach {
                   foodList.add(it)
               }
            }
        })

        foods_recycler_view.layoutManager = LinearLayoutManager(context)
        val resId = R.anim.example
        val animation: LayoutAnimationController =
            AnimationUtils.loadLayoutAnimation(context, resId)
        foods_recycler_view.layoutAnimation = animation


        foods_recycler_view.adapter =
            FoodHorizontalRecyclerViewAdapter(
                foodList,
                context!!,
                activity!!
            )
        foods_recycler_view.setHasFixedSize(true)
    }

    private fun setSearchHint() {
        val searchbox: EditText = activity!!.findViewById(R.id.searchbox_text)
        searchbox.hint = "Search Individual Foods"
    }
}