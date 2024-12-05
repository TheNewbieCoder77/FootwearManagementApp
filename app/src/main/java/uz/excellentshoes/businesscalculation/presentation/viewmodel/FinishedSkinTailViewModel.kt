package uz.excellentshoes.businesscalculation.presentation.viewmodel

import androidx.lifecycle.LiveData
import kotlinx.coroutines.flow.StateFlow
import uz.excellentshoes.businesscalculation.data.types.FinishedShoeMakeData
import uz.excellentshoes.businesscalculation.data.types.FinishedSkinTailData

interface FinishedSkinTailViewModel {
    val finishedSkinTailDataStateFlow: StateFlow<List<FinishedSkinTailData>>
    val progressBarLiveData: LiveData<Boolean>

    fun getDataList()
    fun editData(data: FinishedSkinTailData)
}