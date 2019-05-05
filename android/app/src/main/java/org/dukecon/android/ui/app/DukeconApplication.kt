package org.dukecon.android.ui.app

import android.app.Application
import com.jakewharton.threetenabp.AndroidThreeTen
import org.dukecon.android.ui.injection.ApplicationComponent
import org.dukecon.android.ui.injection.DaggerApplicationComponent

class DukeconApplication : Application() {

    private lateinit var component: ApplicationComponent

    override fun onCreate() {
        super.onCreate()
        AndroidThreeTen.init(this);
        component = DaggerApplicationComponent
                .builder()
                .application(this)
                .build()

        component.inject(this)
    }

    override fun getSystemService(name: String?): Any {
        return when (name) {
            "component" -> component
            else -> super.getSystemService(name ?: "")
        }
    }
}
