package com.unique.helpforce

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.unique.helpforce.retrofit.*
import kotlinx.android.synthetic.main.fragment_help_needed.*
import kotlinx.android.synthetic.main.fragment_help_provided.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class NeededHelpFragment : Fragment() {

    private fun getToken() :String{
        val prefs = requireActivity().getSharedPreferences("helpforce", AppCompatActivity.MODE_PRIVATE)
        return prefs.getString("token","")!!
    }

    private fun getuserid() :String{
        val prefs = requireActivity().getSharedPreferences("helpforce", AppCompatActivity.MODE_PRIVATE)
        return prefs.getString("userid","")!!
    }

    fun ShowlistofHelpProvidedUsers() {

        val rtn =  RetrofitClient.getRetrofitInstance().create(API::class.java)

        CoroutineScope(Dispatchers.IO).launch {
            // Do the GET request and get response
            //auth Header of ServiceProvider
            val response = rtn.showlistofHelpProvidedUsers("Bearer ${getToken()}")
            withContext(Dispatchers.Main) {
                if (response.code() == 200) {

                    Toast.makeText(requireContext(), "YAYYY! ", Toast.LENGTH_SHORT).show()
/*
                    NeededHelpRecyclerView.apply {
                        //adding a layout Manager
                        layoutManager = LinearLayoutManager(activity)
                        adapter = RecyclerViewAdapterForSent(requireContext(),this@NeededHelpFragment,response.body()!!.helpSentRequests)
                        //                                        adapter!!.notifyDataSetChanged();
                    }
*/

                }
                else {
                    Toast.makeText(requireContext(),"MASLA ${response.code()} , ${response.message()}, ${response.errorBody().toString()}",
                        Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    private lateinit var v : View

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        v = inflater.inflate(R.layout.fragment_help_needed, container, false)
        return  v
    }
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
//        ShowlistofHelpProvidedUsers()
    }
}
