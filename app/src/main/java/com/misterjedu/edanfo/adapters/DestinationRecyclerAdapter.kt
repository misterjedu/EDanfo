package com.misterjedu.edanfo.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.misterjedu.edanfo.R
import com.misterjedu.edanfo.data.DestinationData
import com.misterjedu.edanfo.data.firebasedata.Trip
import kotlinx.android.synthetic.main.single_destination_price.view.*

class DestinationRecyclerAdapter(
    private var clickListener: OnDestinationClickListener,
) : RecyclerView.Adapter<DestinationRecyclerAdapter.DestinationViewHolders>() {

    private var tripList: MutableList<Trip> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DestinationViewHolders {
        val destinationListView = LayoutInflater.from(parent.context)
            .inflate(R.layout.single_destination_price, parent, false)
        return DestinationViewHolders(destinationListView)
    }

    override fun onBindViewHolder(holder: DestinationViewHolders, position: Int) {
        tripList[position].let { holder.initialize(it, clickListener) }
    }

    override fun getItemCount() = tripList.size

    inner class DestinationViewHolders(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private var location: TextView = itemView.single_destination_location_tv
        private var destination: TextView = itemView.single_destination_destination_tv
        private var price: TextView = itemView.single_destination_price_tv

        fun initialize(item: Trip, action: OnDestinationClickListener) {
            location.text = item.location
            destination.text = item.destination
            price.text = item.price.toString()

            itemView.setOnClickListener {
                action.onItemClick(item, adapterPosition)
            }
        }
    }


    fun setTripList(tripList: MutableList<Trip>) {
        this.tripList = tripList.toMutableList()
    }

    fun addTripToList(trip: Trip) {
        this.tripList.add(trip)
    }

    fun removeTripFromList(position: Int){
        this.tripList.removeAt(position)
    }

    // OnClick Listener InterfaceR
    interface OnDestinationClickListener {
        fun onItemClick(item: Trip, position: Int)
    }
}
