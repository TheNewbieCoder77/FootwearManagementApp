package uz.excellentshoes.businesscalculation.presentation.viewmodel

import androidx.lifecycle.LiveData
import kotlinx.coroutines.flow.StateFlow
import uz.excellentshoes.businesscalculation.data.types.FinishedSkinTailData
import uz.excellentshoes.businesscalculation.data.types.PreparedTailData
import uz.excellentshoes.businesscalculation.data.types.ProcessTailData
import uz.excellentshoes.businesscalculation.data.types.SkinData

interface SkinTailorViewModel {

    //AddSkinTailorScreen
    val progressBarLiveData: LiveData<Unit>
    val addProcessResult: LiveData<Boolean>

    fun addProcessDataList(processTailDataList: List<ProcessTailData>)
    fun addFinishedSkinTail(data: FinishedSkinTailData)

    //SkinTailorScreen
    val processTailDataList: StateFlow<List<ProcessTailData>>
    val openAddSkinTailorLiveData: LiveData<Unit>
    val skinDataFlow: StateFlow<List<SkinData>>
    val deleteSkinDataResult: StateFlow<Unit?>
    val progressBarSecondLiveData: LiveData<Boolean>

    fun openAddSkinTailorScreen()
    fun fetchProcessDataByPhoneNumber(phoneNumber: String)
    fun getSkinDataList()
    fun deleteSkinData(objectName: String)
    fun deleteProcessData(objectName: String)
    fun addSkinData(skinData: SkinData)
    fun addPreparedTailData(preparedTailData: PreparedTailData)
    fun showProgressBar(state: Boolean)
}