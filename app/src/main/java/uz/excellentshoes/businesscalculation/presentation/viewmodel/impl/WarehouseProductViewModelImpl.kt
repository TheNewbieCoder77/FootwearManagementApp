package uz.excellentshoes.businesscalculation.presentation.viewmodel.impl

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import uz.excellentshoes.businesscalculation.data.types.UnitData
import uz.excellentshoes.businesscalculation.data.types.WarehouseProductData
import uz.excellentshoes.businesscalculation.domain.impl.WarehouseProductRepositoryImpl
import uz.excellentshoes.businesscalculation.presentation.viewmodel.WarehouseProductViewModel

class WarehouseProductViewModelImpl: ViewModel(), WarehouseProductViewModel {
    override val progressBarLiveData = MutableLiveData<Boolean>()
    override val warehouseProductStateFlow = MutableStateFlow<List<WarehouseProductData>>(emptyList())
    override val unitProductStateFlow = MutableStateFlow<List<UnitData>>(emptyList())
    val unitsList = unitProductStateFlow.asStateFlow()
    private val repository = WarehouseProductRepositoryImpl.getInstance()

    override fun getAllWarehouseProducts() {
        viewModelScope.launch {
            repository.getAllWarehouseProducts().collect{
                warehouseProductStateFlow.value = it
            }
        }
    }

    override fun getAllUnits() {
        viewModelScope.launch {
            repository.getAllUnitData().collect{
                unitProductStateFlow.value = it
            }
        }
    }

    override fun addWarehouseProduct(data: WarehouseProductData) {
        viewModelScope.launch {
            repository.addWarehouseProduct(data)
        }
    }

    override fun deleteWarehouseProduct(objectName: String) {
        viewModelScope.launch {
            repository.deleteWarehouseProduct(objectName)
        }
    }

    override fun editWarehouseProduct(data: WarehouseProductData) {
        viewModelScope.launch {
            repository.editWarehouseProduct(data)
        }
    }

}