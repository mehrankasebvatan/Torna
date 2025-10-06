package ir.kasebvatan.torna.data

import android.app.Application

class Application: Application() {
    override fun onCreate() {
        super.onCreate()
        Cache.init(this@Application)
    }
}