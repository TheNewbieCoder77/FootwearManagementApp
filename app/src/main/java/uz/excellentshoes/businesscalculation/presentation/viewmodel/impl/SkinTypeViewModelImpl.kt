package uz.excellentshoes.businesscalculation.presentation.viewmodel.impl

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import uz.excellentshoes.businesscalculation.data.types.SkinTypeData
import uz.excellentshoes.businesscalculation.domain.impl.CommonAdminRepositoryImpl
import uz.excellentshoes.businesscalculation.presentation.viewmodel.SkinTypeViewModel

class SkinTypeViewModelImpl : ViewModel(), SkinTypeViewModel {
    override val skinTypeListStateFlow = MutableStateFlow<List<SkinTypeData>>(emptyList())
    private val commonRepository = CommonAdminRepositoryImpl.getInstance()

    override fun getAllSkinTypes() {
        viewModelScope.launch {
            commonRepository.getAllSkinTypes().collect{ dataList->
                skinTypeListStateFlow.value = dataList
            }
        }
    }

    override fun addSkinType(data: SkinTypeData) {
        viewModelScope.launch {
            commonRepository.addSkinType(data)
        }
    }

    override fun deleteSkinType(objectName: String) {
        viewModelScope.launch {
            commonRepository.deleteSkinType(objectName)
        }
    }
}