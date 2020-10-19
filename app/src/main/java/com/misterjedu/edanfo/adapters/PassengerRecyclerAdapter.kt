package com.misterjedu.edanfo.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.misterjedu.edanfo.R
import com.misterjedu.edanfo.data.PassengerData
import kotlinx.android.synthetic.main.single_passenger_order.view.*

class PassengerRecyclerAdapter(
    private var clickListener: OnPassengerClickListener,
    private var passengerList : List<PassengerData>

) : RecyclerView.Adapter<PassengerRecyclerAdapter.PassengerViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PassengerViewHolder {
        val passengerListView = LayoutInflater.from(parent.context)
            .inflate(R.layout.single_passenger_order, parent, false)
        return PassengerViewHolder(passengerListView)
    }

    override fun getItemCount() = passengerList.size

    override fun onBindViewHolder(holder: PassengerViewHolder, position: Int) {
        holder.initialize(passengerList[position], clickListener)
    }


    inner class PassengerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private var passengerName : TextView = itemView.single_passenger_order_name_tv
        private var timeOrdered : TextView = itemView.single_passenger_time_ordered_tv
        private var tripDestination : TextView = itemView.single_passenger_order_trip_tv
        var cancelPassenegerBtn : Button = itemView.single_passenger_cancel_order_btn

        fun initialize(item : PassengerData, action: OnPassengerClickListener){
            passengerName.text = item.name
            timeOrdered.text = item.timeOrdered
            tripDestination.text = item.trip

            itemView.setOnClickListener {
                action.onItemClick(item, adapterPosition)
            }
        }


    }

    //OnClick Listener InterfaceR
    interface OnPassengerClickListener {
        fun onItemClick(item: PassengerData, position: Int)
    }
}