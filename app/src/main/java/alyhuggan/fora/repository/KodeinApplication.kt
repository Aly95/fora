package alyhuggan.fora.repository

import alyhuggan.fora.repository.database.DatabaseImplementation
import alyhuggan.fora.repository.database.DatabaseInterface
import alyhuggan.fora.repository.objects.foods.FoodDaoInterface
import alyhuggan.fora.repository.objects.recipe.RecipeDaoInterface
import alyhuggan.fora.repository.objects.user.UserDaoInterface
import alyhuggan.fora.viewmodels.food.FoodViewModelFactory
import alyhuggan.fora.viewmodels.recipe.RecipeViewModelFactory
import alyhuggan.fora.viewmodels.user.UserViewModelFactory
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
        bind<RecipeDaoInterface>() with singleton { instance<DatabaseInterface>().recipeDao }
        bind() from provider { RecipeViewModelFactory(instance()) }

        //Binding Foods
        bind<FoodDaoInterface>() with singleton { instance<DatabaseInterface>().foodDao }
        bind() from provider { FoodViewModelFactory(instance()) }

        //Binding Users
        bind<UserDaoInterface>() with singleton { instance<DatabaseInterface>().userDao }
        bind() from provider { UserViewModelFactory(instance()) }
    }
}