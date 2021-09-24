package com.klapz.mylibrary.api
import android.content.Context
import android.content.SharedPreferences
import org.json.JSONObject

class KlapzConfig {
    
    fun Start(key:String,context: Context){
        val pref = context.getSharedPreferences("MyPref", 0)
        val editor: SharedPreferences.Editor = pref.edit()
        editor.putString("Klapzkey", key);
        editor.apply()
    }

    fun Config(config:JSONObject,context: Context){
        val pref = context.getSharedPreferences("MyPref", 0)
        val editor: SharedPreferences.Editor = pref.edit()
        editor.putString("KlapConfig", config.toString());
        editor.apply()
    }


    fun Close(context: Context){
        val pref = context.getSharedPreferences("MyPref", 0)
        val editor: SharedPreferences.Editor = pref.edit()
        editor.remove("Klapzkey")
        editor.remove("KlapConfig")
        editor.remove("Klapztoken")
        editor.apply()
    }


}