package uz.excellentshoes.businesscalculation.presentation.viewmodel

import androidx.lifecycle.LiveData
import kotlinx.coroutines.flow.StateFlow
import uz.excellentshoes.businesscalculation.data.types.KontragentData
import uz.excellentshoes.businesscalculation.data.types.MoneySpendData

interface CommonMoneySpenderViewModel {
    val progressBarLiveData: LiveData<Boolean>
    val addMoneySpendDataStateFlow: StateFlow<Boolean>
    val editMoneySpendDataStateFlow: StateFlow<Boolean>
    val moneySpendDataListStateFlow: StateFlow<List<MoneySpendData>>
    val paidAmountStateFlow: StateFlow<Double>
    val unPaidAmountStateFlow: StateFlow<Double>
    val receiveAmountStateFlow: StateFlow<Double>
    val unReceiveAmountStateFlow: StateFlow<Double>
    val kontragentListStateFlow: StateFlow<List<KontragentData>>


    fun getAllMoneyDataList()
    fun addMoneySpendData(data: MoneySpendData)
    fun editMoneySpendData(data: MoneySpendData)
    fun deleteMoneySpendData(objectName: String)
    fun getAllKontragent()

}