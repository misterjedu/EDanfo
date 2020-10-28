package com.misterjedu.edanfo.ui.auth

import android.os.Bundle
import android.text.Editable
import android.text.SpannableStringBuilder
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.google.android.gms.tasks.TaskExecutors
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider
import com.misterjedu.edanfo.R
import com.misterjedu.edanfo.data.PhoneToOtp
import com.misterjedu.edanfo.utils.*
import com.mukesh.OtpView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_phone_activation.*
import kotlinx.android.synthetic.main.fragment_sign_up.*
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@AndroidEntryPoint
class PhoneActivationFragment : Fragment() {

    @Inject
    lateinit var firebaseAuth: FirebaseAuth

    lateinit var otpView: OtpView
    private lateinit var dataFromSignUp: PhoneToOtp

    lateinit var storedVerificationId: String
    var resendToken: PhoneAuthProvider.ForceResendingToken? = null

    private val args: PhoneActivationFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Get Argument Passed from SignUpFragment
        dataFromSignUp = args.phoneToOtp

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_phone_activation, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

//        fragment_phone_activation_progress_bar.show(fragment_phone_activation_continue_btn)

        val enterDigitString =
            "Enter the 6-digit code sent to you at ${dataFromSignUp.phoneNumber}. Did you enter the correct number?"

        fragment_phone_activation_enter_digit_tv.text = enterDigitString

        otpView = fragment_phone_activation_otp_view

        // Load Header Background Image
        Glide.with(this)
            .load(R.drawable.danfo_curved_bg_2)
            .into(fragment_phone_activation_header_img)


        // Call the Verify code method to verify of the OTP if the user has to manually enter it
        fragment_phone_activation_continue_btn.setOnClickListener {
            if (validateOTP(fragment_phone_activation_otp_view.text.toString())) {
                verifyCode(otpView.text.toString())
            } else {
                showSnackBar(fragment_phone_activation_continue_btn, "Invalid OTP")
            }
        }


        //Send Phone Verification on Create.A~
        sendVerificationCode(dataFromSignUp.phoneNumber)

        //Resend Phone Verification
        fragment_phone_activation_resend_code_tv.setOnClickListener {
            if (resendToken != null) {
                fragment_phone_activation_resend_code_tv.hide()
                resendVerificationCode(dataFromSignUp.phoneNumber)
            }

        }

        // Back Arrow
        fragment_phone_activation_back_arrow_iv.setOnClickListener {
            findNavController().popBackStack()
        }
    }


    //Method to send the verification code
    private fun sendVerificationCode(number: String) {
        fragment_phone_activation_progress_bar.show()  //Show Progress Bar
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
            number,
            60L,
            TimeUnit.SECONDS,
            TaskExecutors.MAIN_THREAD,
            mCallbacks,
        )
    }

    //Method to Re-send the verification code
    private fun resendVerificationCode(number: String) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
            number,
            60L,
            TimeUnit.SECONDS,
            TaskExecutors.MAIN_THREAD,
            mCallbacks,
            resendToken
        )
    }


    //The callback to detect the verification status
    private val mCallbacks: PhoneAuthProvider.OnVerificationStateChangedCallbacks =
        object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            override fun onCodeSent(
                verificationId: String,
                token: PhoneAuthProvider.ForceResendingToken
            ) {
                super.onCodeSent(verificationId, token)

                //storing the verification id that is sent to the user
                storedVerificationId = verificationId
                resendToken = token
            }


            override fun onVerificationCompleted(phoneAuthCredential: PhoneAuthCredential) {
                //Getting the code sent by SMS
                val code = phoneAuthCredential.smsCode
                showSnackBar(fragment_phone_activation_resend_code_tv, "Verification Completed")

                //Enable Button when code sent and Hide Progress bar
                fragment_phone_activation_progress_bar.hide(fragment_phone_activation_continue_btn)

                //If code is automatically detected by Firebase, Verify code Immediately
                if (code != null) {
                    val otpCode: Editable = SpannableStringBuilder(code)
                    otpView.text = otpCode
                    //verifying the code
                    verifyCode(code)
                }
            }

            override fun onVerificationFailed(e: FirebaseException) {
                fragment_phone_activation_progress_bar.hide()
                fragment_phone_activation_resend_code_tv.show()
                Log.i("Firebase Otp Failed", e.message.toString())
                e.message?.let { showSnackBar(fragment_phone_activation_resend_code_tv, it) }
            }
        }


    //Use Otp to get Signup Credential
    private fun verifyCode(code: String) {
        val credential: PhoneAuthCredential =
            PhoneAuthProvider.getCredential(storedVerificationId, code)

        signInWithCredential(credential)
    }


    //Sign In using Credential
    private fun signInWithCredential(credential: PhoneAuthCredential) {

        firebaseAuth.signInWithCredential(credential).addOnCompleteListener {
            if (it.isSuccessful) {


                //Save User Phone Number To shared Preference
                saveToSharedPreference(
                    requireActivity(),
                    DRIVERPHONENUMBER,
                    dataFromSignUp.phoneNumber
                )


                if (dataFromSignUp.fragmentName == "Sign Up") {
                    findNavController().navigate(R.id.action_phoneActivationFragment_to_createProfileLanding)
                } else {
                    findNavController().navigate(R.id.action_phoneActivationFragment_to_changePasswordFragment2)
                }
            } else {
                Toast.makeText(requireContext(), it.exception?.message, Toast.LENGTH_SHORT).show()
            }
        }
    }
}
