package alyhuggan.fora.ui.recipe.addrecipe

import alyhuggan.fora.R
import alyhuggan.fora.repository.objects.Quantity
import alyhuggan.fora.repository.objects.foods.FoodItem
import alyhuggan.fora.repository.objects.recipe.Recipe
import alyhuggan.fora.ui.recipe.recyclerviewadapters.addrecipes.AddRecipeRecyclerViewAdapter
import alyhuggan.fora.ui.recipe.recyclerviewadapters.addrecipes.InstructionRecyclerViewAdapter
import alyhuggan.fora.viewmodels.recipe.RecipeViewModel
import alyhuggan.fora.viewmodels.recipe.RecipeViewModelFactory
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.items_recipe_fooditem.view.*
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.closestKodein
import org.kodein.di.generic.instance

private const val TAG = "AddRecipeFragment"

class AddRecipeFragment : Fragment(), KodeinAware {

    override val kodein by closestKodein()
    private val viewModelFactory by instance<RecipeViewModelFactory>()

    private val ingredientList: MutableList<FoodItem> = mutableListOf()
    private val tagsList = ArrayList<String>()
    private val instructionList = ArrayList<String>()
    private var selectedImageURI: Uri? = null

//    private val instructionRecyclerView = RecyclerView(context!!)

    companion object {
        private const val SELECT_PICTURE = 999
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_recipe_scrollview, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        ingredientRecyclerView(view)
        setUpInstruction(view)
        setUpTag(view)
        populateUserCardView(view)
        setUpNavigation()
        setUpCollapsibleViews(view)

    }
    private fun setUpCollapsibleViews(view: View) {

        val nameChildren = ArrayList<View>()
        val ingredientChildren = ArrayList<View>()
        val ingredientListChildren = ArrayList<View>()
        val methodChildren = ArrayList<View>()
        val methodListChildren = ArrayList<View>()
        val tagsChildren = ArrayList<View>()
        val tagsListChildren = ArrayList<View>()
        val ratingChildren = ArrayList<View>()
        val imageChildren = ArrayList < View >()

        val name = view.findViewById<LinearLayout>(R.id.name_bar)
        val nameET = view.findViewById<EditText>(R.id.add_recipe_name)
        nameChildren.add(nameET)
        setUpListeners(name, nameChildren)

        val ingredient = view.findViewById<LinearLayout>(R.id.ingredient_bar)
        val ingredientCard = view.findViewById<View>(R.id.addItem)
        val ingredientButton = view.findViewById<View>(R.id.add_recipe_btn_add)
        ingredientChildren.add(ingredientCard)
        ingredientChildren.add(ingredientButton)
        setUpListeners(ingredient, ingredientChildren)

        val ingredientList = view.findViewById<LinearLayout>(R.id.ingredientList_bar)
        val ingredientListRV = view.findViewById<View>(R.id.add_ingredient_recycler_view)
        ingredientListChildren.add(ingredientListRV)
        setUpListeners(ingredientList, ingredientListChildren)

        val instruction = view.findViewById<LinearLayout>(R.id.instruction_bar)
        val instructionET = view.findViewById<View>(R.id.add_recipe_instruction)
        val instructionButton = view.findViewById<View>(R.id.add_recipe_add_instruction_btn)
        methodChildren.add(instructionET)
        methodChildren.add(instructionButton)
        setUpListeners(instruction, methodChildren)

        val instructionList = view.findViewById<LinearLayout>(R.id.instructionList_bar)
        val instructionListRV = view.findViewById<View>(R.id.add_recipe_instruction_recyclerview)
        methodListChildren.add(instructionListRV)
        setUpListeners(instructionList, methodListChildren)

        val tags = view.findViewById<LinearLayout>(R.id.tags_bar)
        val tagsET = view.findViewById<View>(R.id.add_recipe_tags)
        val tagsBtn = view.findViewById<View>(R.id.add_recipe_add_tags_btn)
        tagsChildren.add(tagsET)
        tagsChildren.add(tagsBtn)
        setUpListeners(tags, tagsChildren)

        val tagsList = view.findViewById<LinearLayout>(R.id.tagsList_bar)
        val tagsRV = view.findViewById<View>(R.id.add_recipe_tags_recyclerview)
        tagsListChildren.add(tagsRV)
        setUpListeners(tagsList, tagsListChildren)

        val rating = view.findViewById<LinearLayout>(R.id.rating_bar)
        val ratingBar = view.findViewById<View>(R.id.add_recipe_ratingbar)
        ratingChildren.add(ratingBar)
        setUpListeners(rating, ratingChildren)

        val image = view.findViewById<LinearLayout>(R.id.image_bar)
        val imageCard = view.findViewById<View>(R.id.card_add)
        val imageText = view.findViewById<View>(R.id.add_recipe_uri)
        imageChildren.add(imageCard)
        imageChildren.add(imageText)
        setUpListeners(image, imageChildren)
    }

    private fun setUpListeners(parent: View, children: ArrayList<View>) {
        parent.setOnClickListener {
            cardViewAnimation(children)
        }
    }

    /*
    Expand/Collapse card view children
     */
    private fun cardViewAnimation(viewList: ArrayList<View>) {
        viewList.forEach { view ->
            if (view.visibility == View.VISIBLE) {
                view.visibility = View.GONE
            } else {
                view.visibility = View.VISIBLE
            }
        }
    }

    private fun setUpTag(view: View) {
        val addButton: Button = view.findViewById(R.id.add_recipe_add_tags_btn)
        val recyclerView = view.findViewById<RecyclerView>(R.id.add_recipe_tags_recyclerview)
        val tagsET: EditText = view.findViewById(R.id.add_recipe_tags)
        tagsList.add("Try adding some tags!")

        recyclerView(view, addButton, tagsList, recyclerView, tagsET)
    }

    private fun setUpInstruction(view: View) {
        val addButton: Button = view.findViewById(R.id.add_recipe_add_instruction_btn)
        val recyclerView = view.findViewById<RecyclerView>(R.id.add_recipe_instruction_recyclerview)
        val instructionET: EditText = view.findViewById(R.id.add_recipe_instruction)
        instructionList.add("Try adding some instructions!")

        recyclerView(view, addButton, instructionList, recyclerView, instructionET)
    }


    private fun recyclerView(
        view: View, addButton: Button,
        passedList: ArrayList<String>,
        recyclerView: RecyclerView, editText: EditText
    ) {

        var itemAdded = false

        recyclerView.layoutManager = LinearLayoutManager(context)
        setUpRecyclerView(passedList, recyclerView)

        addButton.setOnClickListener {

            if (itemAdded == false) {
                passedList.clear()
                itemAdded = true
            }

            val editTextString = editText.text.toString()
            if (editTextString != "") {
                if (addButton.id == R.id.add_recipe_add_tags_btn) {
                    val parsedList = parseList(editTextString)
                    parsedList.forEach { tag ->
                        passedList.add(tag)
                    }
                } else {
                    passedList.add(editTextString)
                }
                recyclerView.adapter!!.notifyDataSetChanged()
            } else {
                Toast.makeText(context, "Please enter some data", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun parseList(str: String): List<String> {
        val splitStringsList = str.split(",")
        val trimmed = ArrayList<String>()

        splitStringsList.forEach {
            if (it.trim() != "") {
                trimmed.add(it.trim())
            }
        }
        return trimmed
    }

    private fun ingredientRecyclerView(view: View) {

        val addIngredient: Button = view.findViewById(R.id.add_recipe_btn_add)
        val addForm: View = view.findViewById(R.id.addItem)

        val defaultText: MutableList<FoodItem> = mutableListOf()

        defaultText.add(
            FoodItem(
                "Type",
                "some ingredients!",
                "Try adding",
                Quantity(
                    "",
                    ""
                )
            )
        )

        val recyclerView = view.findViewById<RecyclerView>(R.id.add_ingredient_recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(context)
        setUpIngredientRecyclerView(defaultText, recyclerView)

        addIngredient.setOnClickListener {
            val brand = addForm.recipe_fooditem_brand.text.toString()
            val food = addForm.recipe_fooditem_brand_food.text.toString()
            val quantityType = addForm.recipe_fooditem_quantity_type.text.toString()
            val quantityAmount = addForm.recipe_fooditem_quantity_amount.text.toString()

            /*
            Check if works without if(brand != null)
             */
            if (food != "" && quantityType != "" && quantityAmount != "") {
                if (brand != "") {
                    val foodItem = FoodItem(
                        "",
                        food,
                        brand,
                        Quantity(
                            quantityType,
                            quantityAmount
                        )
                    )
                    ingredientList.add(foodItem)
                    setUpIngredientRecyclerView(ingredientList, recyclerView)
                } else {
                    val foodItem = FoodItem(
                        "",
                        food,
                        null,
                        Quantity(
                            quantityType,
                            quantityAmount
                        )
                    )

                    Log.d(TAG, "Food Item = $foodItem")
                    ingredientList.add(foodItem)
                    setUpIngredientRecyclerView(ingredientList, recyclerView)
                }
            } else {
                Toast.makeText(context, "Please enter details", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun setUpNavigation() {

        val navController = Navigation.findNavController(activity!!, R.id.nav_host_fragment)
        val submit: Button = view!!.findViewById(R.id.add_recipe_btn_submit)
        val cancel: Button = view!!.findViewById(R.id.add_recipe_btn_cancel)

        submit.setOnClickListener {
            addEntry()
            navController.navigate(R.id.next)
        }

        cancel.setOnClickListener {
            navController.popBackStack()
        }
    }

    private fun getRecipe(): Recipe {

        val addView = view

        val title = addView!!.findViewById<EditText>(R.id.add_recipe_name)
        val rating = addView.findViewById<RatingBar>(R.id.add_recipe_ratingbar)
        var photo: String? = ""

        if (selectedImageURI != null) {
            photo = selectedImageURI.toString()
        } else {
            photo = null
        }

        return Recipe(
            title.text.toString(),
            rating.rating.toDouble(),
            photo,
            tagsList,
            ingredientList
        )
    }

    //add boolean return on successful add
    private fun addEntry() {
        val recipe = getRecipe()
        val viewModel =
            ViewModelProviders.of(this, viewModelFactory).get(RecipeViewModel::class.java)
        viewModel.addRecipe(recipe)
    }

    private fun setUpIngredientRecyclerView(
        ingredientList: List<FoodItem>,
        recyclerView: RecyclerView
    ) {
        recyclerView.adapter =
            AddRecipeRecyclerViewAdapter(
                ingredientList
            )
        recyclerView.adapter!!.notifyDataSetChanged()
    }

    private fun setUpRecyclerView(instructionList: List<String>, recyclerView: RecyclerView) {
        recyclerView.adapter = InstructionRecyclerViewAdapter(instructionList)
        recyclerView.adapter!!.notifyDataSetChanged()
    }

    private fun populateUserCardView(view: View) {
        val cardView = view.findViewById<View>(R.id.card_add)
        val txtView = cardView.findViewById<TextView>(R.id.item_card_title)
        val imgView = cardView.findViewById<ImageView>(R.id.item_card_image)

        imgView.setImageResource(R.drawable.food)
        txtView.text = getString(R.string.item_card_title_default_txt)

        cardView.setOnClickListener {
            launchGallery()
        }
    }

//    private fun populateUploadCardView(view: View) {
//        val uploadCardView = view.findViewById<View>(R.id.card_upload)
//        val uploadTxt = uploadCardView.findViewById<TextView>(R.id.item_card_title)
//        val uploadImg = uploadCardView.findViewById<ImageView>(R.id.item_card_image)
//
//        //Change to use string
//        uploadTxt.text = "Click Above To Select Image"
//        uploadImg.setImageResource(R.drawable.upload)
//        uploadImg.scaleType = ImageView.ScaleType.FIT_CENTER
//
//        uploadImg.setOnClickListener {
//            launchGallery()
//        }
//    }

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

        if (resultCode == RESULT_OK) {
            if (requestCode == SELECT_PICTURE) {
                selectedImageURI = data!!.data!!
                Glide.with(activity!!).load(selectedImageURI).centerCrop().into(imgView)
            }
        }
    }
}