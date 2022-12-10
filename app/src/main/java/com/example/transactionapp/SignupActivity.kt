package com.example.transactionapp

import DBHandler
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Rect
import android.os.Bundle
import android.util.Patterns
import android.view.MotionEvent
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.transactionapp.databinding.ActivitySignupBinding


class SignupActivity : AppCompatActivity() {
    lateinit var ActSignBin: ActivitySignupBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //this.deleteDatabase("TransactionDb")
        setTheme(R.style.Theme_TransactionApp)
        ActSignBin=ActivitySignupBinding.inflate(layoutInflater)
        setContentView(ActSignBin.root)
        startWork()
    }

    private fun startWork(){
        ActSignBin.imgBackSignup.setOnClickListener{
            val intent = Intent(this, LoginActivity::class.java)
            finish()
            startActivity(intent)
        }
        ActSignBin.buttonSignup.setOnClickListener{
            val editTextNameSignup=ActSignBin.editTextNameSignUpChild.text.toString()
            val editTextEmailSignup=ActSignBin.editTextEmailSignupChild.text.toString()
            val editTextPasswordSignup=ActSignBin.editTextPasswordSignupChild.text.toString()
            EvaluatingUserData(editTextNameSignup,editTextEmailSignup,editTextPasswordSignup)
        }

        ActSignBin.buttonGoogleSignup.setOnClickListener{
            Toast.makeText(this, "Not Available Yet", Toast.LENGTH_SHORT).show()
        }
    }

    fun EvaluatingUserData(name: String, email: String, password: String){

        if(name.isEmpty() || email.isEmpty()|| password.isEmpty()) {
            Toast.makeText(this, "Enter Credentials", Toast.LENGTH_SHORT).show()
            return
        }
        else {
            if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                ActSignBin.editTextEmailSignupChild.setError("Invalid Email")
                ActSignBin.editTextEmailSignupChild.requestFocus()
                return
            }
            val DBHandler=DBHandler(this)
            val retTempString: String=DBHandler.addNewUser(name,email,password)
            if(retTempString.equals("ADDED")){
                Toast.makeText(this, "User Added", Toast.LENGTH_SHORT).show()
                val sharPref: SharedPreferences = getSharedPreferences("TransactionAppPreferences",
                    MODE_PRIVATE)
                val sharPrefEdit: SharedPreferences.Editor = sharPref.edit()
                sharPrefEdit.putInt("log",1)
                sharPrefEdit.putString("email",email)
                sharPrefEdit.apply()
                val intent = Intent(this, MainActivity::class.java)
                finish()
                startActivity(intent)
            }
            else if(retTempString.equals("EXIST"))
                Toast.makeText(this, "Email is already registered", Toast.LENGTH_SHORT).show()
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