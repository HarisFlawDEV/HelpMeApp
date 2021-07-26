package com.unique.helpforce
import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.widget.*
import com.unique.helpforce.retrofit.API
import com.unique.helpforce.retrofit.DefaultResponse
import com.unique.helpforce.retrofit.RetrofitClient
import com.unique.helpforce.retrofit.UserBody
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.activity_register.*
import okhttp3.ResponseBody
import org.w3c.dom.Text
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*
import java.util.regex.Pattern

class RegisterActivity : AppCompatActivity() {

    @SuppressLint("WeekBasedYear")
    private var formatDate = SimpleDateFormat("YYYY-MM-dd",Locale.US)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        pickDate()
        register_btn_login.setOnClickListener {
            val intent = Intent(this,MainActivity::class.java)
            startActivity(intent)
        }
        register_btn_register.setOnClickListener {
            if(!inputValidation())
                Toast.makeText(this, "Invalid Details", Toast.LENGTH_SHORT).show()
            else
            {
                val phone : EditText = findViewById(R.id.register_phone)
                val password : EditText = findViewById(R.id.register_password)
                val email : EditText = findViewById(R.id.register_email)
                val cpassword : EditText = findViewById(R.id.register_confirm_Password)
                val fullName : EditText = findViewById(R.id.register_fname)
                val dob : TextView = findViewById(R.id.register_dob)
                val partName: List<String> = fullName.text.toString().trim().split(" ")

                Log.d("register", phone.text.toString().trim()+" "+password.text.toString().trim()+" "+cpassword.text.toString().trim()+" "+email.text.toString().trim()+" "+dob.text.toString().trim()+" "+CheckGender()+" "+partName[0]+ " "+partName[1] )
                signup(
                    phone.text.toString().trim() ,
                    password.text.toString().trim(),
                    cpassword.text.toString().trim(),
                    partName[0],
                    partName[1],
                    email.text.toString().trim(),
                    dob.text.toString().trim(),
                    CheckGender()
                )
            }
        }
    }

    private fun inputValidation(): Boolean{
        val phone : EditText = findViewById(R.id.register_phone)
        val password : EditText = findViewById(R.id.register_password)
        val email : EditText = findViewById(R.id.register_email)
        val cpassword : EditText = findViewById(R.id.register_confirm_Password)
        val fullName : EditText = findViewById(R.id.register_fname)
        val gender : RadioGroup = findViewById(R.id.radiogender)

        val passwordRegex = Pattern.compile(
            "^" +
                    "(?=.*[0-9])" +         //at least 1 digit
                    "(?=.*[a-z])" +         //at least 1 lower case letter
                    "(?=.*[A-Z])" +         //at least 1 upper case letter
                    "(?=\\S+$)" +           //no white spaces
                    ".{8,}" +               //at least 8 characters
                    "$")

        val phoneRegex = Pattern.compile(
            "^"+
                "(0)"+
                "((3[0-6][0-9]))"+
                "(\\d{7})"+
                "$")

        return if(phone.text.isEmpty() || password.text.isEmpty() || email.text.isEmpty() || cpassword.text.isEmpty() || fullName.text.isEmpty() ) false

        else if (!phoneRegex.matcher(phone.text).matches()) {
            phone.error ="Phone Number Not Valid"
            return false
        }
        else if (!passwordRegex.matcher(password.text).matches()) {
            tvpassword.error = "Password Too Weak"
            return false
        }
        else if (!fullName.text.contains(" ")) {
            fullName.error = "Enter Full Name"
            return false
        }
        else if(!Patterns.EMAIL_ADDRESS.matcher(email.text).matches()){
            email.error = "Not Valid Email Address"
            return false
        }
        else if (!(cpassword.text.toString().trim().equals(password.text.toString().trim()))){
            tvconfirmPassword.error = "Password not Matched"
            return false
        }
        else if (!ValidateGender(gender)){
            return false
        }
        else true
    }
    private fun signup(phone: String,
                       password: String,
                       confirmPassword: String,
                       firstName: String,
                       lastName: String,
                       email : String,
                       dob : String,
                       gender : String){

        val retIn = RetrofitClient.getRetrofitInstance().create(API::class.java)

        retIn.registerUser(phone,password,confirmPassword,firstName,lastName,email,gender,dob).enqueue(object : Callback<DefaultResponse> {
            override fun onFailure(call: Call<DefaultResponse>, t: Throwable) {
                Toast.makeText(this@RegisterActivity, t.message, Toast.LENGTH_SHORT).show()
            }

            override fun onResponse(
                call: Call<DefaultResponse>,
                response: Response<DefaultResponse>
            ) {
                if (response.code() == 200) {
                    Log.d("haris", response.body()!!.toString())
                    Toast.makeText(this@RegisterActivity, "Account Created!", Toast.LENGTH_SHORT).show()

                    val sharedPrefFile = "helpforce"
                    val sharedPreferences: SharedPreferences =
                        this@RegisterActivity.getSharedPreferences(
                            sharedPrefFile,
                            Context.MODE_PRIVATE
                        )
                    val editor: SharedPreferences.Editor = sharedPreferences.edit()

                    val saveToken: String = response.body()!!.token
                    val userid: String = response.body()!!.user._id

                    editor.putString("token", saveToken)
                    editor.putString("userid", userid)
                    editor.apply()
                }
                else if (response.code() == 400) {
                    Toast.makeText(this@RegisterActivity, "User already exists", Toast.LENGTH_SHORT).show()
                }
                else {
                    Toast.makeText(
                        this@RegisterActivity,
                        "Login Failed ${response.code()}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        })
    }

    private fun CheckGender() : String
    {
        val rb_male = findViewById<RadioButton>(R.id.register_radio_male)
        val rb_female = findViewById<RadioButton>(R.id.register_radio_female)
        if (rb_male.isChecked) return "male"
        else if (rb_female.isChecked)
            return "female"
        else
            return ""

    }
    private fun ValidateGender(rg_gender:RadioGroup) : Boolean
    {
        return rg_gender.checkedRadioButtonId != -1
    }
    private fun pickDate() {
        register_dob.setOnClickListener {
            val cal = Calendar.getInstance()
            val datePicker = DatePickerDialog(this,android.R.style.Theme_Holo_Light_Dialog_MinWidth,DatePickerDialog.OnDateSetListener { datePicker, i, i2, i3 ->
            val selectDate = Calendar.getInstance()
                selectDate.set(Calendar.YEAR,i)
                selectDate.set(Calendar.MONTH,i2)
                selectDate.set(Calendar.DAY_OF_MONTH,i3)
                val date = formatDate.format(selectDate.time)
                Toast.makeText(this, "Date: $date",Toast.LENGTH_SHORT).show()
                register_dob.text = date
            },cal.get(Calendar.YEAR),cal.get(Calendar.MONTH),cal.get(Calendar.DAY_OF_MONTH))
            datePicker.show()
        }
    }

}