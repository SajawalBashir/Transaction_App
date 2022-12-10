package com.example.transactionapp

import DBHandler
import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import com.example.transactionapp.databinding.FragmentTotalAmountBinding

class totalAmountFragment : Fragment() {
    lateinit var TotAmoFrag: FragmentTotalAmountBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        TotAmoFrag=FragmentTotalAmountBinding.inflate(layoutInflater,container,false)
        return TotAmoFrag.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val total=DBHandler(context).calculateTotal()
        val text= "$$total"
        TotAmoFrag.textAmount.text = text
    }

//    fun calculateTotal(): Long{
//        val db= DBHandler(context).readableDatabase
//        val sharPref=context?.getSharedPreferences("TransactionAppPreferences",
//            AppCompatActivity.MODE_PRIVATE
//        )
//        var TABLE_NAME=sharPref?.getString("email","emailNotGetted").toString()
//        TABLE_NAME=DBHandler(context).removeSpecialChar(TABLE_NAME)
//        var debitAmount=totalPriceFromDebit(TABLE_NAME,db)
//        var creditAmount=totalPriceFromCredit(TABLE_NAME,db)
//
//        return creditAmount-debitAmount
//    }
//
//    fun totalPriceFromDebit(tableName: String,db: SQLiteDatabase): Long{
//        var totalAmount: Long=0
//        val TABLE_NAME=tableName+"DebitTable"
//        val cur=db.rawQuery("SELECT amount FROM " + TABLE_NAME + ";" ,null)
//        if(cur.moveToFirst()){
//            totalAmount+=cur.getString(0).toLong()
//            //Log.d("jjj"," line 44 " + cur.getString(0))
//            while(cur.moveToNext()){
//                totalAmount+=cur.getString(0).toLong()
//                //Log.d("jjj"," line 47 " + cur.getString(0))
//            }
//        }
//        else
//            return 0
//        //Log.d("jjj"," line 47 " + totalAmount)
//        return totalAmount
//    }
//
//    fun totalPriceFromCredit(tableName: String,db: SQLiteDatabase): Long{
//        var totalAmount: Long=0
//        val TABLE_NAME=tableName+"CreditTable"
//        val cur=db.rawQuery("SELECT amount FROM " + TABLE_NAME + ";" ,null)
//        if(cur.moveToFirst()){
//            totalAmount+=cur.getString(0).toLong()
//            //Log.d("jjj"," line 44 " + cur.getString(0))
//            while(cur.moveToNext()){
//                totalAmount+=cur.getString(0).toLong()
//                //Log.d("jjj"," line 47 " + cur.getString(0))
//            }
//        }
//        else
//            return 0
//        //Log.d("jjj"," line 47 " + totalAmount)
//        return totalAmount
//    }

}