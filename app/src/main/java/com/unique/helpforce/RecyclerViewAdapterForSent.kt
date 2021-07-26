package com.unique.helpforce
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.content.SharedPreferences
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import com.unique.helpforce.retrofit.*

class RecyclerViewAdapterForSent(
    private val mContext: Context,
    private val fragmentAdapter: Fragment,
    private val UserList: List<HelpProviders>
) : RecyclerView.Adapter<RecyclerViewAdapterForSent.MyViewHolder>()  {

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val DialogProfile = itemView.findViewById(R.id.helper_user) as LinearLayout
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {

        val v = LayoutInflater.from(parent.context).inflate(R.layout.helperuser, parent, false)
        return MyViewHolder(v)
    }

    override fun getItemCount(): Int = UserList.size
    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        val textViewName = holder.itemView.findViewById(R.id.helper_name) as TextView
        val textViewHelpType  = holder.itemView.findViewById(R.id.help_type) as TextView
        val textViewHelpStatus  = holder.itemView.findViewById(R.id.helpstatus) as TextView
        val imgViewPhoto = holder.itemView.findViewById(R.id.img_user) as ImageView

        textViewName.text = UserList[position].createdAt
        textViewHelpType.text = UserList[position].description
        textViewHelpStatus.text =UserList[position].status

/*
        Picasso.with(mContext)
            .load(UserList[position].creatorId.image)
            .into(imgViewPhoto);
*/

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
                Dialog_phone.visibility = View.GONE
                Dialog_name.text = UserList[position].createdAt
                Dialog_helptype.text = UserList[position].description
                Dialog_helpstatus.text = UserList[position].status
/*
                Picasso.with(mContext)
                    .load(UserList[position].creatorId.image)
                    .into(Dialog_image);
*/
/*
                Dialog_phone.text = UserList[position].creatorId.phone
*/

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
                        UserList[position].requestedTo[0].uid.location.coordinates[0].toString()
                    )
                    editor.putString(
                        "CoordLat",
                        UserList[position].requestedTo[0].uid.location.coordinates[1].toString()
                    )
                    editor.putBoolean(
                        "ShowOther", true
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
