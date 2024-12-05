package uz.excellentshoes.businesscalculation.presentation.viewmodel

import androidx.lifecycle.LiveData
import kotlinx.coroutines.flow.StateFlow
import uz.excellentshoes.businesscalculation.data.types.PreparedToSailData
import uz.excellentshoes.businesscalculation.data.types.SoldShoeData

interface ShoeSellerViewModel {
    val progressBarLiveData: LiveData<Boolean>
    val preparedToSailStateFlow: StateFlow<List<PreparedToSailData>>

    fun addSoldShoeList(dataList: List<SoldShoeData>)
    fun deleteShoeSell(objectName: String)
    fun getPreparedToSailData()
}