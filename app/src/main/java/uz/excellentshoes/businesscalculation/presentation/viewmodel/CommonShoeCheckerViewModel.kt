package uz.excellentshoes.businesscalculation.presentation.viewmodel

import androidx.lifecycle.LiveData
import kotlinx.coroutines.flow.StateFlow
import uz.excellentshoes.businesscalculation.data.types.DeclinedShoeData
import uz.excellentshoes.businesscalculation.data.types.FinishedShoeCheckData
import uz.excellentshoes.businesscalculation.data.types.PreparedShoeMakerData
import uz.excellentshoes.businesscalculation.data.types.PreparedToSailData

interface CommonShoeCheckerViewModel {
    //ShoeCheckerScreen
    val progressBarShoeCheckerLiveData: LiveData<Boolean>
    val preparedShoeMakerDataListStateFlow: StateFlow<List<PreparedShoeMakerData>>
    val shoeCountStateFlow: StateFlow<Int?>
    val finishedShoeCountStateFlow: StateFlow<Int?>

    fun getAllPreparedShoeMakerData()
    fun addDeclinedShoeData(data: DeclinedShoeData)
    fun deletePreparedShoeMakerData(objectName: String)
    fun addShoeSellerData(data: PreparedToSailData)
    fun addFinishedShoeCheck(data: FinishedShoeCheckData)
    fun showShoeCheckerProgressBar(state: Boolean)

    //DeclinedShoeScreen
    val progressBarDeclinedShoeLiveData: LiveData<Boolean>
    val declinedShoeDataListStateFlow: StateFlow<List<DeclinedShoeData>>

    fun getAllDeclinedShoeData()
    fun deleteDeclinedShoeData(objectName: String)
    fun addPreparedShoeDataList(dataList: List<PreparedShoeMakerData>)
}