package uz.excellentshoes.businesscalculation.presentation.viewmodel.impl

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import uz.excellentshoes.businesscalculation.data.types.UserPasswordData
import uz.excellentshoes.businesscalculation.domain.impl.CommonAdminRepositoryImpl
import uz.excellentshoes.businesscalculation.presentation.viewmodel.UserPasswordViewModel

class UserPasswordViewModelImpl : ViewModel(), UserPasswordViewModel {
    private val commonRepository = CommonAdminRepositoryImpl.getInstance()
    override val userPasswordStateFlow = MutableStateFlow<List<UserPasswordData>>(emptyList())

    override fun addUserPassword(data: UserPasswordData) {
        viewModelScope.launch {
            commonRepository.upsertUserData(data)
        }
    }

    override fun deleteUserPassword(objectName: String) {
        viewModelScope.launch {
            commonRepository.deleteUserData(objectName)
        }
    }

    override fun getAllUserPassword() {
        viewModelScope.launch {
            commonRepository.getAllUserData().collect{
                userPasswordStateFlow.value = it
            }
        }
    }

}