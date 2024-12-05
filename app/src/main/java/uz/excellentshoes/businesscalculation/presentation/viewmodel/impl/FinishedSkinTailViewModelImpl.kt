package uz.excellentshoes.businesscalculation.presentation.viewmodel.impl

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import uz.excellentshoes.businesscalculation.data.types.FinishedSkinTailData
import uz.excellentshoes.businesscalculation.domain.impl.CommonFinishedWorksRepositoryImpl
import uz.excellentshoes.businesscalculation.presentation.viewmodel.FinishedSkinTailViewModel

class FinishedSkinTailViewModelImpl : ViewModel(), FinishedSkinTailViewModel {
    override val finishedSkinTailDataStateFlow = MutableStateFlow<List<FinishedSkinTailData>>(
        emptyList()
    )
    override val progressBarLiveData = MutableLiveData<Boolean>()
    private val commonRepository = CommonFinishedWorksRepositoryImpl.getInstance()

    override fun getDataList() {
        progressBarLiveData.value = true
        viewModelScope.launch {
            commonRepository.getAllFinishedSkinTail().collect{
                finishedSkinTailDataStateFlow.value = it
                progressBarLiveData.postValue(false)
            }
        }
    }

    override fun editData(data: FinishedSkinTailData) {
        progressBarLiveData.value = true
        viewModelScope.launch {
            commonRepository.editFinishedSkinTail(data)
            progressBarLiveData.postValue(false)
        }
    }


}