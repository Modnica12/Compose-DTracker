package doubletapp.iS.compose

import android.app.Application

class TrackerApp: Application() {

    override fun onCreate() {
        super.onCreate()
        PlatformSdk.initialize()
    }
}
