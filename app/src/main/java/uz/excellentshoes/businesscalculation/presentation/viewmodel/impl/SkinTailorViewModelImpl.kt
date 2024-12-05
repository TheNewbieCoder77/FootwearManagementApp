package uz.excellentshoes.businesscalculation.presentation.viewmodel.impl

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import uz.excellentshoes.businesscalculation.data.types.FinishedSkinTailData
import uz.excellentshoes.businesscalculation.data.types.PreparedTailData
import uz.excellentshoes.businesscalculation.data.types.ProcessTailData
import uz.excellentshoes.businesscalculation.data.types.SkinData
import uz.excellentshoes.businesscalculation.domain.impl.SkinDataRepositoryImpl
import uz.excellentshoes.businesscalculation.domain.impl.SkinTailorRepositoryImpl
import uz.excellentshoes.businesscalculation.presentation.viewmodel.SkinTailorViewModel

class SkinTailorViewModelImpl : ViewModel(), SkinTailorViewModel {
    private val repository = SkinTailorRepositoryImpl.getInstance()
    private val skinDataRepository = SkinDataRepositoryImpl.getInstance()
    override val processTailDataList = MutableStateFlow<List<ProcessTailData>>(emptyList())
    override val openAddSkinTailorLiveData = MutableLiveData<Unit>()
    override val progressBarLiveData = MutableLiveData<Unit>()
    override val addProcessResult = MutableLiveData<Boolean>()
    override val skinDataFlow = MutableStateFlow<List<SkinData>>(emptyList())
    override val deleteSkinDataResult = MutableStateFlow<Unit?>(null)
    override val progressBarSecondLiveData = MutableLiveData<Boolean>()

    override fun openAddSkinTailorScreen() {
        openAddSkinTailorLiveData.value = Unit
    }


    override fun addProcessDataList(processTailDataList: List<ProcessTailData>) {
        progressBarLiveData.value = Unit
        viewModelScope.launch {
            val result = repository.addProcessDataList(processTailDataList)
            addProcessResult.postValue(result)
        }
    }

    override fun addFinishedSkinTail(data: FinishedSkinTailData) {
        viewModelScope.launch {
            repository.addFinishedSkinTail(data)
        }
    }

    override fun fetchProcessDataByPhoneNumber(phoneNumber: String) {
        progressBarSecondLiveData.value = true
        viewModelScope.launch {
            repository.getProcessDataByPhoneNumber(phoneNumber).collect { dataList ->
                processTailDataList.value = dataList
                progressBarSecondLiveData.postValue(false)
            }
        }
    }

    override fun getSkinDataList() {
        viewModelScope.launch {
            skinDataRepository.getAllSkinData().collect{ list->
                skinDataFlow.value = list
            }
        }
    }

    override fun deleteSkinData(objectName: String) {
        viewModelScope.launch {
            val result = skinDataRepository.deleteSkinData(objectName)
            when(result){
                true -> deleteSkinDataResult.value = Unit
                else -> deleteSkinDataResult.value = null
            }
        }
    }

    override fun deleteProcessData(objectName: String) {
        viewModelScope.launch {
            repository.deleteProcessData(objectName)
        }
    }

    override fun addSkinData(skinData: SkinData) {
        viewModelScope.launch {
            skinDataRepository.addSkinData(skinData)
        }
    }

    override fun addPreparedTailData(preparedTailData: PreparedTailData) {
        viewModelScope.launch {
            repository.addPreparedTailData(preparedTailData)
        }
    }

    override fun showProgressBar(state: Boolean) {
        progressBarSecondLiveData.value = state
    }


}