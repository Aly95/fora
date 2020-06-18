package alyhuggan.fora.repository.database

import alyhuggan.fora.repository.objects.foods.FoodDaoImplementation
import alyhuggan.fora.repository.objects.foods.FoodDaoInterface
import alyhuggan.fora.repository.objects.recipe.RecipeDaoImplementation
import alyhuggan.fora.repository.objects.recipe.RecipeDaoInterface
import alyhuggan.fora.repository.objects.user.UserDaoImplementation
import alyhuggan.fora.repository.objects.user.UserDaoInterface

class DatabaseImplementation: DatabaseInterface {
    override val recipeDao: RecipeDaoInterface = RecipeDaoImplementation()
    override val foodDao: FoodDaoInterface = FoodDaoImplementation()
    override val userDao: UserDaoInterface = UserDaoImplementation()
}