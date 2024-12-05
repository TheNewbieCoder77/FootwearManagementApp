package uz.excellentshoes.businesscalculation.utils

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import uz.excellentshoes.businesscalculation.presentation.viewmodel.impl.CommonShoeCheckerViewModelImpl

class CommonShoeCheckerViewModelFactory(
    private val currentDate: String
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CommonShoeCheckerViewModelImpl::class.java)) {
            return CommonShoeCheckerViewModelImpl(currentDate) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}