package alyhuggan.fora.viewmodels

import alyhuggan.fora.repository.database.logic.ForaDaoInterface
import androidx.lifecycle.ViewModel

class ViewModel(private val foraDaoInterface: ForaDaoInterface) : ViewModel() {
}
