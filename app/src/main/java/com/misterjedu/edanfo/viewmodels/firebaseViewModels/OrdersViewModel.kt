package com.misterjedu.edanfo.viewmodels.firebaseViewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.database.*
import com.misterjedu.edanfo.data.firebasedata.Orders
import com.misterjedu.edanfo.utils.DRIVER_DESTINATION

class OrdersViewModel : ViewModel() {

    //New Order
    private val _orderChange = MutableLiveData<Orders?>()
    val orderChange: LiveData<Orders?>
        get() = _orderChange

    //All Ongoing Orders

    private val _onGoingOrders = MutableLiveData<MutableList<Orders?>>()
    val onGoingOrders: LiveData<MutableList<Orders?>>
        get() = _onGoingOrders


    //Add an Order by Passenger

    //Fetch all orders for a particular destination
    fun fetchAllOrders(destinationId: String) {
        FirebaseDatabase.getInstance().getReference("${DRIVER_DESTINATION}/${destinationId}")
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        val orderList: MutableList<Orders?> = mutableListOf()
                        for (order in snapshot.children) {
                            val singleOrder = order.getValue(Orders::class.java)
                            singleOrder?.id = order.key

                            if (
                                singleOrder != null &&
                                !singleOrder.isCancelled!! &&
                                !singleOrder.isCompleted!!
                            ) {
                                orderList.add(singleOrder)
                            }
                        }
                        _onGoingOrders.value = orderList
                    }
                }

                override fun onCancelled(error: DatabaseError) {

                }
            })

    }


    fun getOrderUpdate(destinationId: String) {
        val driverDestinationDatabase =
            FirebaseDatabase.getInstance().getReference("${DRIVER_DESTINATION}/${destinationId}")
        driverDestinationDatabase.addChildEventListener(childEventListener)

    }

    // Listen to events on Orders children
    private val childEventListener = object : ChildEventListener {
        override fun onCancelled(error: DatabaseError) {}
        override fun onChildMoved(snapshot: DataSnapshot, p1: String?) {}
        override fun onChildChanged(snapshot: DataSnapshot, p1: String?) {
            val newChange = snapshot.getValue(Orders::class.java)
            if (newChange != null) {
                newChange.id = snapshot.key
                _orderChange.value = newChange
            } else {
                _orderChange.value = null
            }
        }

        override fun onChildRemoved(snapshot: DataSnapshot) {}

        override fun onChildAdded(snapshot: DataSnapshot, p1: String?) {
            val newOrder = snapshot.getValue(Orders::class.java)
            if (newOrder != null) {
                newOrder.id = snapshot.key
                _orderChange.value = newOrder
            } else {
                _orderChange.value = null
            }
        }
    }


}
