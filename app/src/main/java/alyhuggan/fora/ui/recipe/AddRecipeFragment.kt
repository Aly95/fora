package alyhuggan.fora.ui.recipe

import alyhuggan.fora.R
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.squareup.picasso.Picasso


class AddRecipeFragment : Fragment() {

    private lateinit var imageView: ImageView
    private lateinit var imageButton: Button
    private lateinit var sendButton: Button
    private var imageData: ByteArray? = null

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
        return inflater.inflate(R.layout.fragment_add_recipe, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val uploadCardView = view.findViewById<View>(R.id.card_upload)
        val uploadTxt = uploadCardView.findViewById<TextView>(R.id.item_card_title)
        val uploadImg = uploadCardView.findViewById<ImageView>(R.id.item_card_image)

        val cardView = view.findViewById<View>(R.id.card_add)
        val txtView = cardView.findViewById<TextView>(R.id.item_card_title)
        val imgView = cardView.findViewById<ImageView>(R.id.item_card_image)

        uploadTxt.text = "Click Above To Select Image"
        uploadImg.setImageResource(R.drawable.upload)
        uploadImg.scaleType = ImageView.ScaleType.FIT_CENTER

        imgView.setImageResource(R.drawable.food)
        txtView.text = "Image Preview"

        uploadImg.setOnClickListener {
            launchGallery()
        }
    }

    private fun launchGallery() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, SELECT_PICTURE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        val cardView = view!!.findViewById<View>(R.id.card_add)
        val imgView = cardView.findViewById<ImageView>(R.id.item_card_image)

        if (resultCode == RESULT_OK) {
            if (requestCode == SELECT_PICTURE) {
                val selectedImageURI = data!!.data!!
                Picasso.get().load(selectedImageURI).noPlaceholder().centerCrop()
                    .fit()
                    .into(imgView)
            }
        }
    }
}