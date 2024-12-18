package uz.excellentshoes.businesscalculation.domain

import kotlinx.coroutines.flow.Flow
import uz.excellentshoes.businesscalculation.data.types.UnitData
import uz.excellentshoes.businesscalculation.data.types.WarehouseProductData

interface WarehouseProductRepository {
    suspend fun addWarehouseProduct(data: WarehouseProductData): Boolean
    suspend fun deleteWarehouseProduct(objectName: String)
    suspend fun editWarehouseProduct(data: WarehouseProductData)
    fun getAllWarehouseProducts(): Flow<List<WarehouseProductData>>
    fun getAllUnitData(): Flow<List<UnitData>>
}