package com.unique.helpforce

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.SharedPreferences
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.Handler
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayoutStates.TAG
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.unique.helpforce.retrofit.*
import kotlinx.android.synthetic.main.activity_main_screen.*
import kotlinx.android.synthetic.main.fragment_emergency.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ServiceFragment : Fragment() {

    private fun getToken() :String{
        val prefs = requireActivity().getSharedPreferences("helpforce", AppCompatActivity.MODE_PRIVATE)
        return prefs.getString("token","")!!
    }

    private fun getuserid() :String{
        val prefs = requireActivity().getSharedPreferences("helpforce", AppCompatActivity.MODE_PRIVATE)
        return prefs.getString("userid","")!!
    }

    private fun getServicename() :String{
        val prefs = requireActivity().getSharedPreferences("helpforce", AppCompatActivity.MODE_PRIVATE)
        return prefs.getString("servicename","")!!
    }
    private fun getservice() :Boolean?{
        val prefs = requireActivity().getSharedPreferences("helpforce", AppCompatActivity.MODE_PRIVATE)
        return prefs?.getBoolean("serviceselected",false)
    }
    fun unselectservice()
    {
        val sharedPrefFile = "helpforce"

        val sharedPreferences: SharedPreferences =
            requireContext().getSharedPreferences(
                sharedPrefFile,
                Context.MODE_PRIVATE
            )
        val editor: SharedPreferences.Editor = sharedPreferences.edit()
        editor.putBoolean("serviceselected",false)
        editor.apply()
    }
    fun showLoadingDialog()
    {
        val Dialog : androidx.appcompat.app.AlertDialog
        val DialogView = LayoutInflater.from(requireContext()).inflate(
            R.layout.searchinguserdialog,
            null
        )
        val builder = androidx.appcompat.app.AlertDialog.Builder(requireContext())
            .setView(DialogView)
            .setCancelable(false)
        Dialog = builder.create()
        Dialog.show()

        val handler = Handler()
        handler.postDelayed(Runnable() {
            kotlin.run {

                Dialog.dismiss()
            }
        }, 5000)

    }

    fun FetchAllServiceProviders(auth : String,service : String) {
        val rtn =  RetrofitClient.getRetrofitInstance().create(API::class.java)

        CoroutineScope(Dispatchers.IO).launch {
            // Do the GET request and get response
            //auth Header of ServiceProvider
            val response = rtn.fetchallServiceProviders(auth,service)
            withContext(Dispatchers.Main) {
                if (response.code() == 200) {
                    Toast.makeText(requireContext(), "YAYYY! ${response.body()!!.toString()}", Toast.LENGTH_SHORT).show()

                    val dialog : Dialog = Dialog(requireActivity())
                    dialog.setContentView(R.layout.listviewserviceprovider)
                    if (dialog.window != null)
                    {
                        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                    }
                    val listView :ListView = dialog.findViewById(R.id.serviceproviderlist)
                    val arrayAdapter: ArrayAdapter<*> =
                        ServiceProviderAdapter(requireContext(), R.layout.serviceprovideruser,
                            response.body()!! as ArrayList<ServiceProvider>
                        )
                    listView.adapter = arrayAdapter
                    listView.setOnItemClickListener { adapterView, view, which, l ->
                        Log.d("Check",auth+" "+response.body()!![which]._id+" "+service)
                        SendRequesttoServiceProvider(auth,response.body()!![which]._id,service)
                        dialog.dismiss()
                    }
                    dialog.show()
                }
                else {
                    Toast.makeText(requireContext(),"MASLA ${response.code()} , ${response.message()}, ${response.errorBody().toString()}",Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    private fun SendRequesttoServiceProvider(authHeader : String,serviceProviderId : String , serviceName : String )
    {
        val retIn = RetrofitClient.getRetrofitInstance().create(API::class.java)

        retIn.sendRequesttoServiceProvider(authHeader,serviceProviderId,serviceName).enqueue(object :
            Callback<RequesttoServiceProviderResponse> {
            override fun onFailure(call: Call<RequesttoServiceProviderResponse>, t: Throwable) {
                Toast.makeText(requireContext(), t.message, Toast.LENGTH_SHORT).show()
            }

            override fun onResponse(call: Call<RequesttoServiceProviderResponse>, response: Response<RequesttoServiceProviderResponse>) {
                if (response.code() == 200) {
                    Toast.makeText(requireContext(), "YAYYY!", Toast.LENGTH_SHORT).show()
                }
                else {
                    Toast.makeText(requireContext(),"MASLA ${response.code()} , ${response.message()}, ${response.errorBody().toString()}",
                        Toast.LENGTH_LONG).show()
                }
            }
        }
        )
    }

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

            if(getservice()!!) {
                showLoadingDialog()
                FetchAllServiceProviders("Bearer ${getToken()}",getServicename())
            }
            else {
                Toast.makeText(requireContext(),"First Select Service",Toast.LENGTH_SHORT).show()
            }
            unselectservice()
        }
    }
    //crating an arraylist to store users using the data class user
    private lateinit var ServiceList : ArrayList<ServiceList>
    private lateinit var v : View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        v = inflater.inflate(R.layout.fragment_service, container, false)
   //getting recyclerview from xml
        val recyclerView = v.findViewById(R.id.ServiceRecyclerView) as RecyclerView

        ServiceList = ArrayList()
        ServiceList = setService()

        //creating our adapter
        val adapter = ServiceAdapter(requireContext(),ServiceList)

        //now adding the adapter to recyclerview
        recyclerView.adapter = adapter

        //adding a layout Manager
        val gridLayoutManager = GridLayoutManager(requireContext(), 2, LinearLayoutManager.VERTICAL, false)
        recyclerView.layoutManager = gridLayoutManager
        recyclerView.setHasFixedSize(true)


        // Inflate the layout for this fragment
        return v
    }
    private fun setService(): ArrayList<ServiceList> {

        val arrayList: ArrayList<ServiceList> = ArrayList()
        arrayList.add(ServiceList(R.drawable.electrician,"electrician"))
        arrayList.add(ServiceList(R.drawable.plumbing,"plumber"))
        arrayList.add(ServiceList(R.drawable.cleaning,"cleaning"))
        arrayList.add(ServiceList(R.drawable.carmechanic,"car mechanic"))
        return arrayList
    }


}