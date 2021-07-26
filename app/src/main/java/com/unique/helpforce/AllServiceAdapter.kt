package com.unique.helpforce

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import com.squareup.picasso.Picasso
import com.unique.helpforce.retrofit.ListOfServices

class AllServiceAdapter (private var Ctx : Context, private var resource : Int, private var Item : ArrayList<ListOfServices>)
    : ArrayAdapter<ListOfServices>(Ctx,resource,Item) {
    @SuppressLint("ViewHolder", "SetTextI18n")
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val layoutInflater : LayoutInflater = LayoutInflater.from(Ctx)
        val view : View = layoutInflater.inflate(resource,null)

        val imageView : ImageView = view.findViewById(R.id.service_img)
        val title : TextView = view.findViewById(R.id.servicename)

        val item : ListOfServices = Item[position]
        title.text = item.name
/*
        Picasso.with(context)
            .load(item.image)
            .into(imageView);
*/


        return view
    }


    }