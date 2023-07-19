package doubletapp.iS.compose

import PlatformSdk
import android.app.Application
import configuration.PlatformConfiguration

class TrackerApp: Application() {

    override fun onCreate() {
        super.onCreate()
        val platformConfiguration = PlatformConfiguration(context = applicationContext)
        PlatformSdk.initialize(platformConfiguration)
    }
}
