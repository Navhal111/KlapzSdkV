package com.klapz.sdk

import android.content.Context
import android.content.SharedPreferences
import android.util.AttributeSet
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.*
import com.android.volley.*
import com.android.volley.toolbox.HttpHeaderParser
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.klapz.sdk.api.Urls
import com.lamudi.phonefield.PhoneInputLayout
import org.json.JSONException
import org.json.JSONObject
import java.io.UnsupportedEncodingException
import java.nio.charset.Charset
import java.util.HashMap


class KlapzButton @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null) :
    LinearLayout(context, attrs) {
    lateinit var Phoneedit: PhoneInputLayout;

    lateinit var klapznext :TextView
    lateinit  var klapzlogin :TextView
    lateinit var klapzmain:TextView
    lateinit var user_detail_layout:LinearLayout
    lateinit var otpfinal  :LinearLayout
    lateinit var login  :LinearLayout
    lateinit var thncyou  :LinearLayout
    lateinit var plain_text_input : EditText
    lateinit var klapzimnage: ImageView
    var token = ""

    init {

        orientation = HORIZONTAL
        gravity = Gravity.CENTER_VERTICAL
        val inflater = context
            .getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        inflater.inflate(R.layout.activity_main, this, true)

        val bottomSheetDialog = BottomSheetDialog(context)
        bottomSheetDialog.setContentView(R.layout.bottomsheet)
        val pref = context.getSharedPreferences("MyPref", 0)

         klapzimnage =  findViewById(R.id.klapz)
         klapznext = bottomSheetDialog.findViewById<TextView>(R.id.klapznext)!!
         klapzlogin = bottomSheetDialog.findViewById<TextView>(R.id.klapzlogin)!!
         klapzmain = bottomSheetDialog.findViewById<TextView>(R.id.klapz)!!
         user_detail_layout = bottomSheetDialog.findViewById<LinearLayout>(R.id.user_detail_layout)!!
         otpfinal = bottomSheetDialog.findViewById<LinearLayout>(R.id.otpfinal)!!
         login = bottomSheetDialog.findViewById<LinearLayout>(R.id.login)!!
         thncyou = bottomSheetDialog.findViewById<LinearLayout>(R.id.thncyou)!!
         Phoneedit = bottomSheetDialog.findViewById<PhoneInputLayout>(R.id.edit_text)!!
         plain_text_input = bottomSheetDialog.findViewById<EditText>(R.id.plain_text_input)!!
//        Phoneedit.ont

        Phoneedit!!.setHint(R.string.phone)
        Phoneedit!!.setDefaultCountry("IN")



        if (klapznext != null) {
            klapznext.setOnClickListener {
                Login()

            }
        }

        if (klapzlogin != null) {
            klapzlogin!!.setOnClickListener {
                OTP()
//                otpfinal!!.visibility = View.GONE
//                user_detail_layout!!.visibility = View.VISIBLE
            }
        }

        klapzmain!!.setOnClickListener {
            user_detail_layout!!.visibility = View.GONE
            thncyou!!.visibility = View.VISIBLE
        }

        klapzimnage.setOnClickListener {
            bottomSheetDialog.show()
        }

        if(pref.getString("token","test")!="test"){
            otpfinal!!.visibility = View.GONE
            login!!.visibility = View.GONE
            user_detail_layout!!.visibility = View.VISIBLE
        }        
    }

    private fun showBottomSheetDialog(title: String, Klapz: Int, Key: String) {
        val bottomSheetDialog = BottomSheetDialog(context)
        bottomSheetDialog.setContentView(R.layout.bottomsheet)
        bottomSheetDialog.show()
    }
    fun Login(){
        Log.e("numbert", Phoneedit.phoneNumber)
        if(Phoneedit.phoneNumber.isNullOrBlank()){
            Toast.makeText(context, "Enter Valid number", Toast.LENGTH_LONG)
                .show()
            return
        }

        var stringphone = Phoneedit.phoneNumber
        val obj = JSONObject()
        val objinner = JSONObject()
        objinner.put("mobile", stringphone)
        obj.put("user", objinner)

        val jsObjRequest =
            object : JsonObjectRequest(
                Request.Method.POST,
                Urls.apiurl + "auth/request_mobile_otp?apiFrom=" + Urls.apiFrom + "&buildNumber=" + Urls.buildNumber,
                obj,
                Response.Listener<JSONObject?> { response ->
                    Log.e("responce", response.toString())
                    if (response != null) {
                        login!!.visibility = View.GONE
                        otpfinal!!.visibility = View.VISIBLE
                    }

                },
                Response.ErrorListener { error ->
                    Toast.makeText(context, "Error in request", Toast.LENGTH_LONG)
                        .show()
                    Log.e("error", error.toString())
                }
            ) {

                override fun getHeaders(): Map<String, String> {
                    val headers = HashMap<String, String>()
                    return headers
                }
            }
        val requestQueue: RequestQueue = Volley.newRequestQueue(context)
        requestQueue.add(jsObjRequest)
    }

    fun OTP(){
        if(plain_text_input.text.length !=4 ){
            Toast.makeText(context, "Enter Valid OTP", Toast.LENGTH_LONG)
                .show()
            return
        }

        val obj = JSONObject()
        val objinner = JSONObject()
        var stringphone = Phoneedit.phoneNumber
        objinner.put("mobile", stringphone)
        objinner.put("otp", plain_text_input.text)

        obj.put("user", objinner)

        Log.e("numbert", obj.toString())
        Log.e(
            "numbert",    Urls.apiurl + "auth/verify_mobile_otp.json?apiFrom=" + Urls.apiFrom + "&buildNumber=" + Urls.buildNumber
        )
        val jsObjRequest =
            object : JsonObjectRequest(
                Request.Method.POST,
                Urls.apiurl + "auth/verify_mobile_otp.json?apiFrom=" + Urls.apiFrom + "&buildNumber=" + Urls.buildNumber,
                obj,
                Response.Listener<JSONObject?> { response ->
                    Log.e("responce", response.toString())
                    if (response != null) {
                        val pref = context.getSharedPreferences("MyPref", 0)
                        val editor: SharedPreferences.Editor = pref.edit()
                        editor.putString("Klapztoken", token);
                        editor.apply()
                        otpfinal!!.visibility = View.GONE
                        user_detail_layout!!.visibility = View.VISIBLE
                    }

                },
                Response.ErrorListener { error ->
                    Toast.makeText(context, "Error in request", Toast.LENGTH_LONG)
                        .show()
                    Log.e("error", error.toString())
                }
            ) {
                override fun getHeaders(): Map<String, String> {
                    val headers = HashMap<String, String>()
                    headers.put("Content-Type", "application/json")
                    return headers
                }
                override fun parseNetworkResponse(response: NetworkResponse?): Response<JSONObject>? {

                    try{
                        token = response?.headers?.get("auth-token").toString()
                        Log.e("token",token)
                        val jsonString = String(
                            response?.data ?: ByteArray(0),
                            Charset.forName(HttpHeaderParser.parseCharset(response?.headers)))
                        return Response.success(
                            JSONObject(jsonString),
                            HttpHeaderParser.parseCacheHeaders(response)
                        )
                    } catch (e: UnsupportedEncodingException) {
                        return Response.error( ParseError(e));
                    } catch (je: JSONException) {
                        return Response.error(ParseError(je));
                    }
                }
            }
        val requestQueue: RequestQueue = Volley.newRequestQueue(context)
        requestQueue.add(jsObjRequest)
    }
}