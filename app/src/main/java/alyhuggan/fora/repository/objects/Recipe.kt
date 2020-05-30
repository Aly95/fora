package alyhuggan.fora.repository.objects

import android.media.Image

data class Recipe(val title: String, val rating: Double?, val photo: Image?,
                  val type: String, val foods: ArrayList<FoodItem>){
}