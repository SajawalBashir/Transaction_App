package com.example.transactionapp

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.transactionapp.databinding.ActivityMainBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView


class MainActivity : AppCompatActivity() , NavigationView.OnNavigationItemSelectedListener{
    lateinit var ActMainBin: ActivityMainBinding
    lateinit var DraLay: DrawerLayout
    lateinit var ActBarDraTogg: ActionBarDrawerToggle
    lateinit var NavView: NavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.Theme_TransactionApp)
        ActMainBin=ActivityMainBinding.inflate(layoutInflater)
        setContentView(ActMainBin.root)

        // Bottom navigation setup
        // The Drawer icon removes from ActionBar so line 34,35 were commented
        val botNavView=findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        val navCont=findNavController(R.id.fragmentActivityMain)
        //val appBarConfig= AppBarConfiguration(setOf(R.id.debitFragment,R.id.totalAmountFragment,R.id.creditFragment))
        //setupActionBarWithNavController(navCont,appBarConfig)
        botNavView.setupWithNavController(navCont)
        NavView=findViewById(R.id.nav_view)
        NavView.setNavigationItemSelectedListener(this)

        navCont.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.debitFragment -> {
                    title="Debit"
                }
                R.id.totalAmountFragment -> {
                    title="Total"
                }
                R.id.creditFragment -> {
                    title="Credit"
                }
            }
        }

        // Drawer setup
        DraLay=findViewById(R.id.my_drawer_layout)
        ActBarDraTogg=ActionBarDrawerToggle(this, DraLay,R.string.nav_open,R.string.nav_close)
        DraLay.addDrawerListener(ActBarDraTogg)
        ActBarDraTogg.syncState()
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.fragments_menu,menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(R.id.logoutButton==item.itemId){
            val sharPref: SharedPreferences = getSharedPreferences("TransactionAppPreferences",
                MODE_PRIVATE)
            val sharPrefEdit: SharedPreferences.Editor = sharPref.edit()
            sharPrefEdit.putInt("log",0)
            sharPrefEdit.apply()
            val intent = Intent(this, LoginActivity::class.java)
            finish()
            startActivity(intent)
        }
        return if (ActBarDraTogg.onOptionsItemSelected(item)) {
            true
        } else super.onOptionsItemSelected(item)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_account -> {
                startActivity(Intent(this,accountActivity::class.java))
                //finish()
            }
            R.id.nav_logout -> {
                val sharPref: SharedPreferences = getSharedPreferences("TransactionAppPreferences",
                    MODE_PRIVATE)
                val sharPrefEdit: SharedPreferences.Editor = sharPref.edit()
                sharPrefEdit.putInt("log",0)
                sharPrefEdit.apply()
                val intent = Intent(this, LoginActivity::class.java)
                finish()
                startActivity(intent)
            }
        }
        //drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }
}