package com.example.transactionapp

import DBHandler
import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView

class MyAdapter(data: ArrayList<dataModel>, from: String) :
    RecyclerView.Adapter<MyAdapter.ViewHolder>() {
    var from: String
    var ArrLis: ArrayList<dataModel>

    //Intent intent;
    var context: Context? = null

    //boolean check=true;
    init {
//        Log.d("ttt"," hotel " + hotel.size());
        this.from = from
        this.ArrLis = data
        //        Log.d("ttt"," this.hotel " + this.hotel.size());
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v: View = LayoutInflater.from(parent.context).inflate(
            R.layout.recycler_view_row, parent, false
        )
        //        if(check){
//          context=parent.getContext();
//          intent=new Intent(parent.getContext(),SecondActivity.class);
//          check=false;
//        }
        context = parent.context
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val dataModelObj = ArrLis.get(position)

        Log.d(
            "jjj",
            " dataModelObj=" + dataModelObj.desc + " " + dataModelObj.date + " " + dataModelObj.amount
        )
        if (from.equals("DEBIT"))
            holder.img.setImageResource(R.drawable.red_arrow_up)
        else
            holder.img.setImageResource(R.drawable.green_arrow_up)
        holder.desc.setText(dataModelObj.desc)
        holder.date.setText(dataModelObj.date)
        holder.amou.setText(" $" + dataModelObj.amount)
        holder.imgDel.setOnClickListener {
            val sharPref = context?.getSharedPreferences(
                "TransactionAppPreferences",
                AppCompatActivity.MODE_PRIVATE
            )
            val email = sharPref?.getString("email", "emailNotGetted").toString()
            if (from.equals("DEBIT")) {
                Log.d("jjj","pos="+position)
                DBHandler(context).deleteDataFromDebit(email,dataModelObj.date,dataModelObj.desc,dataModelObj.amount)

            } else if (from.equals("CREDIT")) {
                if (DBHandler(context).calculateTotal() > 0) {
                    DBHandler(context).deleteDataFromCredit(email,dataModelObj.date,dataModelObj.desc,dataModelObj.amount)
                } else
                    Toast.makeText(context, "Can't delete total amount is zero now", Toast.LENGTH_SHORT).show()
            }
        }

    }



    override fun getItemCount(): Int {
        return ArrLis.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var img: ImageView
        var date: TextView
        var desc: TextView
        var amou: TextView
        var imgDel: ImageView

        init {
            imgDel = itemView.findViewById(R.id.imgDelete)
            img = itemView.findViewById(R.id.imageDebit)
            date = itemView.findViewById(R.id.textDateDebit)
            desc = itemView.findViewById(R.id.textDescDebit)
            amou = itemView.findViewById(R.id.textAmountDebit)
        }
    }
}