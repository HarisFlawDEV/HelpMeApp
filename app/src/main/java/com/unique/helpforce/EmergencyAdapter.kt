package com.unique.helpforce

import android.app.Dialog
import android.content.Context
import android.content.SharedPreferences
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.cardview.widget.CardView
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.emergencycardview.view.*
import org.w3c.dom.Text

class EmergencyAdapter(var context: Context, private var arrayList: ArrayList<EmergencyList>) :
    RecyclerView.Adapter<EmergencyAdapter.ItemHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemHolder {
        val viewHolder = LayoutInflater.from(parent.context)
            .inflate(R.layout.emergencycardview, parent, false)
        return ItemHolder(viewHolder)
    }

    override fun getItemCount(): Int {
        return arrayList.size
    }
    fun StoreInSharedPref(emergencyname: String)
    {
        val sharedPrefFile = "helpforce"

        val sharedPreferences: SharedPreferences =
            this.context.getSharedPreferences(
                sharedPrefFile,
                Context.MODE_PRIVATE
            )
        val editor: SharedPreferences.Editor = sharedPreferences.edit()
        editor.putString("emergencyname", emergencyname)
        editor.putBoolean("emergencyselected",true)
        editor.apply()
    }


    override fun onBindViewHolder(holder: ItemHolder, position: Int) {

        val charItem: EmergencyList = arrayList[position]

        holder.icons.setImageResource(charItem.icon!!)
        holder.titles.text = charItem.name

        holder.cardview.setOnClickListener {
            StoreInSharedPref(charItem.name.toString().trim())
            Toast.makeText(context, charItem.name.toString().trim(), Toast.LENGTH_LONG).show()
        }

        if (holder.titles.text == "add"){
            holder.cardview.setOnClickListener {
                val Dialog : Dialog = Dialog(this.context)
                Dialog.setContentView(R.layout.addotherdescdialog)
                val description : TextView = Dialog.findViewById(R.id.itemdescription)
                val donebtn : Button = Dialog.findViewById(R.id.okbtnadd)
                val cancelbtn : Button = Dialog.findViewById(R.id.cancelbtnadd)
                if (Dialog.window != null)
                {
                    Dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                }
                Dialog.show()
                donebtn.setOnClickListener {
                    StoreInSharedPref(description.text.toString().trim())
                    Dialog.dismiss()

                }
                cancelbtn.setOnClickListener {
                    Dialog.dismiss()
                }

            }
        }
            if (holder.titles.text == "Blood Group"){
            (holder.cardview.setOnClickListener {
                StoreInSharedPref(charItem.name.toString().trim())
                val popupMenu: PopupMenu = PopupMenu(context,holder.cardview)
                popupMenu.menuInflater.inflate(R.menu.bloodgroupmenu,popupMenu.menu)
                popupMenu.setOnMenuItemClickListener(PopupMenu.OnMenuItemClickListener { item ->
                    when(item.itemId) {
                        R.id.bloodABminus ->
                            Toast.makeText(context, "You Clicked : " + item.title, Toast.LENGTH_SHORT).show()
                        R.id.bloodAminus ->
                            Toast.makeText(context, "You Clicked : " + item.title, Toast.LENGTH_SHORT).show()
                        R.id.bloodBminus ->
                            Toast.makeText(context, "You Clicked : " + item.title, Toast.LENGTH_SHORT).show()
                        R.id.bloodOminus ->
                            Toast.makeText(context, "You Clicked : " + item.title, Toast.LENGTH_SHORT).show()
                        R.id.bloodAplus ->
                            Toast.makeText(context, "You Clicked : " + item.title, Toast.LENGTH_SHORT).show()
                        R.id.bloodBplus ->
                            Toast.makeText(context, "You Clicked : " + item.title, Toast.LENGTH_SHORT).show()
                        R.id.bloodOplus ->
                            Toast.makeText(context, "You Clicked : " + item.title, Toast.LENGTH_SHORT).show()
                        R.id.bloodABplus ->
                            Toast.makeText(context, "You Clicked : " + item.title, Toast.LENGTH_SHORT).show()
                    }
                    true
                })
                popupMenu.show()
            })
        }
    }

    class ItemHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var icons = itemView.findViewById(R.id.emergencyid) as ImageView
        var titles = itemView.findViewById(R.id.emergencyname) as TextView
        val cardview = itemView.findViewById(R.id.emergencyitem) as CardView

    }
}