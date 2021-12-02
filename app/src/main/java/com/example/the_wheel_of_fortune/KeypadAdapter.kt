package com.example.the_wheel_of_fortune

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import java.util.*

class KeypadAdapter(private val mList: List<ItemsViewModelKeypad>, val onKeyClick: OnKeyClick, val saveData: ArrayList<Char>) : RecyclerView.Adapter<KeypadAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.card_view_design_keypad, parent, false)

        return ViewHolder(view)
    }

    // binds the list items to a view
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val ItemsViewModel = mList[position]
        holder.textView.text =  ItemsViewModel.text.toString()
        if(saveData.contains(ItemsViewModel.text!!)){
            holder.card.setBackgroundColor(Color.parseColor("#d3d3d3"))
        }
        else{
            holder.card.setBackgroundColor(Color.parseColor("#f5f5f5"))
            holder.card.setOnClickListener(View.OnClickListener { view ->onKeyClick.onClick(ItemsViewModel)  })
        }

        





    }

    override fun getItemCount(): Int {
        return mList.size
    }

    class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        val textView: TextView = itemView.findViewById(R.id.textView)
        val card: CardView = itemView.findViewById(R.id.card)

    }
}
