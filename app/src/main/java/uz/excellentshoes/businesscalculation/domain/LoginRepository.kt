package uz.excellentshoes.businesscalculation.domain

import kotlinx.coroutines.flow.Flow
import uz.excellentshoes.businesscalculation.data.types.UserPasswordData

interface LoginRepository {
    fun checkPassword(password: String, position: Int): Boolean
    suspend fun checkPhoneNumberExists(phoneNumber: String, jobPosition: Int): Boolean
    fun getAllUserData(): Flow<List<UserPasswordData>>
}