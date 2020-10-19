package com.misterjedu.edanfo.adapters

import android.util.SparseBooleanArray
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.misterjedu.edanfo.R
import com.misterjedu.edanfo.data.HistoryData
import kotlinx.android.synthetic.main.single_trip_history.view.*

class TripHistoryRecyclerAdapter(
    var list: List<HistoryData>,
    private var clickListener: OnTripClickListener
) :
    RecyclerView.Adapter<TripHistoryRecyclerAdapter.HistoryViewHolder>() {

    var checkBoxStateArray = SparseBooleanArray()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryViewHolder {
        val tripListView = LayoutInflater.from(parent.context)
            .inflate(R.layout.single_trip_history, parent, false)
        return HistoryViewHolder(tripListView)
    }

    override fun onBindViewHolder(holder: HistoryViewHolder, position: Int) {
        holder.checkBox.isChecked = checkBoxStateArray.get(position, false)
        holder.initialize(list[position], clickListener)
    }

    override fun getItemCount() = list.size

    inner class HistoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {


        var checkBox: CheckBox = itemView.single_trip_history_checkbox
        var price: TextView = itemView.single_trip_history_price_tv


        init {
            checkBox.setOnClickListener {
                if (!checkBoxStateArray.get(adapterPosition, false)) {
                    checkBox.isChecked = true
                    checkBoxStateArray.put(adapterPosition, true )
                } else {
                    checkBox.isChecked = false
                    checkBoxStateArray.put(adapterPosition, false)
                }
            }
        }

        fun initialize(item: HistoryData, action: OnTripClickListener) {
            price.text = item.price.toString()
            checkBox.text = item.trip

            itemView.setOnClickListener {
                action.onItemClick(item, adapterPosition)
            }
        }
    }


    //OnClick Listener InterfaceR
    interface OnTripClickListener {
        fun onItemClick(item: HistoryData, position: Int)
    }
}