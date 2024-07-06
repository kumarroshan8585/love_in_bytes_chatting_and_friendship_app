package com.example.datingapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Gravity
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import com.example.datingapp.activity.GeminiActivity
import com.example.datingapp.databinding.ActivityMainBinding
import com.google.android.material.navigation.NavigationView
import com.google.android.material.navigation.NavigationView.OnNavigationItemSelectedListener

class MainActivity : AppCompatActivity(), OnNavigationItemSelectedListener {
    private lateinit var binding: ActivityMainBinding
    var actionBarDrawerToggle: ActionBarDrawerToggle?=null //setting variable for action bar (3 dande vala)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding=ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        actionBarDrawerToggle= ActionBarDrawerToggle(this, binding.drawerLayout, R.string.open, R.string.close)
        //. When you toggle the drawer (open or close it),
        // the ActionBarDrawerToggle will update the action bar
        // (typically changing the hamburger icon to an arrow and vice versa) and provide appropriate accessibility descriptions.

        binding.drawerLayout.addDrawerListener(actionBarDrawerToggle!!)  //Attaches the ActionBarDrawerToggle as a listener to the DrawerLayout to handle drawer state changes.
        actionBarDrawerToggle!!.syncState()  //Ensures that the visual state of the drawer indicator is synchronized with the current state of the DrawerLayout.

        supportActionBar?.setDisplayHomeAsUpEnabled(true)  //This line enables the display of the up button (commonly represented as a back arrow) in the action bar,
        // allowing users to navigate back to the parent activity or close the current activity when the navigation drawer is open.


        binding.navigationView.setNavigationItemSelectedListener(this)  //Nav bar mai kisi button ke click hone ka alarm dega

        val navController=findNavController(R.id.fragment)

        NavigationUI.setupWithNavController(binding.bottomNavigationView, navController)  //It sets up the Bottom Navigation View to automatically handle navigation
    // actions based on the destination changes managed by the NavController.
    // This means that selecting items in the Bottom Navigation View will trigger navigation events to navigate between
    // different destinations within your app's navigation graph.
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.gemini->{
                val intent = Intent(this, GeminiActivity::class.java)
                startActivity(intent)
            }
            R.id.rateUs->{
                Toast.makeText(this, "Rate Us!", Toast.LENGTH_SHORT).show()
            }
            R.id.shareApp->{
                Toast.makeText(this, "Share this app!", Toast.LENGTH_SHORT).show()
            }
            R.id.favourite->{
                Toast.makeText(this, "favourite!", Toast.LENGTH_SHORT).show()
            }
            R.id.privacyPolicy->{
                Toast.makeText(this, "Privacy Policy!", Toast.LENGTH_SHORT).show()
            }
            R.id.termsCondition->{
                Toast.makeText(this, "Terms and conditions!", Toast.LENGTH_SHORT).show()
            }
            R.id.developer->{
                Toast.makeText(this, "Developer!", Toast.LENGTH_SHORT).show()
            }
        }
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {  //handles options menu item selections, If the ActionBarDrawerToggle handles the selection (typically for the navigation drawer toggle button), it returns true indicating that the selection has been handled.
        return if(actionBarDrawerToggle!!.onOptionsItemSelected(item)){
            true
        }
        else{
            super.onOptionsItemSelected(item)
        }

    }

    override fun onBackPressed() {  //Back dabane par application close

        if(binding.drawerLayout.isDrawerOpen(GravityCompat.START)){
            binding.drawerLayout.close()
        }else{
            super.onBackPressed()
        }
    }
}

