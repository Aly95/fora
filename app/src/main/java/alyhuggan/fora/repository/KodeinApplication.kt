package alyhuggan.fora.repository

import alyhuggan.fora.repository.database.DatabaseImplementation
import alyhuggan.fora.repository.database.DatabaseInterface
import alyhuggan.fora.repository.objects.recipe.RecipeDaoInterface
import alyhuggan.fora.viewmodels.ViewModelFactory
import alyhuggan.fora.viewmodels.recipe.RecipeViewModelFactory
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
        //Binding DB
        bind<DatabaseInterface>() with singleton { DatabaseImplementation() }

        //Binding Recipe
        bind<RecipeDaoInterface>() with singleton { instance<DatabaseInterface>().foraDao }
        bind() from provider {
            RecipeViewModelFactory(instance())
        }
    }
}