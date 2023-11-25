package com.example.dodamdodam.ui.signup

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.dodamdodam.ui.utils.State
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.Calendar
import java.util.Locale
import java.util.regex.Pattern

class SignUpViewModel: ViewModel() {

    var auth = FirebaseAuth.getInstance()
    val email = MutableLiveData<String>()
    val nickname = MutableLiveData<String>()
    val phoneNumber = MutableLiveData<String>()
    val password = MutableLiveData<String>()    // 비밀번호
    val passwordRe = MutableLiveData<String>()  // 비밀번호 다시 입력
    val errorMessage = MutableLiveData<String?>()
    val state = MutableLiveData<State>()

    fun sigininAndSignup() : Boolean{
        var k = false
        auth?.createUserWithEmailAndPassword(email.value.toString(),password.value.toString())
            ?.addOnCompleteListener {
                    task ->

                if (task.isSuccessful){
                    val db = FirebaseFirestore.getInstance()
                    state.value = State.OK
                    k = true
                    val user = FirebaseAuth.getInstance().currentUser
                    val uid = user?.uid
                    val calendar = Calendar.getInstance()
                    val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                    val currentDate = dateFormat.format(calendar.time)

                    val data2 = hashMapOf(
                        "id" to uid.toString(),
                        "email" to email.value.toString(),
                        "name" to nickname.value.toString(),
                        "family" to email.value?.dropLast(10).toString(),
                        "createdate" to  currentDate.toString(),
                    )
                    db.collection(email.value.toString())
                        .document("userinfo")
                        .set(data2)
                        .addOnSuccessListener {
                            Log.e("dd","success")
                        }
                        .addOnFailureListener { exception ->
                            Log.e("dd",exception.toString())
                        }
                    val data3 = hashMapOf(
                        "email" to email.value.toString(),
                        "name" to nickname.value.toString(),
                    )
                    db.collection("family")
                        .document("family")
                        .collection(email.value.toString().dropLast(10))
                        .document(email.value.toString().dropLast(10))
                        .set(data3)
                        .addOnSuccessListener {
                            Log.e("dd","success")
                        }
                        .addOnFailureListener { exception ->
                            Log.e("dd",exception.toString())
                        }

                    val data4 = hashMapOf(
                        "exp" to 0,
                    )
                    db.collection("family")
                        .document("detail")
                        .collection(email.value.toString().dropLast(10))
                        .document("exp")
                        .set(data4)
                        .addOnSuccessListener {
                            Log.e("dd","success")
                        }
                        .addOnFailureListener { exception ->
                            Log.e("dd",exception.toString())
                        }



                }else if(task.exception?.message.isNullOrEmpty()){
                    Log.e("dd",task.exception?.message.toString())
                    state.value = State.FAIL
                }else{
                    Log.d("dd","ㄴㄴ")
                    sigininEmail()
                }
            }
        return k
    }
    private fun sigininEmail(){

        auth?.createUserWithEmailAndPassword(email.value.toString(),password.value.toString())
            ?.addOnCompleteListener {
                    task ->
                if (task.isSuccessful){
                    if(task.result?.user!=null){
                        state.value = State.OK
                    }
                    else{
                        state.value = State.FAIL
                    }
                }else{
                    errorMessage.value = task.exception?.message
                    state.value = State.FAIL
                }
            }
    }

    fun inputEmailCheck() : Boolean{

        if (email.value.isNullOrBlank()) {
            return false
        }
        if(!Pattern.matches("^[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*@[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*.[a-zA-Z]{2,3}$", email.value)){
            return false
        }
        return true
    }
    fun inputPasswordCheck() : Boolean{

        if (password.value.isNullOrBlank()) {
            return false
        }
        return true
    }
    fun inputPasswordReCheck() : Boolean{

        if (password.value.isNullOrBlank() || password.value != passwordRe.value) {
            return false
        }
        return true
    }

    fun inputCheckEmailPassword(): Boolean {
        if (email.value.isNullOrBlank()) {
            return false
        }

        if (password.value.isNullOrBlank()) {
            return false
        }

        if (passwordRe.value.isNullOrBlank()) {
            return false
        }

        if (password.value != passwordRe.value) {
            return false
        }

        return true
    }

    fun inputCheckNameNickName(): Boolean {
        if (nickname.value.isNullOrBlank()) {
            return false
        }

        if (nickname.value!!.length !in 2..8) {
            return false
        }

        return true
    }
    fun inputCheckPhoneNumber(): Boolean {
        if (phoneNumber.value.isNullOrBlank()) {
            return false
        }

        if (phoneNumber.value!!.length !in 11..11) {
            return false
        }

        return true
    }
}