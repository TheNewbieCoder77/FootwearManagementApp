package uz.excellentshoes.businesscalculation.domain

import kotlinx.coroutines.flow.Flow
import uz.excellentshoes.businesscalculation.data.types.FinishedShoeMakeData
import uz.excellentshoes.businesscalculation.data.types.PreparedShoeMakerData
import uz.excellentshoes.businesscalculation.data.types.PreparedTailData
import uz.excellentshoes.businesscalculation.data.types.ProcessShoeMakerData

interface CommonShoeMakerRepository {
    //ScreenShoeMaker
    fun getAllPreparedTailData(): Flow<List<PreparedTailData>>
    suspend fun addProcessShoeMakerDataList(dataList: List<ProcessShoeMakerData>): Boolean
    fun getProcessShoeMakerDataByPhone(phoneNumber: String): Flow<List<ProcessShoeMakerData>>
    suspend fun addPreparedTailData(data: PreparedTailData)
    suspend fun deleteProcessShoeMakerData(objectName: String): Boolean
    suspend fun deletePreparedTailData(objectName: String): Boolean
    suspend fun addPreparedShoeMakerData(data: PreparedShoeMakerData)
    suspend fun addFinishedShoeMake(data: FinishedShoeMakeData)

}