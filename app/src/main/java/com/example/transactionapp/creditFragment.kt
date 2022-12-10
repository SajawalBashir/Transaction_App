package com.example.transactionapp

import DBHandler
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.transactionapp.databinding.FragmentCreditBinding
import com.example.transactionapp.databinding.FragmentDebitBinding
import java.security.AccessController.getContext


class creditFragment : Fragment() {
    lateinit var ArrLis: ArrayList<dataModel>
    lateinit var binding: FragmentCreditBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding=FragmentCreditBinding.inflate(layoutInflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.creditButton.setOnClickListener{
            val intent= Intent(context,dataAdder::class.java)
            intent.putExtra("FROM_WHERE","CREDIT")
            startActivity(intent)
            //activity?.finish()
        }

        binding.refreshLayoutCredit.setOnRefreshListener{

            ArrLis=getData()
            binding.recyclerViewDebit.layoutManager=LinearLayoutManager(context)
            binding.recyclerViewDebit.adapter=MyAdapter(ArrLis,"CREDIT")

            // This line is important as it explicitly
            // refreshes only once
            // If "true" it implicitly refreshes forever
            binding.refreshLayoutCredit.isRefreshing=false
        }

        ArrLis=getData()
        binding.recyclerViewDebit.layoutManager= LinearLayoutManager(context)
        binding.recyclerViewDebit.adapter=MyAdapter(ArrLis,"CREDIT")
    }

    private fun getData():ArrayList<dataModel>{
        val ArrLis= ArrayList<dataModel>()
        val db= DBHandler(context).readableDatabase
        val sharPref=context?.getSharedPreferences("TransactionAppPreferences",
            AppCompatActivity.MODE_PRIVATE
        )
        var TABLE_NAME=sharPref?.getString("email","emailNotGetted").toString()
        TABLE_NAME=DBHandler(context).removeSpecialChar(TABLE_NAME)
        TABLE_NAME=TABLE_NAME+"CreditTable"
        val cur=db.rawQuery("SELECT * FROM " + TABLE_NAME + ";" ,null)
        if(cur.moveToFirst()){
            ArrLis.add(dataModel(cur.getString(1),cur.getString(2),cur.getString(3).toLong()))
            //Log.d("jjj"," line 57 " + cur.getString(1)+ " " + cur.getString(2) + " " + cur.getString(3))
            while(cur.moveToNext()){
                ArrLis.add(dataModel(cur.getString(1),cur.getString(2),cur.getString(3).toLong()))
                //Log.d("jjj"," line 60 " + cur.getString(1)+ " " + cur.getString(2) + " " + cur.getString(3))
            }
        }
        return ArrLis
    }
}
