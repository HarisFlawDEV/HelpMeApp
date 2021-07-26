package com.unique.helpforce

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContentProviderCompat.requireContext
import com.squareup.picasso.Picasso
import com.unique.helpforce.retrofit.API
import com.unique.helpforce.retrofit.Friends
import com.unique.helpforce.retrofit.RetrofitClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class PersonalFNFAdapter(private var Ctx: Context, private var resource: Int, private var Item: ArrayList<Friends>)
    : ArrayAdapter<Friends>(Ctx,resource,Item) {


    private fun getToken() :String{
        val prefs = this.context.getSharedPreferences("helpforce", AppCompatActivity.MODE_PRIVATE)
        return prefs.getString("token","")!!
    }

    private fun getuserid() :String{
        val prefs = this.context.getSharedPreferences("helpforce", AppCompatActivity.MODE_PRIVATE)
        return prefs.getString("userid","")!!
    }

    fun deleteMethodforfnf(phone : String)
    {
        val rtn =  RetrofitClient.getRetrofitInstance().create(API::class.java)
        CoroutineScope(Dispatchers.IO).launch {

            // Do the DELETE request and get response


            val response = rtn.deletefnf("Bearer ${getToken()}",phone)
            withContext(Dispatchers.Main) {
                if (response.code() == 200) {
                    Toast.makeText(context, "YAYYY! ", Toast.LENGTH_SHORT).show()

                } else {
                    Log.e("RETROFIT_ERROR", response.code().toString())
                }
            }
        }


    }


    @SuppressLint("ViewHolder", "SetTextI18n")
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val layoutInflater : LayoutInflater = LayoutInflater.from(Ctx)
        val view : View = layoutInflater.inflate(resource,null)

        val title : TextView = view.findViewById(R.id.fnfitem)
        val phone : TextView = view.findViewById(R.id.fnfphone)
        val removeitem : Button = view.findViewById(R.id.toremoveservicesbtn)
        val pic : ImageView = view.findViewById(R.id.img_fnf)

        val item : Friends = Item[position]
        title.text = "${item.firstName} ${item.lastName}"
        phone.text = item.phone
        Picasso.with(context)
            .load(item.image)
            .into(pic);

        removeitem.setOnClickListener {
            Item.removeAt(position)
            deleteMethodforfnf(Item[position].phone.toString().trim())
            notifyDataSetChanged()
        }


        return view
    }

}