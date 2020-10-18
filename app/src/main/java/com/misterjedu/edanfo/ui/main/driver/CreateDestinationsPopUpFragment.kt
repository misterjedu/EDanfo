package com.misterjedu.edanfo.ui.main.driver

import android.app.Dialog
import android.os.Bundle
import android.view.View
import android.app.AlertDialog
import androidx.appcompat.app.AppCompatDialogFragment
import com.misterjedu.edanfo.R
import kotlinx.android.synthetic.main.fragment_create_destinations.view.*


class CreateDestinationsPopUpFragment :  AppCompatDialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        super.onCreateDialog(savedInstanceState)
        //Create an Alert Dialog
        val builder: AlertDialog.Builder =  AlertDialog.Builder(activity)
        val inflater = requireActivity().layoutInflater

        //Inflate the view
        val view: View = inflater.inflate(R.layout.fragment_create_destinations, null)


        //Close the Dialog when cancel button clicked
        view.create_destination_close_dialog_btn.setOnClickListener {
            dialog?.dismiss();
        }


        //return dialog view
        builder.setView(view)
        return builder.create()
    }



}