package uz.excellentshoes.businesscalculation.presentation.viewmodel

import androidx.lifecycle.LiveData
import kotlinx.coroutines.flow.StateFlow
import uz.excellentshoes.businesscalculation.data.types.FinishedShoeMakeData

interface FinishedShoeMakeViewModel {
    val finishedShoeMakeDataStateFlow: StateFlow<List<FinishedShoeMakeData>>
    val progressBarLiveData: LiveData<Boolean>

    fun getDataList()
    fun editData(data: FinishedShoeMakeData)
}