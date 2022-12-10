package com.example.transactionapp

import DBHandler
import android.content.Intent
import android.content.SharedPreferences
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.graphics.Color
import android.graphics.Rect
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.view.MenuItem
import android.view.MotionEvent
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.Toast
import androidx.core.view.isVisible
import com.example.transactionapp.databinding.ActivityAccountBinding

class accountActivity : AppCompatActivity() {

    lateinit var AccouActBin: ActivityAccountBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.Theme_TransactionApp)
        AccouActBin = ActivityAccountBinding.inflate(layoutInflater)
        setContentView(AccouActBin.root)
        val ActBar = supportActionBar
        ActBar?.setDisplayHomeAsUpEnabled(true)
        work()
    }

    fun work() {

        val sharPref = this.getSharedPreferences(
            "TransactionAppPreferences",
            MODE_PRIVATE
        )
        var email = sharPref?.getString("email", "emailNotGetted").toString()
        val db = DBHandler(this).readableDatabase
        val cur = db.rawQuery("SELECT name,password FROM UserInfo WHERE email=" + "'$email';", null)
        if (cur.moveToFirst()) {
            playingWithEditText(cur, email)
        } else
            Toast.makeText(this, "Looks like something went wrong!", Toast.LENGTH_SHORT).show()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                finish()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    fun playingWithEditText(cur: Cursor, email: String) {
        AccouActBin.editTextNameAccountSetting.hint = cur.getString(0)
        AccouActBin.editTextEmailAccountSetting.hint = email
        AccouActBin.editTextPasswordAccountSetting.hint = cur.getString(1)
        AccouActBin.editButton.setOnClickListener {
            AccouActBin.editTextNameAccountSetting.hint = "Enter Name"
            AccouActBin.editTextEmailAccountSetting.hint = "Enter Email"
            AccouActBin.editTextPasswordAccountSetting.hint = "Enter Password"

            AccouActBin.editTextNameAccountSettingChild.isClickable = true
            AccouActBin.editTextNameAccountSettingChild.isFocusable = true
            AccouActBin.editTextNameAccountSettingChild.isFocusableInTouchMode = true
            AccouActBin.editTextNameAccountSettingChild.isCursorVisible = true

//            AccouActBin.editTextEmailAccountSettingChild.isClickable = true
//            AccouActBin.editTextEmailAccountSettingChild.isFocusable = true
//            AccouActBin.editTextEmailAccountSettingChild.isFocusableInTouchMode = true
//            AccouActBin.editTextEmailAccountSettingChild.isCursorVisible = true

            AccouActBin.editTextPasswordAccountSettingChild.isClickable = true
            AccouActBin.editTextPasswordAccountSettingChild.isFocusable = true
            AccouActBin.editTextPasswordAccountSettingChild.isFocusableInTouchMode = true
            AccouActBin.editTextPasswordAccountSettingChild.isCursorVisible = true

            AccouActBin.editTextNameAccountSettingChild.setText(cur.getString(0))
            AccouActBin.editTextEmailAccountSettingChild.setText(email)
            AccouActBin.editTextPasswordAccountSettingChild.setText(cur.getString(1))

            AccouActBin.editTextNameAccountSettingChild.requestFocus()
            AccouActBin.saveButton.setBackgroundColor(Color.parseColor("#008000"))
            AccouActBin.saveButton.isVisible = true
            AccouActBin.editButton.isVisible = false
        }

        AccouActBin.saveButton.setOnClickListener{
           val name= AccouActBin.editTextNameAccountSettingChild.text.toString()
//           val email2= AccouActBin.editTextEmailAccountSettingChild.text.toString()
           val password=AccouActBin.editTextPasswordAccountSettingChild.text.toString()
            EvaluatingUserData(name, email, password)
        }
    }

    fun EvaluatingUserData(name: String, email: String, password: String){

        if(name.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Enter Credentials", Toast.LENGTH_SHORT).show()
            return
        }
        else {
//            if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
//                AccouActBin.editTextEmailAccountSettingChild.setError("Invalid Email")
//                AccouActBin.editTextEmailAccountSettingChild.requestFocus()
//                return
//            }
            val DBHandler=DBHandler(this)
            val retTempString: String=DBHandler.updateData(name,email,password)
            if(retTempString.equals("UPDATED")){
                Toast.makeText(this, "Data Updated", Toast.LENGTH_SHORT).show()
                val sharPref: SharedPreferences = getSharedPreferences("TransactionAppPreferences",
                    MODE_PRIVATE)
                val sharPrefEdit: SharedPreferences.Editor = sharPref.edit()
                sharPrefEdit.putInt("log",1)
                sharPrefEdit.putString("email",email)
                sharPrefEdit.apply()
            }
            else if(retTempString.equals("EXIST"))
                Toast.makeText(this, "This Email is registered", Toast.LENGTH_SHORT).show()
        }
    }

    override fun dispatchTouchEvent(event: MotionEvent): Boolean {
        if (event.action == MotionEvent.ACTION_DOWN) {
            val v = currentFocus
            if (v is EditText) {
                val outRect = Rect()
                v.getGlobalVisibleRect(outRect)
                if (!outRect.contains(event.rawX.toInt(), event.rawY.toInt())) {
                    v.clearFocus()
                    val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0)
                }
            }
        }
        return super.dispatchTouchEvent(event)
    }
}