package uz.excellentshoes.businesscalculation.data

import android.content.Context
import android.content.SharedPreferences

class AppPreferences private constructor(){
    companion object{
        private lateinit var pref: SharedPreferences
        private lateinit var instance: AppPreferences

        fun init(context: Context){
            pref = context.getSharedPreferences("MY_PREF", Context.MODE_PRIVATE)
            instance = AppPreferences()
        }

        fun getInstance(): AppPreferences = instance
    }


    var phoneNumber: String
        set(value) = pref.edit().putString("PHONE_NUMBER",value).apply()
        get() = pref.getString("PHONE_NUMBER","") ?:""

    var fullName: String
        set(value) = pref.edit().putString("FULL_NAME",value).apply()
        get() = pref.getString("FULL_NAME","") ?:""

    var currencyDollar: Int
        set(value) = pref.edit().putInt("CURRENCY_DOLLAR",value).apply()
        get() = pref.getInt("CURRENCY_DOLLAR",12700)
}