package uz.excellentshoes.businesscalculation.presentation.viewmodel.impl

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import uz.excellentshoes.businesscalculation.data.types.FinishedSkinCutData
import uz.excellentshoes.businesscalculation.domain.impl.CommonFinishedWorksRepositoryImpl
import uz.excellentshoes.businesscalculation.presentation.viewmodel.FinishedSkinCutViewModel

class FinishedSkinCutViewModelImpl : ViewModel(), FinishedSkinCutViewModel {
    override val finishedSkinCutDataStateFlow =
        MutableStateFlow<List<FinishedSkinCutData>>(emptyList())
    override val progressBarLiveData = MutableLiveData<Boolean>()
    private val commonRepository = CommonFinishedWorksRepositoryImpl.getInstance()

    override fun getDataList() {
        progressBarLiveData.value = true
        viewModelScope.launch {
            commonRepository.getAllFinishedSkinCut().collect {
                finishedSkinCutDataStateFlow.value = it
                progressBarLiveData.postValue(false)
            }
        }
    }

    override fun editData(data: FinishedSkinCutData) {
        progressBarLiveData.value = true
        viewModelScope.launch {
            commonRepository.editFinishedSkinCut(data)
            progressBarLiveData.postValue(false)
        }

    }


}