package doubletapp.iS.compose

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import setupThemedNavigation

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupThemedNavigation()
    }
}
