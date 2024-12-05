package uz.excellentshoes.businesscalculation.domain

import kotlinx.coroutines.flow.Flow
import uz.excellentshoes.businesscalculation.data.types.PreparedToSailData
import uz.excellentshoes.businesscalculation.data.types.SoldShoeData

interface ShoeSellerRepository {
    fun getAllPreparedToSailData(): Flow<List<PreparedToSailData>>
    suspend fun addSoldShoeDataList(dataList: List<SoldShoeData>): Boolean
    suspend fun deletePreparedToSailData(objectName: String)
}