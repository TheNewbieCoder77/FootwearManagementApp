package uz.excellentshoes.businesscalculation.domain.impl


import android.util.Log
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.tasks.await
import uz.excellentshoes.businesscalculation.app.App
import uz.excellentshoes.businesscalculation.data.AppPreferences
import uz.excellentshoes.businesscalculation.data.types.PhoneData
import uz.excellentshoes.businesscalculation.data.types.UserPasswordData
import uz.excellentshoes.businesscalculation.domain.LoginRepository


class LoginRepositoryImpl private constructor() : LoginRepository {
    private val appPreferences = AppPreferences.getInstance()
    private val databaseReference = App.database.getReference("PhoneData")
    private val databaseReferenceUserPassword = App.database.getReference("UserPasswordData")
    private val databaseReferenceUserPasswordData = App.database.getReference("UserPasswordData")
    private var userDataList = listOf<UserPasswordData>()

    init {
        fetchUserData()
    }

    private fun fetchUserData() {
        databaseReferenceUserPassword.get().addOnSuccessListener { dataSnapshot ->
            val userList = mutableListOf<UserPasswordData>()
            for (snapshot in dataSnapshot.children) {
                snapshot.getValue(UserPasswordData::class.java)?.let {
                    userList.add(it)
                }
            }
            userDataList = userList
        }.addOnFailureListener { exception ->
            println("Error fetching data: ${exception.message}")
        }
    }


    override fun checkPassword(password: String, position: Int): Boolean {
        val data = userDataList.find { it.jobPosition == position }
        return data?.userPassword == password
    }

    override suspend fun checkPhoneNumberExists(phoneNumber: String, jobPosition: Int): Boolean {
        if (phoneNumber.isEmpty()) {
            return false
        }
        val snapshot = databaseReference.orderByChild("phoneNumber").equalTo(phoneNumber).get().await()
        if (snapshot.exists()) {
            for (child in snapshot.children) {
                val phoneData = child.getValue(PhoneData::class.java)
                if (phoneData != null && phoneData.jobPosition == jobPosition && phoneData.phoneNumber == phoneNumber) {
                    return true
                }
            }
            return false
        }
        return false
    }


    override fun getAllUserData(): Flow<List<UserPasswordData>> = callbackFlow<List<UserPasswordData>> {
        val listener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val dataList = snapshot.children.mapNotNull { data ->
                    data.getValue(UserPasswordData::class.java)
                }
                trySend(dataList)
            }

            override fun onCancelled(error: DatabaseError) {
                trySend(emptyList())
            }
        }
        databaseReferenceUserPasswordData.addValueEventListener(listener)
        awaitClose {
            databaseReferenceUserPasswordData.removeEventListener(listener)
        }
    }.flowOn(Dispatchers.IO)



    companion object {
        private lateinit var loginRepository: LoginRepository
        fun getInstance(): LoginRepository {
            if (!::loginRepository.isInitialized) {
                loginRepository = LoginRepositoryImpl()
            }
            return loginRepository
        }
    }

}