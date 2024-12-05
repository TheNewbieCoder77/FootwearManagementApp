package uz.excellentshoes.businesscalculation.presentation.viewmodel

import androidx.lifecycle.LiveData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import uz.excellentshoes.businesscalculation.data.types.PhoneData
import uz.excellentshoes.businesscalculation.data.types.UserPasswordData

interface LoginViewModel {
    val openNextScreenLiveData: LiveData<Unit>
    val phoneExists: StateFlow<Boolean>
    val errorFullNameLiveData: LiveData<Pair<Boolean,String?>>
    val errorPasswordLiveData: LiveData<Pair<Boolean,String?>>
    val userPasswordStateFlow: StateFlow<List<UserPasswordData>>

    fun checkPassword(pass: String, position: Int): Boolean
    fun checkFullName(fullName: String): Boolean
    fun login()
    suspend fun checkPhoneNumber(phoneNumber: String, jobPosition: Int): Boolean
    fun getAllUserPassword()
}