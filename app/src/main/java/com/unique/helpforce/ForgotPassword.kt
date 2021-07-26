package com.unique.helpforce

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.unique.helpforce.retrofit.API
import com.unique.helpforce.retrofit.DefaultResponse
import com.unique.helpforce.retrofit.RetrofitClient
import kotlinx.android.synthetic.main.activity_forgot_password.*
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ForgotPassword : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_password)

        resetbtn.setOnClickListener {
            val phone = reset_phone.text.toString().trim()
            val email = reset_email.text.toString().trim()

            ResetPassword(phone,email)

        }
        Gobackbtn.setOnClickListener {
            val intent = Intent(this@ForgotPassword, MainActivity::class.java)
            startActivity(intent)
        }


    }
    private fun ResetPassword(phone : String, email : String){

        val retIn = RetrofitClient.getRetrofitInstance().create(API::class.java)

        retIn.ResetPassword(phone,email).enqueue(object : Callback<DefaultResponse> {
            override fun onFailure(call: Call<DefaultResponse>, t: Throwable) {
                Toast.makeText(this@ForgotPassword,t.message, Toast.LENGTH_SHORT).show()
            }

            override fun onResponse(call: Call<DefaultResponse>, response: Response<DefaultResponse>) {
                if (response.code() == 200 ){
                    Toast.makeText(this@ForgotPassword,"Link Send to Email!", Toast.LENGTH_SHORT).show()
                }
                else{
                    Toast.makeText(this@ForgotPassword,"Account does not exist ${response.code()}", Toast.LENGTH_SHORT).show()
                }
            }
        })

    }
}