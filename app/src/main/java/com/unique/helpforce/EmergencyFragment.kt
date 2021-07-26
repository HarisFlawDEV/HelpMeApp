package com.unique.helpforce

import android.content.ClipDescription
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.auth.zzd.getToken
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView
import com.unique.helpforce.retrofit.*
import kotlinx.android.synthetic.main.activity_main_screen.*
import kotlinx.android.synthetic.main.emergencycardview.*
import kotlinx.android.synthetic.main.fragment_emergency.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class EmergencyFragment : Fragment() {

//    private var BearerToken : String = "Bearer "+getToken()

    private fun HelpRequest(authHeader : String,description: String,longitude : Double, latitude : Double ,nearbyUsersAllowed : Boolean,fnfAllowed : Boolean, urgencyLevel : String)
    {
        val retIn = RetrofitClient.getRetrofitInstance().create(API::class.java)

        retIn.helpRequest(authHeader,description,longitude,latitude,nearbyUsersAllowed,fnfAllowed,urgencyLevel).enqueue(object : Callback<HelpRequestResponse> {
            override fun onFailure(call: Call<HelpRequestResponse>, t: Throwable) {
                Log.d("exception",t.message.toString())
                Toast.makeText(requireContext(), t.message, Toast.LENGTH_SHORT).show()
            }

            override fun onResponse(call: Call<HelpRequestResponse>, response: Response<HelpRequestResponse>) {
                if (response.code() == 200) {
                    Toast.makeText(requireContext(), "YAYYY! ${response.body()!!.description}", Toast.LENGTH_SHORT).show()
                }
                else {
                    Toast.makeText(requireContext(),"MASLA ${response.code()} , ${response.message()}, ${response.errorBody().toString()}",Toast.LENGTH_LONG).show()
                }
            }
        }
        )
    }




    //crating an arraylist to store users using the data class user
    private lateinit var emergencyList : ArrayList<EmergencyList>
    private lateinit var v : View

    private fun getEmergencyname() :String{
        val prefs = requireActivity().getSharedPreferences("helpforce", AppCompatActivity.MODE_PRIVATE)
        return prefs.getString("emergencyname","")!!
    }


    private fun getToken() :String{
        val prefs = requireActivity().getSharedPreferences("helpforce", AppCompatActivity.MODE_PRIVATE)
        return prefs.getString("token","")!!
    }

    private fun getLatitude() :Double{
        val prefs = requireActivity().getSharedPreferences("helpforce", AppCompatActivity.MODE_PRIVATE)
        return prefs.getString("Latitude","")!!.toDouble()
    }
    private fun getLongitude() :Double{
        val prefs = requireActivity().getSharedPreferences("helpforce", AppCompatActivity.MODE_PRIVATE)
        return prefs.getString("Longitude","")!!.toDouble()
    }
    override fun setUserVisibleHint(visible: Boolean) {
        super.setUserVisibleHint(visible)
        if (visible && isResumed) {
            onResume()
        }
    }
    fun getSeekbarProgress() : String{
        return when(seekBar.progress)
        {
            0-> "Low"
            1-> "Moderate"
            2-> "High"
            else -> "Low"
        }

    }
    private fun getemergency() :Boolean?{
        val prefs = requireActivity().getSharedPreferences("helpforce", AppCompatActivity.MODE_PRIVATE)
        return prefs?.getBoolean("emergencyselected",false)
    }
    fun unselectEmergency()
    {
        val sharedPrefFile = "helpforce"

        val sharedPreferences: SharedPreferences =
            requireContext().getSharedPreferences(
                sharedPrefFile,
                Context.MODE_PRIVATE
            )
        val editor: SharedPreferences.Editor = sharedPreferences.edit()
        editor.putBoolean("emergencyselected",false)
        editor.apply()
    }
    fun showLoadingDialog()
    {
        val Dialog : AlertDialog
        val DialogView = LayoutInflater.from(requireContext()).inflate(
            R.layout.searchinguserdialog,
            null
        )
        val builder = AlertDialog.Builder(requireContext())
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


    override fun onResume() {
        super.onResume()
        if (!userVisibleHint) {
            return
        }
        val emerge = activity as MainScreenActivity?
        emerge!!.fab.setOnClickListener {

            Log.d("params","Bearer ${getToken()},${getEmergencyname()} ,${getLongitude()},${getLatitude()},true,${getSeekbarProgress()}")

            if(getemergency()!! && checkboxfornearby.isChecked && !checkboxforfnf.isChecked)
            {
                showLoadingDialog()
                HelpRequest("Bearer ${getToken()}",getEmergencyname(),getLongitude(),getLatitude(),true,false,getSeekbarProgress())
            }
            else if(getemergency()!! && checkboxforfnf.isChecked  && !checkboxfornearby.isChecked)
            {
                showLoadingDialog()
                HelpRequest("Bearer ${getToken()}",getEmergencyname(),getLongitude(),getLatitude(),false,true,getSeekbarProgress())
            }
            else if(getemergency()!! && checkboxforfnf.isChecked && checkboxfornearby.isChecked)
            {
                showLoadingDialog()
                HelpRequest("Bearer ${getToken()}",getEmergencyname(),getLongitude(),getLatitude(),true,true,getSeekbarProgress())
            }
            else
            {
                Toast.makeText(requireContext(),"First Select Emergency and at least one checkobx",Toast.LENGTH_SHORT).show()
            }
            unselectEmergency()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        v = inflater.inflate(R.layout.fragment_emergency, container, false)

        //getting recyclerview from xml
        val recyclerView = v.findViewById(R.id.EmergencyRecyclerView) as RecyclerView

        emergencyList = ArrayList()
        emergencyList = setEmerge()

        //creating our adapter
        val adapter = EmergencyAdapter(requireContext(), emergencyList)

        //now adding the adapter to recyclerview
        recyclerView.adapter = adapter

        //adding a layout Manager
        val gridLayoutManager = GridLayoutManager(
            requireContext(),
            2,
            LinearLayoutManager.VERTICAL,
            false
        )
        recyclerView.layoutManager = gridLayoutManager
        recyclerView.setHasFixedSize(true)


        // Inflate the layout for this fragment
        return v
    }

    private fun setEmerge(): ArrayList<EmergencyList> {


        val arrayList: ArrayList<EmergencyList> = ArrayList()

        arrayList.add(EmergencyList(R.drawable.bloodgroup, "Blood Group"))
        arrayList.add(EmergencyList(R.drawable.sexualharassment, "Harassment"))
        arrayList.add(EmergencyList(R.drawable.carcollision, "Accident"))
        arrayList.add(EmergencyList(R.drawable.round_add_black_48, "add"))
        return arrayList
    }


}