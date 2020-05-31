package alyhuggan.fora.repository.database

import alyhuggan.fora.repository.database.logic.ForaDaoImplementation
import alyhuggan.fora.repository.database.logic.ForaDaoInterface

class DatabaseImplementation: DatabaseInterface {

    override val foraDao: ForaDaoInterface =
        ForaDaoImplementation()
}