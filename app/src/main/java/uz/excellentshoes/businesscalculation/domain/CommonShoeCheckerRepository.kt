package uz.excellentshoes.businesscalculation.domain

import com.google.android.gms.tasks.Task
import kotlinx.coroutines.flow.Flow
import uz.excellentshoes.businesscalculation.data.types.DeclinedShoeData
import uz.excellentshoes.businesscalculation.data.types.FinishedShoeCheckData
import uz.excellentshoes.businesscalculation.data.types.PreparedShoeMakerData
import uz.excellentshoes.businesscalculation.data.types.PreparedToSailData

interface CommonShoeCheckerRepository {
    fun getAllPreparedShoeMakerData(): Flow<List<PreparedShoeMakerData>>
    suspend fun addDeclinedShoeData(data: DeclinedShoeData)
    fun getAllDeclinedShoeData(): Flow<List<DeclinedShoeData>>
    suspend fun deletePreparedShoeMakerData(objectName: String): Boolean
    suspend fun deleteDeclinedShoeData(objectName: String): Boolean
    suspend fun addShoeSellerData(data: PreparedToSailData)
    suspend fun addFinishedShoeCheck(data: FinishedShoeCheckData)
    suspend fun addPreparedShoeMakeList(dataList: List<PreparedShoeMakerData>)

    fun getInitialShoeCount(currentDate: String): Task<Int>
    fun getInitialFinishedShoeCount(currentDate: String): Task<Int>
    fun observeShoeCount(currentDate: String): Flow<Int>
    fun observeFinishedShoeCount(currentDate: String): Flow<Int>
}