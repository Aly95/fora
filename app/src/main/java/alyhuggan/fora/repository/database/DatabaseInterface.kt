package alyhuggan.fora.repository.database

import alyhuggan.fora.repository.objects.foods.FoodDaoInterface
import alyhuggan.fora.repository.objects.recipe.RecipeDaoInterface
import alyhuggan.fora.repository.objects.user.UserDaoInterface

interface DatabaseInterface {
    val recipeDao: RecipeDaoInterface
    val foodDao: FoodDaoInterface
    val userDao: UserDaoInterface
}