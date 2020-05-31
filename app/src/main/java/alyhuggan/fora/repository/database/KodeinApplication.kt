package alyhuggan.covid_19.repository

import alyhuggan.fora.repository.database.DatabaseImplementation
import alyhuggan.fora.repository.database.DatabaseInterface
import alyhuggan.fora.repository.database.logic.ForaDaoInterface
import alyhuggan.fora.viewmodels.ViewModelFactory
import android.app.Application
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.provider
import org.kodein.di.generic.singleton

class KodeinApplication : Application(), KodeinAware {

    /*
    Provides Kodein with the bindings in order to allow dependency injection
    */
    override val kodein = Kodein.lazy {
        bind<DatabaseInterface>() with singleton { DatabaseImplementation() }
        bind<ForaDaoInterface>() with singleton { instance<DatabaseInterface>().foraDao }
        bind() from provider {
            ViewModelFactory(instance())
        }
    }
}