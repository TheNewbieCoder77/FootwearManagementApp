package uz.excellentshoes.businesscalculation.presentation.viewmodel.impl



import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import uz.excellentshoes.businesscalculation.data.types.UserPasswordData
import uz.excellentshoes.businesscalculation.domain.impl.LoginRepositoryImpl
import uz.excellentshoes.businesscalculation.presentation.viewmodel.LoginViewModel

class LoginViewModelImpl : ViewModel(), LoginViewModel {
    override val openNextScreenLiveData = MutableLiveData<Unit>()
    override val phoneExists = MutableStateFlow(false)
    override val errorFullNameLiveData = MutableLiveData<Pair<Boolean, String?>>()
    override val errorPasswordLiveData = MutableLiveData<Pair<Boolean, String?>>()
    override val userPasswordStateFlow = MutableStateFlow<List<UserPasswordData>>(emptyList())

    private val loginRepository = LoginRepositoryImpl.getInstance()


    override fun checkPassword(pass: String, position: Int): Boolean {
        if(pass.isEmpty()) {
            errorPasswordLiveData.value = Pair(false,"Parolni xato kiritdingiz")
            return false
        }
        val result = loginRepository.checkPassword(pass,position)
        errorPasswordLiveData.value = Pair(result,"Parolni xato kiritdingiz")
        return result
    }

    override fun checkFullName(fullName: String):Boolean {
        val result = fullName.isEmpty()
        errorFullNameLiveData.value = Pair(result,"Ism familiyani kiritish shart")
        return result
    }

    override fun login() {
        openNextScreenLiveData.value = Unit
    }

    override suspend fun checkPhoneNumber(phoneNumber: String, jobPosition: Int): Boolean {
        return loginRepository.checkPhoneNumberExists(phoneNumber,jobPosition)

    }

    override fun getAllUserPassword() {
        viewModelScope.launch {
            Log.d("TTT","Request ketdi")
            loginRepository.getAllUserData().collect{
                userPasswordStateFlow.value = it
                Log.d("TTT","Request keldi")
            }
        }
    }
}