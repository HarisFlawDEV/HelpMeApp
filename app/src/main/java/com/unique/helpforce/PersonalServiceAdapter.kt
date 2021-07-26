package com.unique.helpforce

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.TextView
import com.unique.helpforce.retrofit.ListOfOwnServices
import com.unique.helpforce.retrofit.OwnServices
import com.unique.helpforce.retrofit.Services

class PersonalServiceAdapter(private var Ctx: Context, private var resource: Int, private var Item: ArrayList<Services>)
    : ArrayAdapter<Services>(Ctx,resource,Item) {
    @SuppressLint("ViewHolder")
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val layoutInflater : LayoutInflater = LayoutInflater.from(Ctx)
        val view : View = layoutInflater.inflate(resource,null)

        val title : TextView = view.findViewById(R.id.serviceitem)
        val removeitem : Button = view.findViewById(R.id.toremoveservicesbtn)

        val item : Services = Item[position]
        title.text = item.name

        removeitem.setOnClickListener {
            Item.removeAt(position)
            notifyDataSetChanged()
        }
        return view
    }

}