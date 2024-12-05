package uz.excellentshoes.businesscalculation.presentation.viewmodel.impl

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import uz.excellentshoes.businesscalculation.data.types.ShoeColorData
import uz.excellentshoes.businesscalculation.domain.impl.CommonAdminRepositoryImpl
import uz.excellentshoes.businesscalculation.presentation.viewmodel.ShoeColorViewModel

class ShoeColorViewModelImpl : ViewModel(), ShoeColorViewModel {
    override val shoeColorListStateFlow = MutableStateFlow<List<ShoeColorData>>(emptyList())
    private val commonRepository = CommonAdminRepositoryImpl.getInstance()

    override fun getAllShoeColors() {
        viewModelScope.launch {
            commonRepository.getAllShoeColors().collect{ dataList->
                shoeColorListStateFlow.value = dataList
            }
        }
    }

    override fun addShoeColor(data: ShoeColorData) {
        viewModelScope.launch {
            commonRepository.addShoeColor(data)
        }
    }

    override fun deleteShoeColor(objectName: String) {
        viewModelScope.launch {
            commonRepository.deleteShoeColor(objectName)
        }
    }
}