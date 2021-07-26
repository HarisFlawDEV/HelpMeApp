package com.unique.helpforce
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.children
import com.google.android.gms.location.*
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.activity_main_screen.*

class MainScreenActivity : AppCompatActivity()  {

    private fun getother() :Boolean?{
        val prefs = getSharedPreferences("helpforce", AppCompatActivity.MODE_PRIVATE)
        return prefs?.getBoolean("ShowOther",false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_screen)


        bottomNavigationView.background = null
        bottomNavigationView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)

        if (savedInstanceState == null && !getother()!!) {

            val fragment = MapFragment()
                supportFragmentManager.beginTransaction().replace(
                R.id.container,
                fragment,
                fragment.javaClass.simpleName
            )
                .commit()
        }

        fab.setOnClickListener {


            val fragment = EmergencyFragment()
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, fragment, fragment.javaClass.simpleName)
                .commit()



        //            getToken()
        //            Toast.makeText(this, "${longitude} , ${latitude}", Toast.LENGTH_LONG).show()
            //     putMethod()
        }

   }

    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { menuItem ->
        when (menuItem.itemId) {
            R.id.menu_map -> {
                if (!getother()!!){
                    val fragment = MapFragment()
                    supportFragmentManager
                        .beginTransaction()
                        .replace(R.id.container, fragment, fragment.javaClass.simpleName)
                        .commit()
                    return@OnNavigationItemSelectedListener true
                }
            }
            R.id.menu_services -> {
                if (!getother()!!){
                    val fragment = ServiceFragment()
                supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.container,fragment,fragment.javaClass.simpleName)
                    .commit()
                return@OnNavigationItemSelectedListener true
                }
            }
            R.id.menu_notif -> {
                if (!getother()!!) {
                    val fragment = NotificationFragment()
                    supportFragmentManager
                        .beginTransaction()
                        .replace(R.id.container, fragment, fragment.javaClass.simpleName)
                        .commit()
                    return@OnNavigationItemSelectedListener true
                }
            }
            R.id.menu_acc -> {
                if (!getother()!!){
                    val fragment = ProfileFragment()
                supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.container,fragment,fragment.javaClass.simpleName)
                    .commit()
                return@OnNavigationItemSelectedListener true
                }
            }
        }
        false
    }

    private fun getToken() :String{
        val prefs = this.getSharedPreferences("helpforce", MODE_PRIVATE)
        return prefs.getString("token","")!!
    }
}






