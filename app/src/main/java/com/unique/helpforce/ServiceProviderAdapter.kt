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

class ServiceProviderAdapter (private var Ctx : Context, private var resource : Int, private var Item : ArrayList<ServiceProvider>)
    : ArrayAdapter<ServiceProvider>(Ctx,resource,Item) {
    @SuppressLint("ViewHolder", "SetTextI18n")
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val layoutInflater : LayoutInflater = LayoutInflater.from(Ctx)
        val view : View = layoutInflater.inflate(resource,null)

        val imageView : ImageView = view.findViewById(R.id.img_user)
        val title : TextView = view.findViewById(R.id.providername)
        val rating : RatingBar = view.findViewById(R.id.rating)
        //val location : TextView = view.findViewById(R.id.location)

        val item : ServiceProvider = Item[position]
        title.text = "${item.firstName} ${item.lastName}"
        rating.rating = item.ratingAvg.toFloat()
 //       location.text = "${item.}"
        Picasso.with(context)
            .load(item.image)
            .into(imageView);


        return view
    }


    }