package com.jdannhausenbrun.demoproject

import android.app.Application
import com.jdannhausenbrun.demoproject.database.DemoDatabase
import com.jdannhausenbrun.demoproject.database.daos.CountryDao
import com.jdannhausenbrun.demoproject.network.CountriesRestServer
import com.jdannhausenbrun.demoproject.repository.CountriesRepository
import toothpick.ktp.KTP
import toothpick.ktp.binding.bind
import toothpick.ktp.binding.module

class DemoApplication: Application() {
    override fun onCreate() {
        super.onCreate()

        KTP.openRootScope().installModules(
            module {
                bind(CountryDao::class).toInstance(DemoDatabase.getDb(applicationContext).countryDao())
                bind(CountriesRestServer::class).toInstance(CountriesRestServer.Builder.getServer())
            }
        )

        // Needs to occur in a second installation because CountriesRepository depends on the dao and server
        KTP.openRootScope().installModules(
            module {
                bind(CountriesRepository::class).toInstance(CountriesRepository())
            }
        )
    }
}