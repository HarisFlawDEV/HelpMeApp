package com.unique.helpforce

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.content.SharedPreferences
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isInvisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.findFragment
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import com.unique.helpforce.retrofit.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class RecyclerViewAdapter(
    val mContext: Context,
    val fragmentAdapter: Fragment,
    val UserList: List<HelpRequests>
) : RecyclerView.Adapter<RecyclerViewAdapter.MyViewHolder>()  {

    private fun getToken() :String{
        val prefs = this.mContext.getSharedPreferences("helpforce", AppCompatActivity.MODE_PRIVATE)
        return prefs.getString("token","")!!
    }

    private fun getuserid() :String{
        val prefs = this.mContext.getSharedPreferences("helpforce", AppCompatActivity.MODE_PRIVATE)
        return prefs.getString("userid","")!!
    }

    private fun HelpRequestRespond(authHeader: String, requestedId: String, action: String)
    {
        val retIn = RetrofitClient.getRetrofitInstance().create(API::class.java)

        retIn.helpRequestRespond(authHeader, requestedId, action).enqueue(object :
            Callback<RequestRespondResponse> {
            override fun onFailure(call: Call<RequestRespondResponse>, t: Throwable) {
                Toast.makeText(mContext, t.message, Toast.LENGTH_SHORT).show()
            }

            override fun onResponse(
                call: Call<RequestRespondResponse>,
                response: Response<RequestRespondResponse>
            ) {
                if (response.code() == 200) {
                    Toast.makeText(mContext, "YAYYY!", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(
                        mContext,
                        "MASLA ${response.code()} , ${response.message()}, ${
                            response.errorBody().toString()
                        }",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }
        )
    }


    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val DialogProfile = itemView.findViewById(R.id.item_user) as LinearLayout
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {

        val v = LayoutInflater.from(parent.context).inflate(R.layout.itemuser, parent, false)
        return MyViewHolder(v)
    }

    override fun getItemCount(): Int = UserList.size
    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        val textViewName = holder.itemView.findViewById(R.id.victim_name) as TextView
        val textViewHelpType  = holder.itemView.findViewById(R.id.help_type) as TextView
        val textViewHelpStatus  = holder.itemView.findViewById(R.id.helpstatus) as TextView
        val imgViewPhoto = holder.itemView.findViewById(R.id.img_user) as ImageView
        val donebtn = holder.itemView.findViewById(R.id.acceptbtn) as Button
        val cancelbtn = holder.itemView.findViewById(R.id.rejectbtn) as Button
        val linearLayout = holder.itemView.findViewById(R.id.llforbtn) as LinearLayout


        textViewName.text = "${UserList[position].creatorId.firstName}  ${UserList[position].creatorId.lastName}"
        textViewHelpType.text = UserList[position].description
        textViewHelpStatus.text =UserList[position].status

        if(textViewHelpStatus.text != "Pending")
        {
            linearLayout.visibility = View.GONE
        }
        Picasso.with(mContext)
            .load(UserList[position].creatorId.image)
            .into(imgViewPhoto);

        donebtn.setOnClickListener {
            HelpRequestRespond(
                "Bearer ${getToken()}",
                UserList[position]._id,
                "Accepted"
            )
            linearLayout.visibility = View.GONE
            notifyDataSetChanged()
        }
        cancelbtn.setOnClickListener {
            HelpRequestRespond(
                "Bearer ${getToken()}",
                UserList[position]._id,
                "Rejected"
            )
            linearLayout.visibility = View.GONE
            notifyDataSetChanged()
        }

        holder.DialogProfile.setOnClickListener {
            if (textViewHelpStatus.text == "Accepted") {
                val Dialog: AlertDialog
                val DialogView = LayoutInflater.from(mContext).inflate(
                    R.layout.dialog_userprofille,
                    null
                )
                val builder = AlertDialog.Builder(mContext)
                    .setView(DialogView)
                Dialog = builder.create()
                Dialog.show()

                Dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

                val Dialog_name = DialogView.findViewById(R.id.dialog_name) as TextView
                val Dialog_helptype = DialogView.findViewById(R.id.dialog_helptype) as TextView
                val Dialog_helpstatus = DialogView.findViewById(R.id.dialog_status) as TextView
                val Dialog_image = DialogView.findViewById(R.id.dialog_img) as ImageView
                val Dialog_phone = DialogView.findViewById(R.id.dialog_phone) as TextView
                val Dialog_report = DialogView.findViewById(R.id.dialog_location) as Button

                Dialog_name.text =
                    "${UserList[position].creatorId.firstName}  ${UserList[position].creatorId.lastName}"
                Dialog_helptype.text = UserList[position].description
                Dialog_helpstatus.text = UserList[position].status
                Picasso.with(mContext)
                    .load(UserList[position].creatorId.image)
                    .into(Dialog_image);
                Dialog_phone.text = UserList[position].creatorId.phone

                if (textViewHelpStatus.text == "Completed")
                {
                    Dialog_report.visibility = View.GONE
                }

                Dialog_report.setOnClickListener {

                    val sharedPrefFile = "helpforce"
                    val sharedPreferences: SharedPreferences =
                        this.mContext.getSharedPreferences(
                            sharedPrefFile,
                            Context.MODE_PRIVATE
                        )
                    val editor: SharedPreferences.Editor = sharedPreferences.edit()
                    editor.putString(
                        "CoordLong",
                        UserList[position].creatorId.location.coordinates[0].toString()
                    )
                    editor.putString(
                        "CoordLat",
                        UserList[position].creatorId.location.coordinates[1].toString()
                    )
                    editor.putString(
                        "id",
                        UserList[position].creatorId._id.toString()
                    )
                    editor.putBoolean(
                        "ShowOther", true
                    )
                    editor.putBoolean(
                        "helprequest", true
                    )

                    editor.apply()

                    Dialog.dismiss()
                    val fragment = MapFragment()
                    (fragmentAdapter.activity as AppCompatActivity).supportFragmentManager.beginTransaction()
                        .replace(R.id.container, fragment, "MapFragment").addToBackStack(null)
                        .commit()
                }
            }
        }
    }
}
