package com.example.transactionapp

import DBHandler
import android.content.Intent
import android.graphics.Rect
import android.os.Bundle
import android.view.MenuItem
import android.view.MotionEvent
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.transactionapp.databinding.ActivityDataAdderBinding


class dataAdder : AppCompatActivity() {
    lateinit var dataAdderBinding: ActivityDataAdderBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.Theme_TransactionApp)
        dataAdderBinding=ActivityDataAdderBinding.inflate(layoutInflater)
        setContentView(dataAdderBinding.root)
        init()
        val ActBar= supportActionBar
        ActBar?.setDisplayHomeAsUpEnabled(true)
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

    fun init(){
        dataAdderBinding.buttonAdd.setOnClickListener{
            val descData=dataAdderBinding.editTextDescChild.text.toString()
            val amountData=dataAdderBinding.editTextAmountChild.text.toString()
            if(descData.isEmpty() || amountData.isEmpty()){
                if(descData.isEmpty() && amountData.isEmpty()){
                    dataAdderBinding.editTextDescChild.setError("Enter Detail")
                    dataAdderBinding.editTextDescChild.requestFocus()
                    dataAdderBinding.editTextAmountChild.setError("Enter Amount")
                    dataAdderBinding.editTextAmountChild.requestFocus()
                }
                else if(descData.isEmpty()){
                    dataAdderBinding.editTextDescChild.setError("Enter Detail")
                    dataAdderBinding.editTextDescChild.requestFocus()
                }
                else{
                    dataAdderBinding.editTextAmountChild.setError("Enter Amount")
                    dataAdderBinding.editTextAmountChild.requestFocus()
                }
            }
            else{
                val temp=intent.getStringExtra("FROM_WHERE")
                if(temp.equals("DEBIT")) {
                    if(!(amountData.toLong()<=DBHandler(this).calculateTotal())) {
                        Toast.makeText(this, " Not Enough Amount In Account ", Toast.LENGTH_SHORT).show()

                        return@setOnClickListener
                    }
                    val sharPref=getSharedPreferences("TransactionAppPreferences", MODE_PRIVATE)
                    val result=DBHandler(this).addDebit(sharPref.getString("email","emailNotgetted").toString(),descData,amountData)
                    if(result.toString().equals("-1")){
                        Toast.makeText(this, " NotAdded " + " email= " + sharPref.getString("email","N/A"), Toast.LENGTH_SHORT).show()
                    }
                    Toast.makeText(this, " Added " + " email= " + sharPref.getString("email","N/A"), Toast.LENGTH_SHORT).show()
                }
                else{
                    val sharPref=getSharedPreferences("TransactionAppPreferences", MODE_PRIVATE)
                    val result=DBHandler(this).addCredit(sharPref.getString("email","emailNotgetted").toString(),descData,amountData)
                    if(result.toString().equals("-1")){
                        Toast.makeText(this, " NotAdded " + " email= " + sharPref.getString("email","N/A"), Toast.LENGTH_SHORT).show()
                    }
                    Toast.makeText(this, " Added " + " email= " + sharPref.getString("email","N/A"), Toast.LENGTH_SHORT).show()
                }
                startActivity(Intent(this,MainActivity::class.java))
                finish()
            }
        }
    }

    // Keyboard hides when touched outside editText
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