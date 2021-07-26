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


class HelpFragment : Fragment() {

    private lateinit var v : View
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val v = inflater.inflate(R.layout.fragment_help, container, false)
        return v


    } override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        ChangingNotifyFragment(this)
    }
    companion object {
        private fun ChangingNotifyFragment(helpFragment: HelpFragment) {

            val tabs: TabLayout = helpFragment.requireView().findViewById(R.id.tablayoutreceivedsent)
            val viewpager: ViewPager = helpFragment.requireView().findViewById(R.id.viewpager)
            val adapter = ViewPageAdapter(helpFragment.childFragmentManager)

            //Add Fragment Here
            adapter.addFragment(ProvidedHelpFragment(),"Received")

            viewpager.adapter = adapter
            tabs.setupWithViewPager(viewpager)
        }
    }



}