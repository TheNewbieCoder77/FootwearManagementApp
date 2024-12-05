package uz.excellentshoes.businesscalculation.presentation.viewmodel.impl

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import uz.excellentshoes.businesscalculation.data.types.KontragentData
import uz.excellentshoes.businesscalculation.domain.impl.CommonAdminRepositoryImpl
import uz.excellentshoes.businesscalculation.presentation.viewmodel.KontragentViewModel

class KontragentViewModelImpl : ViewModel(), KontragentViewModel {
    override val kontragentListStateFlow = MutableStateFlow<List<KontragentData>>(emptyList())
    private val commonRepository = CommonAdminRepositoryImpl.getInstance()

    override fun getAllKontragent() {
        viewModelScope.launch {
            commonRepository.getAllKontragent().collect{ dataList->
                kontragentListStateFlow.value = dataList
            }
        }
    }

    override fun addKontragent(data: KontragentData) {
        viewModelScope.launch {
            commonRepository.addKontragent(data)
        }
    }

    override fun deleteKontragent(objectName: String) {
        viewModelScope.launch {
            commonRepository.deleteKontragent(objectName)
        }
    }
}