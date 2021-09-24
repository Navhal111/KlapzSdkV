package com.klapz.sdk

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.klapz.sdk.api.KlapzConfig
import org.json.JSONObject

class TestKlapz : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test_klapz)

        var  kalpz = KlapzConfig();

        val obj = JSONObject()
        obj.put("title", "Klapz this content")
        obj.put("klapz", 10)
        obj.put("createrID", "createrID")
        obj.put("Url", "")


        kalpz.Start("XXXXXXXX",this)
        kalpz.Config(obj,this)


        // If user Logout use this funtion
        // kalpz.Close(this)

    }
}

