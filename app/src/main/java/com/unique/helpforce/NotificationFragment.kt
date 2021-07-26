package com.unique.helpforce

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import com.unique.helpforce.retrofit.API
import com.unique.helpforce.retrofit.RetrofitClient
import kotlinx.android.synthetic.main.activity_main_screen.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


open class NotificationFragment :Fragment() {

    override fun setUserVisibleHint(visible: Boolean) {
        super.setUserVisibleHint(visible)
        if (visible && isResumed) {
            onResume()
        }
    }
    override fun onResume() {
        super.onResume()
        if (!userVisibleHint) {
            return
        }
        val emerge = activity as MainScreenActivity?
        emerge!!.fab.setOnClickListener {

            val fragment = EmergencyFragment()
            fragmentManager?.beginTransaction()
                ?.replace(R.id.container, fragment, fragment.javaClass.simpleName)
                ?.commit()


        }
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
        inflater.inflate(R.layout.fragment_notification, container, false)
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        ChangingNotifyFragment(this)

    }

    companion object {
        private fun ChangingNotifyFragment(notificationFragment: NotificationFragment) {

            val tabs: TabLayout = notificationFragment.requireView().findViewById(R.id.tablayouthelpservice)
            val viewpager: ViewPager = notificationFragment.requireView().findViewById(R.id.viewpager)
            val adapter = ViewPageAdapter(notificationFragment.childFragmentManager)

            //Add Fragment Here
            adapter.addFragment(HelpFragment(),"Help")
            adapter.addFragment(ServiceNotifFragment(),"Service")

            viewpager.adapter = adapter
            tabs.setupWithViewPager(viewpager)
        }
    }

}