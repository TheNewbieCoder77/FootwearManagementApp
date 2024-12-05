package uz.excellentshoes.businesscalculation.presentation.viewmodel.impl

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import uz.excellentshoes.businesscalculation.data.types.FinishedShoeMakeData
import uz.excellentshoes.businesscalculation.domain.impl.CommonFinishedWorksRepositoryImpl
import uz.excellentshoes.businesscalculation.presentation.viewmodel.FinishedShoeMakeViewModel

class FinishedShoeMakeViewModelImpl: ViewModel(), FinishedShoeMakeViewModel {
    override val finishedShoeMakeDataStateFlow = MutableStateFlow<List<FinishedShoeMakeData>>(emptyList())
    override val progressBarLiveData = MutableLiveData<Boolean>()
    private val commonRepository = CommonFinishedWorksRepositoryImpl.getInstance()

    override fun getDataList() {
        progressBarLiveData.value = true
        viewModelScope.launch {
            commonRepository.getALlFinishedShoeMake().collect {
                finishedShoeMakeDataStateFlow.value = it
                progressBarLiveData.postValue(false)
            }
        }
    }

    override fun editData(data: FinishedShoeMakeData) {
        progressBarLiveData.value = true
        viewModelScope.launch {
            commonRepository.editFinishedShoeMae(data)
            progressBarLiveData.postValue(false)
        }
    }


}