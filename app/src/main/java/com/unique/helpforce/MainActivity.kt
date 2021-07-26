
package com.unique.helpforce

import android.R.id.edit
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
import com.unique.helpforce.retrofit.API
import com.unique.helpforce.retrofit.DefaultResponse
import com.unique.helpforce.retrofit.DeviceRegistrationToken
import com.unique.helpforce.retrofit.RetrofitClient
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_register.*
import org.json.JSONException
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.regex.Pattern


class MainActivity : AppCompatActivity() {

    private fun getToken() :String{
        val prefs = getSharedPreferences("helpforce", AppCompatActivity.MODE_PRIVATE)
        return prefs.getString("token","")!!
    }

    private fun getuserid() :String{
        val prefs = getSharedPreferences("helpforce", AppCompatActivity.MODE_PRIVATE)
        return prefs.getString("userid","")!!
    }

    internal var compositeDisposable = CompositeDisposable()

    override fun onStop() {
        compositeDisposable.clear()
        super.onStop()
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

main_btn_login.setOnClickListener {



/*
    val intent = Intent(this@MainActivity, MainScreenActivity::class.java)
    startActivity(intent)
*/


                val phone = main_phone.text.toString().trim()
                val password = main_password.text.toString().trim()



                LoginUser(phone, password)






        }

        main_btn_register.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }

        texttoForgotPass.setOnClickListener {
            val intent = Intent(this, ForgotPassword::class.java)
            startActivity(intent)
        }

        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.w("", "Fetching FCM registration token failed", task.exception)
                return@OnCompleteListener
            }

            // Get new FCM registration token
            val token = task.result
//            DeviceRegistrationToken("Bearer ${getToken()}",token)

            // Log and toast
            val msg = getString(R.string.msg_token_fmt, token)
            Log.d("Haris", msg)
 //           Toast.makeText(baseContext, msg, Toast.LENGTH_SHORT).show()
        })


    }

    private fun DeviceRegistrationToken(auth : String,devtoken : String)
    {
        val retIn = RetrofitClient.getRetrofitInstance().create(API::class.java)

        retIn.deviceRegistrationToken(auth, devtoken).enqueue(object : Callback<DefaultResponse> {
            override fun onFailure(call: Call<DefaultResponse>, t: Throwable) {
                Toast.makeText(this@MainActivity, t.message, Toast.LENGTH_SHORT).show()
            }

            override fun onResponse(
                call: Call<DefaultResponse>,
                response: Response<DefaultResponse>
            ) {
                if (response.code() == 200) {
                    Toast.makeText(this@MainActivity, "Success!", Toast.LENGTH_SHORT).show()
                }
                else {
                    Toast.makeText(
                        this@MainActivity,
                        "Login Failed ${response.code()}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        })

    }


    private fun LoginUser(phone: String, password: String)
    {
        val retIn = RetrofitClient.getRetrofitInstance().create(API::class.java)

        retIn.loginUser(phone, password).enqueue(object : Callback<DefaultResponse> {
            override fun onFailure(call: Call<DefaultResponse>, t: Throwable) {
                Toast.makeText(this@MainActivity, t.message, Toast.LENGTH_SHORT).show()
            }

            override fun onResponse(
                call: Call<DefaultResponse>,
                response: Response<DefaultResponse>
            ) {
                if (response.code() == 200) {
                    Log.d("haris", response.body()!!.toString())
                    Toast.makeText(this@MainActivity, "Login Success!", Toast.LENGTH_SHORT).show()
                    val sharedPrefFile = "helpforce"
                    val sharedPreferences: SharedPreferences =
                        this@MainActivity.getSharedPreferences(
                            sharedPrefFile,
                            Context.MODE_PRIVATE
                        )
                    val editor: SharedPreferences.Editor = sharedPreferences.edit()

                        val saveToken: String = response.body()!!.token
                    val userid: String = response.body()!!.user._id

                    editor.putString("token", saveToken)
                    editor.putString("userid", userid)
                        Log.i("Login", saveToken)
                    Log.i("Login", userid)
                    editor.apply()


                    val intent = Intent(this@MainActivity, MainScreenActivity::class.java)
                    startActivity(intent)


                } else {
                    Toast.makeText(
                        this@MainActivity,
                        "Login Failed ${response.code()}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        })

    }



    public fun InputValidation(): Boolean{
        val edit_phone = findViewById<EditText>(R.id.main_phone);
        val edit_password = findViewById<EditText>(R.id.main_password);

        return !( !ValidatePhoneNumber(edit_phone) or !ValidatePassword(edit_password))
    }

    private  fun ValidatePhoneNumber(edit_phone: EditText):Boolean
    {
        val p = Pattern.compile(
            "^((\\+92)|(0092))-{0,1}\\d{3}-{0,1}\\d{7}$|^\\d{11}$|^\\d{4}-\\d{7}$"
        )
        val PhoneInput : String = edit_phone.text!!.trim().toString()
        if(PhoneInput.isEmpty()){
            edit_phone.error = "Field can't be Empty"
            return false
        }
        else if (!p.matcher(PhoneInput).matches())
        {
            edit_phone.error ="Not Valid Phone Number"
            return false
        }
        else
        {
            edit_phone.error = null
            return true
        }

    }
    private  fun ValidatePassword(edit_password: EditText):Boolean
    {
        val p = Pattern.compile(
            "^" +
                    "(?=.*[0-9])" +         //at least 1 digit
                    "(?=.*[a-z])" +         //at least 1 lower case letter
                    "(?=.*[A-Z])" +         //at least 1 upper case letter
                    //        "(?=.*[a-zA-Z])" +      //any letter
                    "(?=.*[@#$%^&+=])" +    //at least 1 special character
                    "(?=\\S+$)" +           //no white spaces
                    ".{4,}" +               //at least 4 characters
                    "$"
        )

        val PasswordInput : String = edit_password.text!!.trim().toString()
        if(PasswordInput.isEmpty()){
            edit_password.error = "Field can't be Empty"
            return false
        }
        else if (!p.matcher(PasswordInput).matches())
        {
            edit_password.error ="Password Too Weak"
            return false
        }
        else
        {
            edit_password.error = null
            return true
        }
    }

/*
    private fun loginUser(phone: String, password: String) {
        compositeDisposable.add(iMyService.loginUser(phone,password)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe{result ->
                Toast.makeText(this@MainActivity,""+result,Toast.LENGTH_SHORT).show()
            }
        )
    }
*/

}