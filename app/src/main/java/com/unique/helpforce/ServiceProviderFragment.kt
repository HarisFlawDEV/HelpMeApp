package com.unique.helpforce

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.activity_main_screen.*


class ServiceProviderFragment : Fragment() {

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

            val fragment = ServiceProviderFragment()
            fragmentManager?.beginTransaction()
                ?.replace(R.id.container, fragment, fragment.javaClass.simpleName)
                ?.commit()


        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.listviewserviceprovider, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
    }



}