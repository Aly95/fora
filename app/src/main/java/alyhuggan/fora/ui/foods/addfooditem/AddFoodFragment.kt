package alyhuggan.fora.ui.foods.addfooditem

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import alyhuggan.fora.R
import alyhuggan.fora.repository.objects.foods.FoodItem
import alyhuggan.fora.repository.objects.foods.FoodName
import alyhuggan.fora.ui.recipe.addrecipe.AddRecipeFragment
import alyhuggan.fora.viewmodels.food.FoodViewModel
import alyhuggan.fora.viewmodels.food.FoodViewModelFactory
import alyhuggan.fora.viewmodels.recipe.RecipeViewModel
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.util.Log
import android.widget.CheckBox
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.fragment_add_food.*
import kotlinx.android.synthetic.main.fragment_add_food.add_recipe_btn_cancel
import kotlinx.android.synthetic.main.fragment_add_food.add_recipe_btn_submit
import kotlinx.android.synthetic.main.fragment_add_recipe_scrollview.*
import kotlinx.android.synthetic.main.items_fooditem.view.*
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.closestKodein
import org.kodein.di.generic.instance

private const val TAG = "AddFoodFragment"

class AddFoodFragment : Fragment(), KodeinAware {

    override val kodein by closestKodein()
    private val viewModelFactory by instance<FoodViewModelFactory>()

    private var checkboxTag = "Unsure"
    private var selectedImageURI: Uri? = null

    private lateinit var navController: NavController


    companion object {
        private const val SELECT_PICTURE = 999
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_food, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initializeUi()
    }

    private fun initializeUi() {

        populateUserCardView(view!!)
        getTags()
        setUpNavigation()

    }

    private fun setUpNavigation() {

        navController = Navigation.findNavController(activity!!, R.id.nav_host_fragment)

        add_recipe_btn_submit.setOnClickListener {
            val food = getFoodObject()
            addFoodItem()
        }

        add_recipe_btn_cancel.setOnClickListener {
            navController.popBackStack()
        }
    }

    private fun addFoodItem() {

        val viewModel =
            ViewModelProviders.of(this, viewModelFactory).get(FoodViewModel::class.java)

        val food = getFoodObject()

        viewModel.addFoods(food)

        navController.popBackStack()

    }

    private fun checkPhoto(): String? {
        return if(selectedImageURI != null) {
            selectedImageURI.toString()
        } else {
            null
        }
    }

    private fun getFoodObject(): FoodItem{

        val foodDetails = getFoodDetails()
        val brand = foodDetails.brand
        val name = foodDetails.name
        val rating = listOf<Double>(add_food_ratingbar.rating.toDouble())
        val photo = checkPhoto()

        return FoodItem(
            checkboxTag,
            name,
            brand,
            null,
            ArrayList<String?>(),
            rating,
            photo
        )
    }

    private fun format(string: String) = string.capitalize().trim()

    private fun populateUserCardView(view: View) {
        val cardView = view.findViewById<View>(R.id.card_add)
        val txtView = cardView.findViewById<TextView>(R.id.item_card_name)
        val imgView = cardView.findViewById<ImageView>(R.id.item_card_image)

        imgView.setImageResource(R.drawable.food)
        txtView.text = getString(R.string.item_card_title_default_txt)

        cardView.setOnClickListener {
            launchGallery()
        }
    }

    private fun getTags() {
        val checkboxList = getCheckboxList()
        checkboxList.forEach { box ->
            box.setOnClickListener {
                checkboxTag = box.text.toString()
            }
        }
    }

    private fun getCheckboxList(): List<CheckBox>{
        return listOf<CheckBox>(
            checkbox_protein,
            checkbox_starch,
            checkbox_fruitAndVeg,
            checkbox_condiments,
            checkbox_other,
            checkbox_unsure
        )
    }

    private fun getFoodDetails(): FoodName {
        var brand = format(addFood.recipe_fooditem_brand.text.toString())
        if(brand == "") {
            brand = "Generic"
        }
        val name = format(addFood.recipe_fooditem_brand_food.text.toString())
        return FoodName(brand, name)
    }

    private fun launchGallery() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(
            intent,
            SELECT_PICTURE
        )
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        val cardView = view!!.findViewById<View>(R.id.card_add)
        val imgView = cardView.findViewById<ImageView>(R.id.item_card_image)

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == SELECT_PICTURE) {
                selectedImageURI = data!!.data!!
                Glide.with(activity!!).load(selectedImageURI).centerCrop().into(imgView)
            }
        }
    }

}