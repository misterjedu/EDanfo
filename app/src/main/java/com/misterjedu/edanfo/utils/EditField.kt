package com.misterjedu.edanfo.utils

enum class EditField {
    PASSWORD,
    REPEATPASSWORD,
    NAME,
    EMAIL,
    PHONE,
    AMOUNT,
    PAYMENT,
}


class EditFieldCreator(val constants: ArrayList<String> = arrayListOf()) {

    var enumConstants = constants

   fun createEnum(){

   }


}