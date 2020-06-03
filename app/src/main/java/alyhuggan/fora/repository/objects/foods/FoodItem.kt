package alyhuggan.fora.repository.objects.foods

import alyhuggan.fora.repository.objects.Quantity

data class FoodItem(val type: String = "", val name: String = "", val quantity: Quantity? = null) {
}