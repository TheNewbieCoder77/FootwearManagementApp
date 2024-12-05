package uz.excellentshoes.businesscalculation.domain

import kotlinx.coroutines.flow.Flow
import uz.excellentshoes.businesscalculation.data.types.FinishedSkinTailData
import uz.excellentshoes.businesscalculation.data.types.PreparedTailData
import uz.excellentshoes.businesscalculation.data.types.ProcessTailData
import uz.excellentshoes.businesscalculation.data.types.SkinData

interface SkinTailorRepository {
    //SkinAdd
    suspend fun addProcessDataList(processTailDataList: List<ProcessTailData>): Boolean
    suspend fun addFinishedSkinTail(data: FinishedSkinTailData)

    //SkinTailorScreen
    fun getProcessDataByPhoneNumber(phoneNumber: String): Flow<List<ProcessTailData>>
    suspend fun deleteProcessData(objectName: String): Boolean
    suspend fun addPreparedTailData(preparedTailData: PreparedTailData)
}