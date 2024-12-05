package uz.excellentshoes.businesscalculation.app

import android.app.Application
import android.util.Log
import com.google.firebase.database.FirebaseDatabase
import uz.excellentshoes.businesscalculation.data.AppPreferences
import uz.excellentshoes.businesscalculation.utils.NetworkConnectivityObserver

class App : Application() {
    companion object{
        lateinit var context: App
            private set
        lateinit var database: FirebaseDatabase
            private set
    }
    lateinit var networkConnectivityObserver: NetworkConnectivityObserver

    override fun onCreate() {
        super.onCreate()
        context = this
        AppPreferences.init(this)
        database = FirebaseDatabase.getInstance("https://business-calculation-b4852-default-rtdb.firebaseio.com/")
        database.setPersistenceEnabled(true)
        networkConnectivityObserver = NetworkConnectivityObserver(this)
    }
}