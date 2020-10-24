package com.misterjedu.edanfo.helpers

import com.misterjedu.edanfo.data.HistoryData
import com.misterjedu.edanfo.data.PassengerData

object DummyData {
    fun tripData(): List<HistoryData> {
        return arrayListOf(
            HistoryData(1, "Mushin to Yaba", 100),
            HistoryData(2, "Shagamu to Boluwatife", 100),
            HistoryData(3, "Lekki to Sangotedo", 100),
            HistoryData(4, "Lekki to Sangotedo", 100),
            HistoryData(5, "Lekki to Ikorodu", 100),
            HistoryData(6, "Mushin to Yaba", 100),
            HistoryData(7, "Shagamu to Boluwatife", 100),
            HistoryData(8, "Lekki to Ikorodu", 100),
            HistoryData(9, "Shagamu to Boluwatife", 100),
            HistoryData(10, "Lekki to Sangotedo", 100),
            HistoryData(11, "Lekki to Ikorodu", 100),
            HistoryData(12, "Mushin to Yaba", 100),
            HistoryData(13, "Lekki to Sangotedo", 100),
            HistoryData(14, "Lekki to Sangotedo", 100),
            HistoryData(15, "Lekki to Ikorodu", 100),
            HistoryData(16, "Mushin to Yaba", 100),
            HistoryData(17, "Shagamu to Boluwatife", 100),
            HistoryData(18, "Lekki to Ikorodu", 100),
            HistoryData(19, "Shagamu to Boluwatife", 100),
            HistoryData(20, "Lekki to Sangotedo", 100),
            HistoryData(21, "Lekki to Ikorodu", 100),
            HistoryData(21, "Mushin to Yaba", 100),
            HistoryData(22, "Shagamu to Boluwatife", 100),
            HistoryData(23, "Lekki to Sangotedo", 100),
            HistoryData(24, "Lekki to Sangotedo", 100),
            HistoryData(25, "Lekki to Ikorodu", 100),
            HistoryData(26, "Mushin to Yaba", 100),
            HistoryData(27, "Shagamu to Boluwatife", 100),
            HistoryData(28, "Lekki to Ikorodu", 100),
            HistoryData(29, "Shagamu to Boluwatife", 100),
            HistoryData(30, "Lekki to Sangotedo", 100),
        )
    }


    fun passengerList(): List<PassengerData> {
       return arrayListOf(
            PassengerData("Badmus Shobowale", "Mushin to Yaba", "2 mins ago"),
            PassengerData("Omo Ologo", "Shagamu to Boluwatife", "5 mins ago"),
            PassengerData("Adewale Shwab", "Lekki to Sangotedo", "5 mins ago"),
            PassengerData("Funke Duduke", "Mushin to Yaba", "10 mins ago"),
            PassengerData("Hushpuppy MoronmuboMoronmubo", "Lekki to Ikorodu", "11 mins ago"),
            PassengerData("Badmus Shobowale", "Mushin to Yaba", "13 mins ago"),
            PassengerData("Funke Duduke", "Mushin to Yaba", "15 mins ago"),
        )
    }
}
