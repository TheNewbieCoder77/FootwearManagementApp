package uz.excellentshoes.businesscalculation.presentation.viewmodel

import androidx.lifecycle.LiveData
import kotlinx.coroutines.flow.StateFlow
import uz.excellentshoes.businesscalculation.data.types.UnitData
import uz.excellentshoes.businesscalculation.data.types.WarehouseProductData

interface WarehouseProductViewModel {
    val progressBarLiveData: LiveData<Boolean>
    val warehouseProductStateFlow: StateFlow<List<WarehouseProductData>>
    val unitProductStateFlow: StateFlow<List<UnitData>>

    fun getAllWarehouseProducts()
    fun getAllUnits()
    fun addWarehouseProduct(data: WarehouseProductData)
    fun deleteWarehouseProduct(objectName: String)
    fun editWarehouseProduct(data: WarehouseProductData)

}