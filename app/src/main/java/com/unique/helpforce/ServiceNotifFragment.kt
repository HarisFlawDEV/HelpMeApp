package com.unique.helpforce

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import kotlinx.android.synthetic.main.activity_main_screen.*


class ServiceNotifFragment : Fragment() {

    //crating an arraylist to store users using the data class user
/*
    private val users = ArrayList<HelperUsers>()
*/
    private lateinit var v : View



    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val v = inflater.inflate(R.layout.fragment_servicenotif, container, false)
/*

        //getting recyclerview from xml
        val recyclerView = v.findViewById(R.id.NeededHelpRecyclerView) as RecyclerView

        //creating our adapter
        val adapter = RecyclerViewAdapterForSent(requireContext(), users)

        //adding a layout Manager
        recyclerView.layoutManager = LinearLayoutManager(activity)

        //now adding the adapter to recyclerview
        recyclerView.adapter = adapter



*/


        return v


    } override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        ChangingNotifyFragment(this)

/*
        //adding some dummy data to the list
        users.add(HelperUsers("Belal Khan", R.drawable.account_icon,"Blood Group (O +ve)","Completed","0223323213"))
        users.add(HelperUsers("Belal Khan", R.drawable.account_icon,"Blood Group (O +ve)","Completed","0223323213"))
        users.add(HelperUsers("Belal Khan", R.drawable.account_icon,"Blood Group (O +ve)","Completed","0223323213"))
        users.add(HelperUsers("Belal Khan", R.drawable.account_icon,"Blood Group (O +ve)","Completed","0223323213"))
        users.add(HelperUsers("Belal Khan", R.drawable.account_icon,"Blood Group (O +ve)","Completed","0223323213"))
        users.add(HelperUsers("Belal Khan", R.drawable.account_icon,"Blood Group (O +ve)","Completed","0223323213"))
        users.add(HelperUsers("Belal Khan", R.drawable.account_icon,"Blood Group (O +ve)","Completed","0223323213"))

*/

    }
    companion object {
        private fun ChangingNotifyFragment(serviceNotifFragment: ServiceNotifFragment) {

            val tabs: TabLayout = serviceNotifFragment.requireView().findViewById(R.id.tablayoutreceivedsent)
            val viewpager: ViewPager = serviceNotifFragment.requireView().findViewById(R.id.viewpager)
            val adapter = ViewPageAdapter(serviceNotifFragment.childFragmentManager)

            //Add Fragment Here
            adapter.addFragment(ServiceReceivedFragment(),"Received")

            viewpager.adapter = adapter
            tabs.setupWithViewPager(viewpager)
        }
    }



}