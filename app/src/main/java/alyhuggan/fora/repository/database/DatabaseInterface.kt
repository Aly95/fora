package alyhuggan.fora.repository.database

import alyhuggan.fora.repository.objects.recipe.RecipeDaoInterface

interface DatabaseInterface {
    val foraDao: RecipeDaoInterface
}