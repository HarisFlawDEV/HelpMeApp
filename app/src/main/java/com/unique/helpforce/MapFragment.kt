package com.unique.helpforce

import android.app.Dialog
import android.content.Context
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.location.Location
import android.os.*
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.google.android.gms.location.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.google.gson.Gson
import com.unique.helpforce.retrofit.*
import kotlinx.android.synthetic.main.activity_main_screen.*
import kotlinx.android.synthetic.main.reviewandratedialog.*
import okhttp3.OkHttpClient
import okhttp3.Request
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MapFragment : Fragment(), OnMapReadyCallback {

    private fun SetReport(authHeader: String, label: String, reportedTo: String)
    {
        val retIn = RetrofitClient.getRetrofitInstance().create(API::class.java)

        retIn.setReport(authHeader, label, reportedTo).enqueue(object :
            Callback<RequesttoServiceProviderResponse> {
            override fun onFailure(call: Call<RequesttoServiceProviderResponse>, t: Throwable) {
                Toast.makeText(requireContext(), t.message, Toast.LENGTH_SHORT).show()
            }

            override fun onResponse(
                call: Call<RequesttoServiceProviderResponse>,
                response: Response<RequesttoServiceProviderResponse>
            ) {

                if (response.code() == 200) {
                    Toast.makeText(requireContext(), "YAYYY!", Toast.LENGTH_SHORT).show()
                } else {
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
        )
    }



    private fun HelpRequestRespond(authHeader: String, requestedId: String, action: String)
    {
        val retIn = RetrofitClient.getRetrofitInstance().create(API::class.java)

        retIn.helpRequestRespond(authHeader, requestedId, action).enqueue(object :
            Callback<RequestRespondResponse> {
            override fun onFailure(call: Call<RequestRespondResponse>, t: Throwable) {
                Toast.makeText(requireContext(), t.message, Toast.LENGTH_SHORT).show()
            }

            override fun onResponse(
                call: Call<RequestRespondResponse>,
                response: Response<RequestRespondResponse>
            ) {
                if (response.code() == 200) {
                    Toast.makeText(requireContext(), "YAYYY!", Toast.LENGTH_SHORT).show()
                } else {
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
        )
    }


    private fun getToken() :String{
        val prefs = requireActivity().getSharedPreferences(
            "helpforce",
            AppCompatActivity.MODE_PRIVATE
        )
        return prefs.getString("token", "")!!
    }
    private fun getid() :String{
        val prefs = requireActivity().getSharedPreferences(
            "helpforce",
            AppCompatActivity.MODE_PRIVATE
        )
        return prefs.getString("id", "")!!
    }


    private fun getuserid() :String{
        val prefs = requireActivity().getSharedPreferences(
            "helpforce",
            AppCompatActivity.MODE_PRIVATE
        )
        return prefs.getString("userid", "")!!
    }

    fun putMethod(longitude: Double, latitude: Double) {

        val retIn = RetrofitClient.getRetrofitInstance().create(API::class.java)

        retIn.UserLocation("Bearer ${getToken()}", getuserid(), longitude, latitude)
            .enqueue(object : Callback<DefaultResponse> {
                override fun onFailure(call: Call<DefaultResponse>, t: Throwable) {
                    Toast.makeText(requireContext(), t.message, Toast.LENGTH_SHORT).show()
                }

                override fun onResponse(
                    call: Call<DefaultResponse>,
                    response: Response<DefaultResponse>
                ) {
                    if (response.code() == 200) {
                        Toast.makeText(
                            requireContext(),
                            "Location Update .",
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        Toast.makeText(
                            requireContext(),
                            "Failed Location Update ${response.code()}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
            )
    }

    private fun SetRating(authHeader: String, ratedtoId: String, rating: Int)
    {
        val retIn = RetrofitClient.getRetrofitInstance().create(API::class.java)

        retIn.setRating(authHeader, ratedtoId, rating).enqueue(object :
            Callback<SetRatingResponse> {
            override fun onFailure(call: Call<SetRatingResponse>, t: Throwable) {
                Toast.makeText(requireContext(), t.message, Toast.LENGTH_SHORT).show()
            }

            override fun onResponse(
                call: Call<SetRatingResponse>,
                response: Response<SetRatingResponse>
            ) {

                if (response.code() == 200) {
                    Toast.makeText(requireContext(), "YAYYY!", Toast.LENGTH_SHORT).show()
                } else {
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
        )
    }

    private fun SetReview(authHeader: String, userId: String, content: String)
    {
        val retIn = RetrofitClient.getRetrofitInstance().create(API::class.java)

        retIn.setReview(authHeader, userId, content).enqueue(object :
            Callback<ReviewResponse> {
            override fun onFailure(call: Call<ReviewResponse>, t: Throwable) {
                Toast.makeText(requireContext(), t.message, Toast.LENGTH_SHORT).show()
            }

            override fun onResponse(
                call: Call<ReviewResponse>,
                response: Response<ReviewResponse>
            ) {

                if (response.code() == 200) {
                    Toast.makeText(requireContext(), "YAYYY!", Toast.LENGTH_SHORT).show()
                }
                else if (response.code() == 400)
                {
                    Toast.makeText(requireContext(), "You are not allowed to give any review", Toast.LENGTH_SHORT).show()
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
        )
    }

    private fun ServiceRequestRespond(authHeader: String, requestedId: String, action: String)
    {
        val retIn = RetrofitClient.getRetrofitInstance().create(API::class.java)

        retIn.respondtoServicesRequest(authHeader, requestedId, action).enqueue(object :
            Callback<RespondtoServicesRequestResponse> {
            override fun onFailure(call: Call<RespondtoServicesRequestResponse>, t: Throwable) {
                Toast.makeText(requireContext(), t.message, Toast.LENGTH_SHORT).show()
            }

            override fun onResponse(
                call: Call<RespondtoServicesRequestResponse>,
                response: Response<RespondtoServicesRequestResponse>
            ) {
                if (response.code() == 200) {
                    Toast.makeText(requireContext(), "YAYYY!", Toast.LENGTH_SHORT).show()
                } else {
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
        )
    }



    private fun isHelpRequest() :Boolean?{
        val prefs = requireActivity().getSharedPreferences(
            "helpforce",
            AppCompatActivity.MODE_PRIVATE
        )
        return prefs?.getBoolean("helprequest", false)
    }


    private fun getother() :Boolean?{
        val prefs = requireActivity().getSharedPreferences(
            "helpforce",
            AppCompatActivity.MODE_PRIVATE
        )
        return prefs?.getBoolean("ShowOther", false)
    }

    private fun getotherLatitude() :Double{
        val prefs = requireActivity().getSharedPreferences(
            "helpforce",
            AppCompatActivity.MODE_PRIVATE
        )
        return prefs.getString("CoordLat", "")!!.toDouble()
    }
    private fun getotherLongitude() :Double{
        val prefs = requireActivity().getSharedPreferences(
            "helpforce",
            AppCompatActivity.MODE_PRIVATE
        )
        return prefs.getString("CoordLong", "")!!.toDouble()
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

            if(getother()!!){

                val sharedPrefFile = "helpforce"

                val sharedPreferences: SharedPreferences =
                    requireActivity().getSharedPreferences(
                        sharedPrefFile,
                        Context.MODE_PRIVATE
                    )

                val Dialog : Dialog = Dialog(requireActivity())
                Dialog.setContentView(R.layout.reviewandratedialog)
                val donebtn : Button = Dialog.findViewById(R.id.okbtnrr)
                val reportbtn : Button = Dialog.findViewById(R.id.reportbtn)
                val cancelbtn : Button = Dialog.findViewById(R.id.cancelbtnrr)
                val review : EditText = Dialog.findViewById(R.id.editreview)
                val rBar : RatingBar = Dialog.findViewById(R.id.rating)
                if (Dialog.window != null)
                {
                    Dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                }
                Dialog.show()
                donebtn.setOnClickListener {
                    Log.d("here", getother()!!.toString())
                    val editor: SharedPreferences.Editor = sharedPreferences.edit()
                    editor.putBoolean(
                        "ShowOther", false
                    )
                    editor.apply()
                    Log.d("here", getother()!!.toString())

                    Log.d(
                        "rating",
                        "Bearer ${getToken()}" + " " + getid() + " " + review.text.toString().trim()
                    )
                    SetRating("Bearer ${getToken()}", getid(), rBar.rating.toInt())
                    SetReview("Bearer ${getToken()}", getid(), review.text.toString().trim())
                    if (isHelpRequest()!!)
                    {
                        HelpRequestRespond("Bearer ${getToken()}", getid(), "Completed")
                        editor.putBoolean(
                            "helprequest", false
                        )
                        editor.apply()
                    }
                    else {
                        ServiceRequestRespond("Bearer ${getToken()}", getid(), "Completed")
                    }
                    //review.text.toString().trim()
                    Dialog.dismiss()

                    val f2 = MapFragment()
                    val transaction: FragmentTransaction? = fragmentManager?.beginTransaction()
                    transaction?.replace(R.id.container, f2)
                    transaction?.addToBackStack(null)
                    transaction?.commit()

                }
                cancelbtn.setOnClickListener {
                    Dialog.dismiss()
                }
                reportbtn.setOnClickListener {
                    Dialog.dismiss()
                    val Dialog2 : Dialog = Dialog(requireActivity())
                    Dialog2.setContentView(R.layout.reportdialog)
                    val donebtn : Button = Dialog2.findViewById(R.id.okbtnreport)
                    val cancelbtn : Button = Dialog2.findViewById(R.id.cancelbtnreport)
                    val review : EditText = Dialog2.findViewById(R.id.editreport)
                    if (Dialog2.window != null)
                    {
                        Dialog2.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                    }
                    Dialog2.show()
                    donebtn.setOnClickListener {
                        SetReport("Bearer ${getToken()}", review.text.toString().trim(), getid())
                        Dialog2.dismiss()
                    }
                    cancelbtn.setOnClickListener {
                        Dialog2.dismiss()
                    }
                }

            }

            else {
                val fragment = EmergencyFragment()
                fragmentManager?.beginTransaction()
                    ?.replace(R.id.container, fragment, fragment.javaClass.simpleName)
                    ?.commit()
            }
        }
    }


     var latitude: Double = 0.toDouble()
     var longitude: Double = 0.toDouble()


    var Otherlatitude: Double = 0.toDouble()
    var Otherlongitude: Double = 0.toDouble()

    private lateinit var mLastLocation: Location
    private var mMarker: Marker? = null

    //Location
    lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    lateinit var locationRequest: LocationRequest
    lateinit var locationCallback: LocationCallback

    fun createLocationRequest() {
        val locationRequest = LocationRequest.create()?.apply {
            interval = 10000
            fastestInterval = 5000
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var rootView = inflater.inflate(R.layout.fragment_map, container, false)
        mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(this)
        return rootView
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        //Request Runtime Permission
        @RequiresApi(Build.VERSION_CODES.M)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkLocationPermission()) {
                buildLocationRequest()
                buildLocationCallback();

                fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(
                    requireContext()
                );
                fusedLocationProviderClient.requestLocationUpdates(
                    locationRequest, locationCallback,
                    Looper.myLooper()
                )
            }
        } else {
            buildLocationRequest()
            buildLocationCallback();

            fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(
                requireContext()
            );
            fusedLocationProviderClient.requestLocationUpdates(
                locationRequest, locationCallback,
                Looper.myLooper()
            )
        }







    }
    companion object{
        internal const val MY_PERMISSION_CODE : Int = 1001
        var mapFragment : SupportMapFragment?=null
        val TAG: String = MapFragment::class.java.simpleName
        fun newInstance() = MapFragment()}
    private lateinit var mMap: GoogleMap

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
    super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    when (requestCode) {
        MapFragment.MY_PERMISSION_CODE -> {
            if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (ContextCompat.checkSelfPermission(
                        requireContext(),
                        android.Manifest.permission.ACCESS_FINE_LOCATION
                    ) == PackageManager.PERMISSION_GRANTED
                )
                    if (checkLocationPermission()) {
                        buildLocationRequest()
                        buildLocationCallback();

                        fusedLocationProviderClient =
                            LocationServices.getFusedLocationProviderClient(requireContext());
                        fusedLocationProviderClient.requestLocationUpdates(
                            locationRequest, locationCallback,
                            Looper.myLooper()
                        );

                        mMap!!.isMyLocationEnabled = true

                    }
            } else {
                Toast.makeText(requireContext(), "Permission Denied", Toast.LENGTH_SHORT).show()
            }
        }
    }
}

    private fun checkLocationPermission(): Boolean {
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    requireActivity(),
                    android.Manifest.permission.ACCESS_FINE_LOCATION
                )
            ) {
                ActivityCompat.requestPermissions(
                    requireActivity(), arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
                    MapFragment.MY_PERMISSION_CODE
                )
            } else {
                ActivityCompat.requestPermissions(
                    requireActivity(), arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
                    MapFragment.MY_PERMISSION_CODE
                )
            }
            return false
        } else {
            return true
        }

    }

    private fun buildLocationCallback() {
        locationCallback = object : LocationCallback() {
            override fun onLocationResult(p0: LocationResult?) {
                mLastLocation = p0!!.locations.get(p0!!.locations.size - 1) // Get Last Location

                if (mMarker != null) {
                    mMarker!!.remove()
                }
                latitude = mLastLocation.latitude
                longitude = mLastLocation.longitude

                putMethod(longitude, latitude)

                val sharedPrefFile = "helpforce"

                val sharedPreferences: SharedPreferences =
                    requireActivity().getSharedPreferences(
                        sharedPrefFile,
                        Context.MODE_PRIVATE
                    )
                val editor: SharedPreferences.Editor = sharedPreferences.edit()
                editor.putString("Latitude", latitude.toString());
                editor.putString("Longitude", longitude.toString());
                editor.apply()

                val latlng = LatLng(latitude, longitude)
                val markerOptions = MarkerOptions()
                    .position(latlng)
                    .title("your position")
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_CYAN))
                mMarker = mMap!!.addMarker(markerOptions)

                //MOVE CAMERA
                mMap!!.moveCamera(CameraUpdateFactory.newLatLng(latlng))
                mMap!!.animateCamera(CameraUpdateFactory.zoomTo(11f))

                if (getother()!!) {
                    val otherlocation = LatLng(getotherLatitude(), getotherLongitude())
                    mMap.addMarker(
                        MarkerOptions()
                            .position(otherlocation)
                            .title("other")
                            .icon(
                                BitmapDescriptorFactory
                                    .defaultMarker(BitmapDescriptorFactory.HUE_GREEN)
                            )
                    )
                    Log.d("GoogleMap", "before URL")
                    val URL = getDirectionURL(latlng, otherlocation)
                    Log.d("GoogleMap", "URL : $URL")
                    GetDirection(URL).execute()
                }
            }
        }
    }

    fun getDirectionURL(origin: LatLng, dest: LatLng) : String{
        return "https://maps.googleapis.com/maps/api/directions/json?origin=${origin.latitude},${origin.longitude}&destination=${dest.latitude},${dest.longitude}&sensor=false&mode=driving&key=${getString(
            R.string.google_maps_key
        )}"
    }

    private inner class GetDirection(val url: String) : AsyncTask<Void, Void, List<List<LatLng>>>(){
        override fun doInBackground(vararg params: Void?): List<List<LatLng>> {
            val client = OkHttpClient()
            val request = Request.Builder().url(url).build()
            val response = client.newCall(request).execute()
            val data = response.body()!!.string()
            Log.d("GoogleMap", " data : $data")
            val result =  ArrayList<List<LatLng>>()
            try{
                val respObj = Gson().fromJson(data, GoogleMapDTO::class.java)

                val path =  ArrayList<LatLng>()

                for (i in 0 until respObj.routes[0].legs[0].steps.size){
        /*            val startLatLng = LatLng(respObj.routes[0].legs[0].steps[i].start_location.lat.toDouble()
                        ,respObj.routes[0].legs[0].steps[i].start_location.lng.toDouble())
                    path.add(startLatLng)
                    val endLatLng = LatLng(respObj.routes[0].legs[0].steps[i].end_location.lat.toDouble()
                        ,respObj.routes[0].legs[0].steps[i].end_location.lng.toDouble())
        */            path.addAll(decodePolyline(respObj.routes[0].legs[0].steps[i].polyline.points))
                }
                result.add(path)
            }catch (e: Exception){
                e.printStackTrace()
            }
            return result
        }

        override fun onPostExecute(result: List<List<LatLng>>) {
            val lineoption = PolylineOptions()
            for (i in result.indices){
                lineoption.addAll(result[i])
                lineoption.width(10f)
                lineoption.color(Color.BLUE)
                lineoption.geodesic(true)
            }
            mMap.addPolyline(lineoption)
        }
    }

    public fun decodePolyline(encoded: String): List<LatLng> {

        val poly = ArrayList<LatLng>()
        var index = 0
        val len = encoded.length
        var lat = 0
        var lng = 0

        while (index < len) {
            var b: Int
            var shift = 0
            var result = 0
            do {
                b = encoded[index++].toInt() - 63
                result = result or (b and 0x1f shl shift)
                shift += 5
            } while (b >= 0x20)
            val dlat = if (result and 1 != 0) (result shr 1).inv() else result shr 1
            lat += dlat

            shift = 0
            result = 0
            do {
                b = encoded[index++].toInt() - 63
                result = result or (b and 0x1f shl shift)
                shift += 5
            } while (b >= 0x20)
            val dlng = if (result and 1 != 0) (result shr 1).inv() else result shr 1
            lng += dlng

            val latLng = LatLng((lat.toDouble() / 1E5), (lng.toDouble() / 1E5))
            poly.add(latLng)
        }

        return poly
    }

    private fun buildLocationRequest() {
        locationRequest = LocationRequest()
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        locationRequest.interval = 5000
        locationRequest.fastestInterval = 3000
        locationRequest.smallestDisplacement = 10f
    }




    override fun onStop() {
        fusedLocationProviderClient.removeLocationUpdates(locationCallback)
        super.onStop()
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        //Init Google PLay Services
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(
                    requireContext(),
                    android.Manifest.permission.ACCESS_FINE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                mMap!!.isMyLocationEnabled = true
            }
        }
        else
            mMap!!.isMyLocationEnabled = true

        //Enable ZOOM CONTROL
        mMap.uiSettings.isZoomControlsEnabled = true
    }



}

