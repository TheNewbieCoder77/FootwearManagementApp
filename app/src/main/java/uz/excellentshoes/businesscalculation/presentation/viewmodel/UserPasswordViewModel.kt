package uz.excellentshoes.businesscalculation.presentation.viewmodel

import kotlinx.coroutines.flow.StateFlow
import uz.excellentshoes.businesscalculation.data.types.UserPasswordData

interface UserPasswordViewModel {
    val userPasswordStateFlow: StateFlow<List<UserPasswordData>>

    fun addUserPassword(data: UserPasswordData)
    fun deleteUserPassword(objectName: String)
    fun getAllUserPassword()

}