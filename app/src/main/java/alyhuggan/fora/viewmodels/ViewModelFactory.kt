package alyhuggan.fora.viewmodels

import alyhuggan.fora.repository.database.logic.ForaDaoInterface
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class ViewModelFactory(private val foraDaoInterface: ForaDaoInterface)
    : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return ViewModel(foraDaoInterface) as T
    }
}