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
import kotlinx.android.synthetic.main.fragment_help_provided.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class ProvidedHelpFragment : Fragment() {
    private fun getToken() :String{
        val prefs = requireActivity().getSharedPreferences("helpforce", AppCompatActivity.MODE_PRIVATE)
        return prefs.getString("token","")!!
    }

    private fun getuserid() :String{
        val prefs = requireActivity().getSharedPreferences("helpforce", AppCompatActivity.MODE_PRIVATE)
        return prefs.getString("userid","")!!
    }

    fun ShowlistofHelpNeededUsers(){

        val rtn =  RetrofitClient.getRetrofitInstance().create(API::class.java)

        CoroutineScope(Dispatchers.IO).launch {
            //auth Header of ServiceProvider
            val response = rtn.showlistofServiceNeededUsers("Bearer ${getToken()}")

            withContext(Dispatchers.Main) {
                if (response.code() == 200) {

//                    Toast.makeText(requireContext(), "YAYYY! ${response.body()!!.helpRequests.toString()}", Toast.LENGTH_SHORT).show()


                   ProvidedHelpRecyclerView.apply {
                                   //adding a layout Manager
                                         layoutManager = LinearLayoutManager(activity)
                                         adapter = RecyclerViewAdapter(requireContext(),this@ProvidedHelpFragment,response.body()!!.helpRequests)
 //                                        adapter!!.notifyDataSetChanged();
                                     }

                }
                else {
                    Toast.makeText(
                        requireContext(),
                        "MASLA ${response.code()} , ${response.message()}, ${
                            response.errorBody().toString()
                        }",
                        Toast.LENGTH_LONG
                    ).show()
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

        v = inflater.inflate(R.layout.fragment_help_provided, container, false)
//        ShowlistofServiceNeededUsers()

/*

        //getting recyclerview from xml
        val recyclerView = v.findViewById(R.id.ProvidedHelpRecyclerView) as RecyclerView

        //creating our adapter
        val adapter = RecyclerViewAdapter(requireContext(), list as ArrayList<DisplayRequestsItem>)

        //adding a layout Manager
        recyclerView.layoutManager = LinearLayoutManager(activity)

        //now adding the adapter to recyclerview
        recyclerView.adapter = adapter

*/

        return  v

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        ShowlistofHelpNeededUsers()
    }
/*
        //adding some dummy data to the list
        users.add(VictimUsers("Belal Khan", R.drawable.account_icon,"Blood Group (O +ve)","Completed","0223323213"))
        users.add(VictimUsers("Belal Khan", R.drawable.account_icon,"Blood Group (O +ve)","Completed","0223323213"))
        users.add(VictimUsers("Belal Khan", R.drawable.account_icon,"Blood Group (O +ve)","Completed","0223323213"))
        users.add(VictimUsers("Belal Khan", R.drawable.account_icon,"Blood Group (O +ve)","Completed","0223323213"))
        users.add(VictimUsers("Belal Khan", R.drawable.account_icon,"Blood Group (O +ve)","Completed","0223323213"))
        users.add(VictimUsers("Belal Khan", R.drawable.account_icon,"Blood Group (O +ve)","Completed","0223323213"))
        users.add(VictimUsers("Belal Khan", R.drawable.account_icon,"Blood Group (O +ve)","Completed","0223323213"))
        users.add(VictimUsers("Belal Khan", R.drawable.account_icon,"Blood Group (O +ve)","Completed","0223323213"))
        users.add(VictimUsers("Belal Khan", R.drawable.account_icon,"Blood Group (O +ve)","Completed","0223323213"))
*/

    }



