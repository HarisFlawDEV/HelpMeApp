package com.unique.helpforce

import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.content.ContentResolver
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.OpenableColumns
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat.checkSelfPermission
import androidx.fragment.app.Fragment
import com.google.gson.GsonBuilder
import com.squareup.picasso.Picasso
import com.unique.helpforce.retrofit.*
import kotlinx.android.synthetic.main.activity_main_screen.*
import kotlinx.android.synthetic.main.fragment_profile.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream


class ProfileFragment : Fragment() {


    fun ShowlistofOwnServices() {
        val rtn =  RetrofitClient.getRetrofitInstance().create(API::class.java)

        CoroutineScope(Dispatchers.IO).launch {
            //    * For @Path: You need to replace the following line with val response = service.getEmployee(53)

            // Do the GET request and get response
            //auth Header of ServiceProvider
            val response = rtn.showlistofOwnServices("Bearer ${getToken()}")
            withContext(Dispatchers.Main) {
                if (response.code() == 200) {
                    val dialog : Dialog = Dialog(requireActivity())
                    dialog.setContentView(R.layout.showservicesdialog)
                    if (dialog.window != null)
                    {
                        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                    }
                    val listView : ListView = dialog.findViewById(R.id.serviceslist)
                    val arrayAdapter: ArrayAdapter<*> =
                        PersonalServiceAdapter(
                            requireContext(),
                            R.layout.itemshowserviceview,
                            response.body()!!.services as ArrayList<Services>
                        )
                    val addServices : Button = dialog.findViewById(R.id.toaddservicesbtn)

                    listView.adapter = arrayAdapter
                    listView.setOnItemClickListener { adapterView, view, which, l ->
                    }

                    addServices.setOnClickListener {
                        ShowlistofServices()
                    }
                    dialog.show()


//                    Toast.makeText(requireContext(), "YAYYY! ${response.body()!![0]._id} , ${response.body()!![1]._id} , ${response.body()!![2]._id}", Toast.LENGTH_SHORT).show()
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


    fun ShowlistofOwnFriends() {
        val rtn =  RetrofitClient.getRetrofitInstance().create(API::class.java)

        CoroutineScope(Dispatchers.IO).launch {
            //    * For @Path: You need to replace the following line with val response = service.getEmployee(53)

            // Do the GET request and get response
            //auth Header of ServiceProvider
            val response = rtn.showlistofOwnFriends("Bearer ${getToken()}")
            withContext(Dispatchers.Main) {
                if (response.code() == 200) {

                    val dialog : Dialog = Dialog(requireActivity())
                    dialog.setContentView(R.layout.showfnfdialog)
                    if (dialog.window != null)
                    {
                        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                    }
                    val listView : ListView = dialog.findViewById(R.id.fnflist)
            val arrayAdapter: ArrayAdapter<*> =
                                  PersonalFNFAdapter(requireContext(), R.layout.itemshowfnfview,
                                      response.body()!!.friends as ArrayList<Friends>
                                  )
                    val addfnf : Button = dialog.findViewById(R.id.toaddfnfbtn)

                            listView.adapter = arrayAdapter
                          listView.setOnItemClickListener { adapterView, view, which, l ->
                        }
                    addfnf.setOnClickListener {

                        val SecondDialog : Dialog = Dialog(requireActivity())
                        SecondDialog.setContentView(R.layout.addfnfdialog)
                        val donebtn : Button = SecondDialog.findViewById(R.id.okbtn)
                        val fnfphone : EditText = SecondDialog.findViewById(R.id.addfnfphone)
                        if (SecondDialog.window != null)
                        {
                            SecondDialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                        }
                        SecondDialog.show()
                        donebtn.setOnClickListener {
                            AddaFriend("Bearer ${getToken()}", fnfphone.text.toString().trim())
                            SecondDialog.dismiss()
                        }
                    }
                    dialog.show()
//                    Toast.makeText(requireContext(), "YAYYY! ${response.body()!![0]._id} , ${response.body()!![1]._id} , ${response.body()!![2]._id}", Toast.LENGTH_SHORT).show()
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
    private var selectedImageUri: Uri? = null

    private fun uploadImage() {
        val parcelFileDescriptor = activity?.contentResolver?.openFileDescriptor(selectedImageUri!!, "r", null) ?: return
        Log.d("here","here1")
        val file = File(activity?.cacheDir , requireActivity().contentResolver.getFileName(selectedImageUri!!))
        Log.d("here","here2")
        val inputStream = FileInputStream(parcelFileDescriptor.fileDescriptor)
        Log.d("here","here3")
        val outputStream = FileOutputStream(file)
        Log.d("here",file.name)
        inputStream.copyTo(outputStream)


        val retIn = RetrofitClient.getRetrofitInstance().create(API::class.java)
        retIn.uploadImage("Bearer ${getToken()}",
            RequestBody.create(MediaType.parse("multipart/form-data"),"name"),
            MultipartBody.Part.createFormData("Photo",file.name)
        ).enqueue(object : Callback<ImageUploadResponse>{
            override fun onResponse(
                call: Call<ImageUploadResponse>,
                response: Response<ImageUploadResponse>
            ) {
                if (response.code() == 200) {
                    Toast.makeText(requireContext(), "YAYYY!", Toast.LENGTH_SHORT).show()
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

            override fun onFailure(call: Call<ImageUploadResponse>, t: Throwable) {
                Toast.makeText(requireContext(), t.message, Toast.LENGTH_SHORT).show()
            }

        })


    }

    private fun ContentResolver.getFileName(fileUri: Uri): String {
        var name = ""
        val returnCursor = this.query(fileUri, null, null, null, null)
        if (returnCursor != null) {
            val nameIndex = returnCursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
            returnCursor.moveToFirst()
            name = returnCursor.getString(nameIndex)
            returnCursor.close()
        }
        return name
    }

    private fun AddaFriend(authHeader: String, name: String)
    {
        val retIn = RetrofitClient.getRetrofitInstance().create(API::class.java)

        retIn.addafriend(authHeader, name).enqueue(object :
            Callback<RegisterServicesResponse> {
            override fun onFailure(call: Call<RegisterServicesResponse>, t: Throwable) {
                Toast.makeText(requireContext(), t.message, Toast.LENGTH_SHORT).show()
            }

            override fun onResponse(
                call: Call<RegisterServicesResponse>,
                response: Response<RegisterServicesResponse>
            ) {
                if (response.code() == 200) {
                    Toast.makeText(requireContext(), "YAYYY!", Toast.LENGTH_SHORT).show()
                } else if (response.code() == 400) //Bad Request
                {
                    Toast.makeText(
                        requireContext(),
                        "Phone Number is not defined",
                        Toast.LENGTH_SHORT
                    ).show()
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


    private fun RegisterPredefinedServices(authHeader: String, name: String)
    {
        val retIn = RetrofitClient.getRetrofitInstance().create(API::class.java)

        retIn.registerPredefinedServices(authHeader, name).enqueue(object :
            Callback<RegisterServicesResponse> {
            override fun onFailure(call: Call<RegisterServicesResponse>, t: Throwable) {
                Toast.makeText(requireContext(), t.message, Toast.LENGTH_SHORT).show()
            }

            override fun onResponse(
                call: Call<RegisterServicesResponse>,
                response: Response<RegisterServicesResponse>
            ) {

                if (response.code() == 200) {
                    Toast.makeText(requireContext(), "YAYYY!", Toast.LENGTH_SHORT).show()
                } else if (response.code() == 400) //Bad Request
                {
                    Toast.makeText(
                        requireContext(),
                        "Service Already registered",
                        Toast.LENGTH_SHORT
                    ).show()
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


    fun ShowlistofServices() {
        val rtn =  RetrofitClient.getRetrofitInstance().create(API::class.java)

        CoroutineScope(Dispatchers.IO).launch {
            //    * For @Path: You need to replace the following line with val response = service.getEmployee(53)

            // Do the GET request and get response
            //auth Header of ServiceProvider
            val response = rtn.showlistofServices("Bearer ${getToken()}")
            withContext(Dispatchers.Main) {
                if (response.code() == 200) {

                    val dialog : Dialog = Dialog(requireActivity())
 //                   val cancelbtntoownservices : Button = dialog.findViewById(R.id.cancelbtn)
                    dialog.setContentView(R.layout.addservicesdialog)
                    if (dialog.window != null)
                    {
                        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                    }
                    val listView :ListView = dialog.findViewById(R.id.allservicelist)
                    val arrayAdapter: ArrayAdapter<*> =
                        AllServiceAdapter(
                            requireContext(), R.layout.servicesitemforaddsevices,
                            response.body()!! as ArrayList<ListOfServices>
                        )
                    listView.adapter = arrayAdapter
                    listView.setOnItemClickListener { adapterView, view, which, l ->
                        RegisterPredefinedServices(
                            "Bearer ${getToken()}",
                            response.body()!![which].name
                        )
                      //  SendRequesttoServiceProvider("Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpZCI6IjVmYzdmZmJiNjhiNjU5MDAxNzgzZGQ2OCIsInBob25lIjoiMDMzNTIwMzU1MTgiLCJpYXQiOjE2MDg2NDQwMDAsImV4cCI6MTYwODgxNjgwMH0.J1LP5KCMaLe2bY0ZB2o9d_KVbiO53MH27A384_F3HcI",response.body()!![which]._id,service)
                        dialog.dismiss()
                    }
                    dialog.show()
/*
                    cancelbtntoownservices.setOnClickListener {
                        dialog.dismiss()
                    }
*/

//                    Toast.makeText(requireContext(), "YAYYY! ${response.body()!![0]._id} , ${response.body()!![1]._id} , ${response.body()!![2]._id}", Toast.LENGTH_SHORT).show()
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




    @SuppressLint("SetTextI18n")

    private fun getToken() :String{
        val prefs = requireActivity().getSharedPreferences("helpforce", AppCompatActivity.MODE_PRIVATE)
        return prefs.getString("token","")!!
    }

    private fun getuserid() :String{
        val prefs = requireActivity().getSharedPreferences("helpforce", AppCompatActivity.MODE_PRIVATE)
        return prefs.getString("userid","")!!
    }
    @SuppressLint("SetTextI18n")
    fun GetProfile() {

        val rtn =  RetrofitClient.getRetrofitInstance().create(API::class.java)

        CoroutineScope(Dispatchers.IO).launch {
         //    * For @Path: You need to replace the following line with val response = service.getEmployee(53)
            // Do the GET request and get response
            val response = rtn.getProfile("Bearer ${getToken()}",getuserid())

            withContext(Dispatchers.Main) {
                if (response.code() == 200) {
                    profile_name.text = "${response.body()!!.firstName} ${response.body()!!.lastName}"
                    profile_email.text = response.body()!!.email
                    profile_phone.text = response.body()!!.phone
                    profile_dob.text = response.body()!!.dob
                    profile_gender.text = response.body()!!.gender
                    profile_phone.text = response.body()!!.phone
                    val pic = view?.findViewById(R.id.profile_img) as ImageView
//                    val imgName = response.body()!!.image.split("/").toTypedArray()

                    Log.d("img", response.body()!!.image)
                    Picasso.with(requireContext())
                        .load("https://helpme-restapi.herokuapp.com/public/uploads/images/${response.body()!!.image}")
                        .into(pic);


                    Toast.makeText(
                        requireContext(),
                        "YAYYY! ${response.body()!!.firstName.toString()}",
                        Toast.LENGTH_SHORT
                    ).show()
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

/*

    private fun GetProfile(authHeader : String,userId :String)
    {
        val retIn = RetrofitClient.getRetrofitInstance().create(API::class.java)

        retIn.getProfile(authHeader,userId).enqueue(object :
            Callback<GetProfileResponse> {
            override fun onFailure(call: Call<GetProfileResponse>, t: Throwable) {
                Toast.makeText(requireContext(), t.message, Toast.LENGTH_SHORT).show()
            }

            override fun onResponse(call: Call<GetProfileResponse>, response: Response<GetProfileResponse>) {
                if (response.code() == 200) {
                    Toast.makeText(requireContext(), "YAYYY! ${response.body().toString()}", Toast.LENGTH_SHORT).show()
                }
                else {
                    Toast.makeText(requireContext(),"MASLA ${response.code()} , ${response.message()}, ${response.errorBody().toString()}",Toast.LENGTH_LONG).show()
                }
            }
        }
        )
    }
*/






    companion object{
        private val IMAGE_PICK_CODE = 1000;
        private val PERMISSION_CODE = 1001;
    }

    private var PersonalServicesList : ArrayList<PersonalServices> = ArrayList()
    private var PersonalFNFList : ArrayList<PersonalFNF> = ArrayList()

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
            //ShowlistofServiceNeededUsers()

            val fragment = EmergencyFragment()
            fragmentManager?.beginTransaction()
                ?.replace(R.id.container, fragment, fragment.javaClass.simpleName)
                ?.commit()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val v = inflater.inflate(R.layout.fragment_profile, container, false)
        GetProfile()
    return v
    }
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)




        PersonalServicesList = setPersonalServices()

        profile_img.setOnClickListener {

            Toast.makeText(requireContext(),"ok",Toast.LENGTH_SHORT).show()
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
            {
                if (checkSelfPermission(
                        requireContext(),
                        android.Manifest.permission.READ_EXTERNAL_STORAGE
                    ) == PackageManager.PERMISSION_DENIED)
                {
                    //Permission Dennied
                    val permissions = arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE)
                    //show popup to request runtime permission
                    requestPermissions(permissions, PERMISSION_CODE)
                }
                else{
                    //PERMISSIONS ALREADY GRANTED
                    openGalleryForImage()
                }
            }
            else{
                //SYSTEM OS IS < MARSHMALLOW
                openGalleryForImage()
            }
        }




        profile_editprofile.setOnClickListener {
            val Dialog : Dialog = Dialog(requireActivity())
           Dialog.setContentView(R.layout.editprofiledialog)
            val donebtn : Button = Dialog.findViewById(R.id.okbtn)
            val cancelbtn : Button = Dialog.findViewById(R.id.cancelbtn)
            val Name : EditText = Dialog.findViewById(R.id.editname)
            val Phone : EditText = Dialog.findViewById(R.id.editphone)
            val Email : EditText = Dialog.findViewById(R.id.editemail)
            val dob : TextView = Dialog.findViewById(R.id.editdob)
            val gender : TextView = Dialog.findViewById(R.id.editgender)
            val imgpic : ImageView = Dialog.findViewById(R.id.editpic)

//                val PersonalServiceName : String
            if (Dialog.window != null)
            {
                Dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            }
            Dialog.show()
            imgpic.setOnClickListener {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
                {
                    if (checkSelfPermission(
                            requireContext(),
                            android.Manifest.permission.READ_EXTERNAL_STORAGE
                        ) == PackageManager.PERMISSION_DENIED)
                    {
                        //Permission Dennied
                        val permissions = arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE)
                        //show popup to request runtime permission
                        requestPermissions(permissions, PERMISSION_CODE)
                    }
                    else{
                        //PERMISSIONS ALREADY GRANTED
                        openGalleryForImage()

                    }
                }
                else{
                    //SYSTEM OS IS < MARSHMALLOW
                    openGalleryForImage()
                }
            }

            donebtn.setOnClickListener {
                val SecondDialog : Dialog = Dialog(requireActivity())
                SecondDialog.setContentView(R.layout.editprofilecheckpassdialog)
                val donebtn : Button = SecondDialog.findViewById(R.id.okbtn)
                val cancelbtn : Button = SecondDialog.findViewById(R.id.cancelbtn)
                val Password : EditText = SecondDialog.findViewById(R.id.editnpass)
                if (SecondDialog.window != null)
                {
                    SecondDialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                }
                SecondDialog.show()
                donebtn.setOnClickListener {
//                    PersonalServicesList.add(PersonalServices(ServiceName.text.toString().trim()))
                    SecondDialog.dismiss()
                }
                cancelbtn.setOnClickListener {
                    SecondDialog.dismiss()
                }
            }
            cancelbtn.setOnClickListener {
                Dialog.dismiss()
            }


        }

        profile_Services.setOnClickListener {
            ShowlistofOwnServices()
        }
        profile_fnf.setOnClickListener {
           ShowlistofOwnFriends()
        }


        profile_acclogoutbtn.setOnClickListener {
            val builder = AlertDialog.Builder(requireContext())
            builder.setMessage("Are you sure you want to Logout ?")
                .setCancelable(false)
                .setPositiveButton("Yes") { dialog, id ->
                    val intent = Intent (activity, MainActivity::class.java)
                    activity?.startActivity(intent)
                    // Delete selected note from database
                }
                .setNegativeButton("No") { dialog, id ->
                    // Dismiss the dialog
                    dialog.dismiss()
                }
            val alert = builder.create()
            alert.show()

        }


        profile_accdel.setOnClickListener{

                val builder = AlertDialog.Builder(requireContext())
                builder.setMessage("Are you sure you want to Delete?")
                    .setCancelable(false)
                    .setPositiveButton("Yes") { dialog, id ->
                        val intent = Intent (activity, MainActivity::class.java)
                        activity?.startActivity(intent)
                        // Delete selected note from database
                    }
                    .setNegativeButton("No") { dialog, id ->
                        // Dismiss the dialog
                        dialog.dismiss()
                    }
                val alert = builder.create()
                alert.show()
            }

        }

    fun setPersonalServices() : ArrayList<PersonalServices>
    {
        val arrayList: ArrayList<PersonalServices> = ArrayList()

        arrayList.add(PersonalServices("Laundry"))
        arrayList.add(PersonalServices("Laundry"))
        arrayList.add(PersonalServices("Laundry"))
        arrayList.add(PersonalServices("Laundry"))
        arrayList.add(PersonalServices("Laundry"))
        arrayList.add(PersonalServices("Laundry"))
        return arrayList


    }

    fun setFNFServices() : ArrayList<PersonalFNF>
    {
        val arrayList: ArrayList<PersonalFNF> = ArrayList()

        arrayList.add(PersonalFNF("NOBITA", "03332121223", R.drawable.account_icon))
        arrayList.add(PersonalFNF("NOBITA", "03332121223", R.drawable.account_icon))
        arrayList.add(PersonalFNF("NOBITA", "03332121223", R.drawable.account_icon))
        arrayList.add(PersonalFNF("NOBITA", "03332121223", R.drawable.account_icon))
        arrayList.add(PersonalFNF("NOBITA", "03332121223", R.drawable.account_icon))
        arrayList.add(PersonalFNF("NOBITA", "03332121223", R.drawable.account_icon))
        arrayList.add(PersonalFNF("NOBITA", "03332121223", R.drawable.account_icon))
        return arrayList
    }
    private fun openGalleryForImage() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, IMAGE_PICK_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == IMAGE_PICK_CODE){
            selectedImageUri = data?.data
           profile_img.setImageURI(selectedImageUri)
            uploadImage()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when(requestCode){
            PERMISSION_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //PERMISSION FROM POPUP GRANTED
                    openGalleryForImage()
                } else {
                    //PERMISSION FROM POPUP DENIED
                    Toast.makeText(requireContext(), "Permission Denied ", Toast.LENGTH_SHORT)
                        .show()
                }
            }
        }
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }
}
