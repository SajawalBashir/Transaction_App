package com.example.transactionapp

import DBHandler
import android.R
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout.OnRefreshListener
import com.example.transactionapp.databinding.FragmentDebitBinding


class debitFragment : Fragment() {
    lateinit var ArrLis: ArrayList<dataModel>
    lateinit var binding: FragmentDebitBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
           binding=FragmentDebitBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.debitButton.setOnClickListener{
            if(DBHandler(context).calculateTotal()>0) {
                val intent = Intent(context, dataAdder::class.java)
                intent.putExtra("FROM_WHERE", "DEBIT")
                startActivity(intent)
                //activity?.finish()
            }
            else
                Toast.makeText(context, "Not Enough Amount in Account", Toast.LENGTH_SHORT).show()
        }

        binding.refreshLayoutDebit.setOnRefreshListener{

                    ArrLis=getData()
                    binding.recyclerViewDebit.layoutManager=LinearLayoutManager(context)
                    binding.recyclerViewDebit.adapter=MyAdapter(ArrLis,"DEBIT")

                    // This line is important as it explicitly
                    // refreshes only once
                    // If "true" it implicitly refreshes forever
                    binding.refreshLayoutDebit.isRefreshing=false
        }
        ArrLis=getData()
        binding.recyclerViewDebit.layoutManager=LinearLayoutManager(context)
        binding.recyclerViewDebit.adapter=MyAdapter(ArrLis,"DEBIT")
        //Log.d("jjj"," ArrLis Size" + ArrLis.size.toString())

    }

    private fun getData():ArrayList<dataModel>{
        val ArrLis= ArrayList<dataModel>()
        val db= DBHandler(context).readableDatabase
        val sharPref=context?.getSharedPreferences("TransactionAppPreferences",
            AppCompatActivity.MODE_PRIVATE
        )
        var TABLE_NAME=sharPref?.getString("email","emailNotGetted").toString()
        TABLE_NAME=DBHandler(context).removeSpecialChar(TABLE_NAME)
        TABLE_NAME=TABLE_NAME+"DebitTable"
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