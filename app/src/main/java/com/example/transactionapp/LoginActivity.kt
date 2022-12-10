package com.example.transactionapp

import DBHandler
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Rect
import android.os.Bundle
import android.view.MotionEvent
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.transactionapp.databinding.ActivityLoginBinding


class LoginActivity : AppCompatActivity() {
    lateinit var ActLogBin: ActivityLoginBinding

//    override fun onStart() {
//        super.onStart()
//        val dataCheck: String? =intent.getStringExtra("dataCheck")
//        if(dataCheck!=null)
//        {
//            // intent to home page
//        }else{
//            // dont doanytihng
//        }
//    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        checkLoggedInOrNot()
        setTheme(R.style.Theme_TransactionApp)
        ActLogBin=ActivityLoginBinding.inflate(layoutInflater)
        setContentView(ActLogBin.root)
        startWork()
    }

    private fun checkLoggedInOrNot() {
        val sharPref: SharedPreferences = getSharedPreferences("TransactionAppPreferences",
            MODE_PRIVATE)
        if(sharPref.getInt("log",0)==1){
            val intent = Intent(this, MainActivity::class.java)
            finish()
            startActivity(intent)
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

    private fun startWork(){
        lateinit var editTextEmailLogin: String
        lateinit var editTextPasswordLogin: String
        val DBHandler=DBHandler(this)
        ActLogBin.buttonLogin.setOnClickListener{
            editTextEmailLogin=ActLogBin.editTextEmailLoginChild.text.toString()
            editTextPasswordLogin=ActLogBin.editTextPasswordLoginChild.text.toString()
            if(editTextEmailLogin.isEmpty() || editTextPasswordLogin.isEmpty()) {
                if(editTextPasswordLogin.isEmpty()){
                    ActLogBin.editTextPasswordLoginChild.setError("Enter Password")
                    ActLogBin.editTextPasswordLoginChild.requestFocus()
                }else{
                    ActLogBin.editTextEmailLoginChild.setError("Enter Email")
                    ActLogBin.editTextEmailLoginChild.requestFocus()
                }
                Toast.makeText(this, "Enter Credentials", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            else {
                var tempString: String=DBHandler.checkUserExistance(editTextEmailLogin,editTextPasswordLogin)
                if(tempString.equals("NO_TABLE") || tempString.equals("NOT_EXIST") || tempString.equals("WRONG_PASSWORD")){
                    if(tempString.equals("WRONG_PASSWORD"))
                        Toast.makeText(this, "Wrong Password", Toast.LENGTH_SHORT).show()
                    else
                        Toast.makeText(this, "This Email is not registered", Toast.LENGTH_SHORT).show()
                }
                else if(tempString.equals("EXIST")){
                    val sharPref: SharedPreferences = getSharedPreferences("TransactionAppPreferences",
                        MODE_PRIVATE)
                    val sharPrefEdit: SharedPreferences.Editor = sharPref.edit()
                    sharPrefEdit.putInt("log",1)
                    sharPrefEdit.putString("email",editTextEmailLogin)
                    sharPrefEdit.apply()
                    val intent = Intent(this, MainActivity::class.java)
                    finish()
                    startActivity(intent)
                }
            }
        }
        ActLogBin.textDontHaveAccount.setOnClickListener{
                val intent = Intent(this, SignupActivity::class.java)
                finish()
                startActivity(intent)
        }
    }
}