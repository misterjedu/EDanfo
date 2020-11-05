package com.misterjedu.edanfo.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.misterjedu.edanfo.R
import com.misterjedu.edanfo.data.BusTrip
import kotlinx.android.synthetic.main.single_find_bus_layout.view.*

class BusTripRecyclerAdapter(
    private var clickListener: OnBusTripClickListener,
) : RecyclerView.Adapter<BusTripRecyclerAdapter.BusTripViewHolder>() {


    private var busTripList: MutableList<BusTrip> = mutableListOf()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BusTripViewHolder {
        val busTripListView = LayoutInflater.from(parent.context)
            .inflate(R.layout.single_find_bus_layout, parent, false)
        return BusTripViewHolder(busTripListView)
    }

    override fun onBindViewHolder(holder: BusTripViewHolder, position: Int) {
        holder.initialize(busTripList[position], clickListener)
    }

    override fun getItemCount() = busTripList.size


    inner class BusTripViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var busId: TextView = itemView.single_find_bus_bus_id_tv
        var timeStarted: TextView = itemView.single_find_bus_time_started_tv
        var journey: TextView = itemView.single_fins_bus_layout_journey_tv
        var selectTrip: Button = itemView.single_find_bus_select_button
        var journeyPrice: TextView = itemView.single_find_bus_layout_price_tv

        fun initialize(item: BusTrip, action: OnBusTripClickListener) {
            busId.text = item.busUniqueId
            timeStarted.text = item.timeStarted
            journey.text = item.journey
            journeyPrice.text = item.price.toString()

            selectTrip.setOnClickListener {
                action.onButtonClick(item, adapterPosition)
            }
        }
    }


    fun addBustTripList(list: MutableList<BusTrip>) {
        busTripList = list
    }


    // OnClick Listener Interface
    interface OnBusTripClickListener {
        fun onButtonClick(item: BusTrip, position: Int)
    }
}