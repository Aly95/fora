package alyhuggan.fora.repository.database

import alyhuggan.fora.repository.database.logic.ForaDaoInterface

interface DatabaseInterface {
    val foraDao: ForaDaoInterface
}