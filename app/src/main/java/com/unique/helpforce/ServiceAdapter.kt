package com.unique.helpforce

import android.content.Context
import android.content.SharedPreferences
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.PopupMenu
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.servicecardview.view.*

class ServiceAdapter(var context: Context, private var arrayList: ArrayList<ServiceList>) :
    RecyclerView.Adapter<ServiceAdapter.ItemHolder>() {

    fun StoreInSharedPref(servicename: String)
    {
        val sharedPrefFile = "helpforce"

        val sharedPreferences: SharedPreferences =
            this.context.getSharedPreferences(
                sharedPrefFile,
                Context.MODE_PRIVATE
            )
        val editor: SharedPreferences.Editor = sharedPreferences.edit()
        editor.putString("servicename", servicename)
        editor.putBoolean("serviceselected",true)
        editor.apply()
    }



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemHolder {
        val viewHolder = LayoutInflater.from(parent.context)
            .inflate(R.layout.servicecardview, parent, false)
        return ItemHolder(viewHolder)
    }

    override fun getItemCount(): Int {
        return arrayList.size
    }
    class ItemHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var icons = itemView.findViewById(R.id.serviceid) as ImageView
        var titles = itemView.findViewById(R.id.servicename) as TextView
        val cardview = itemView.findViewById(R.id.serviceitem) as CardView

    }

    override fun onBindViewHolder(holder: ServiceAdapter.ItemHolder, position: Int) {

        val charItem: ServiceList = arrayList[position]

        holder.icons.setImageResource(charItem.icon!!)
        holder.titles.text = charItem.name

        holder.titles.setOnClickListener {
            Toast.makeText(context, charItem.name, Toast.LENGTH_LONG).show()
        }

        holder.cardview.setOnClickListener {
            StoreInSharedPref(charItem.name.toString().trim())
            Toast.makeText(context, charItem.name.toString().trim(), Toast.LENGTH_LONG).show()
        }
    }
}