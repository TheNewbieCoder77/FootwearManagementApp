package uz.excellentshoes.businesscalculation.presentation.viewmodel.impl

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import uz.excellentshoes.businesscalculation.data.types.ShoeModelData
import uz.excellentshoes.businesscalculation.domain.impl.CommonAdminRepositoryImpl
import uz.excellentshoes.businesscalculation.presentation.viewmodel.ShoeModelViewModel

class ShoeModelViewModelImpl : ViewModel(), ShoeModelViewModel {
    override val shoeModelsListStateFlow = MutableStateFlow<List<ShoeModelData>>(emptyList())
    private val commonRepository = CommonAdminRepositoryImpl.getInstance()

    override fun getAllShoeModels() {
        viewModelScope.launch {
            commonRepository.getAllShoeModels().collect{ dataList->
                shoeModelsListStateFlow.value = dataList
            }
        }
    }

    override fun addShoeModel(data: ShoeModelData) {
        viewModelScope.launch {
            commonRepository.addShoeModel(data)
        }
    }

    override fun delete(objectName: String) {
        viewModelScope.launch {
            commonRepository.deleteShoeModel(objectName)
        }
    }
}