package doubletapp.tracker.compose

import android.app.Application

class TrackerApp: Application() {

    override fun onCreate() {
        super.onCreate()
        PlatformSdk.initialize()
    }
}
