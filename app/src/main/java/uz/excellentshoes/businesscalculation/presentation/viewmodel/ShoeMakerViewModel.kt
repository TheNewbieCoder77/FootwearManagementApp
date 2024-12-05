package uz.excellentshoes.businesscalculation.presentation.viewmodel

import androidx.lifecycle.LiveData
import kotlinx.coroutines.flow.StateFlow
import uz.excellentshoes.businesscalculation.data.types.FinishedShoeMakeData
import uz.excellentshoes.businesscalculation.data.types.PreparedShoeMakerData
import uz.excellentshoes.businesscalculation.data.types.PreparedTailData
import uz.excellentshoes.businesscalculation.data.types.ProcessShoeMakerData
import uz.excellentshoes.businesscalculation.data.types.ProcessTailData

interface ShoeMakerViewModel {
    //ScreenShoeMaker
    val processShoeMakerDataList: StateFlow<List<ProcessShoeMakerData>>
    val openAddShoeMakerLiveData: LiveData<Unit>
    val preparedTailDataFlow: StateFlow<List<PreparedTailData>>
    val deletePreparedTailDataResult: StateFlow<Unit?>
    val progressBarSecondLiveData: LiveData<Boolean>

    fun openAddShoeMakerScreen()
    fun fetchProcessShoeMakerDataByPhoneNumber(phoneNumber: String)
    fun getPreparedTailDataList()
    fun deletePreparedTailData(objectName: String)
    fun deleteProcessShoeMakerData(objectName: String)
    fun addPreparedTailData(data: PreparedTailData)
    fun addPreparedShoeMakerData(data: PreparedShoeMakerData)
    fun addFinishedShoeMake(data: FinishedShoeMakeData)
    fun showProgressBar(state: Boolean)

    //AddShoeMakerScreen
    val progressBarLiveData: LiveData<Unit>
    val addProcessShoeMakerDataResult: LiveData<Boolean>

    fun addProcessShoeMakerDataList(data: List<ProcessShoeMakerData>)
}