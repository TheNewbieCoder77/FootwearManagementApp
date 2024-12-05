package uz.excellentshoes.businesscalculation.domain

import com.google.android.gms.tasks.Task
import kotlinx.coroutines.flow.Flow
import uz.excellentshoes.businesscalculation.data.types.KontragentData
import uz.excellentshoes.businesscalculation.data.types.MoneySpendData

interface CommonMoneySpenderRepository {
    suspend fun addMoneySpendData(data: MoneySpendData): Boolean
    fun getAllMoneySpendData(): Flow<List<MoneySpendData>>
    suspend fun deleteMoneySpendData(objectName: String)
    suspend fun editMoneySpendData(data: MoneySpendData): Boolean
    fun getAllKontragent(): Flow<List<KontragentData>>

    fun getInitialPaidAmount(): Task<Double>
    fun getInitialUnpaidAmount(): Task<Double>
    fun getInitialReceiveAmount(): Task<Double>
    fun getInitialUnReceiveAmount(): Task<Double>

    fun observePaidAmount(): Flow<Double>
    fun observerUnpaidAmount(): Flow<Double>
    fun observeReceiveAmount(): Flow<Double>
    fun observeUnReceiveAmount(): Flow<Double>
}