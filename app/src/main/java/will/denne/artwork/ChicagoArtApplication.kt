package will.denne.artwork

import android.app.Application
import org.koin.core.context.startKoin
import timber.log.Timber
import will.denne.artwork.koin.KoinModule

class ChicagoArtApplication: Application() {
    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
        startKoin {
            modules(KoinModule.appModule)
        }
    }
}
