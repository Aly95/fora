package alyhuggan.fora.repository.database

import alyhuggan.fora.repository.objects.recipe.ForaDaoImplementation
import alyhuggan.fora.repository.objects.recipe.RecipeDaoInterface

class DatabaseImplementation:
    DatabaseInterface {

    override val foraDao: RecipeDaoInterface =
        ForaDaoImplementation()
}