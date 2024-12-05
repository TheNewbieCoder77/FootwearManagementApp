package uz.excellentshoes.businesscalculation.presentation.viewmodel.impl


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import uz.excellentshoes.businesscalculation.data.types.FinishedShoeMakeData
import uz.excellentshoes.businesscalculation.data.types.PreparedShoeMakerData
import uz.excellentshoes.businesscalculation.data.types.PreparedTailData
import uz.excellentshoes.businesscalculation.data.types.ProcessShoeMakerData
import uz.excellentshoes.businesscalculation.domain.impl.CommonShoeMakerRepositoryImpl
import uz.excellentshoes.businesscalculation.presentation.viewmodel.ShoeMakerViewModel

class ShoeMakerViewModelImpl : ViewModel(), ShoeMakerViewModel {
    override val processShoeMakerDataList = MutableStateFlow<List<ProcessShoeMakerData>>(emptyList())
    override val openAddShoeMakerLiveData = MutableLiveData<Unit>()
    override val preparedTailDataFlow = MutableStateFlow<List<PreparedTailData>>(emptyList())
    override val deletePreparedTailDataResult = MutableStateFlow<Unit?>(null)
    override val progressBarSecondLiveData = MutableLiveData<Boolean>()
    override val progressBarLiveData = MutableLiveData<Unit>()
    override val addProcessShoeMakerDataResult = MutableLiveData<Boolean>()
    private val commonRepository = CommonShoeMakerRepositoryImpl.getInstance()

    override fun openAddShoeMakerScreen() {
        openAddShoeMakerLiveData.value = Unit
    }

    override fun fetchProcessShoeMakerDataByPhoneNumber(phoneNumber: String) {
        progressBarSecondLiveData.value = true
        viewModelScope.launch {
            commonRepository.getProcessShoeMakerDataByPhone(phoneNumber).collect { dataList ->
                processShoeMakerDataList.value = dataList
                progressBarSecondLiveData.postValue(false)
            }
        }
    }

    override fun getPreparedTailDataList() {
        viewModelScope.launch {
            commonRepository.getAllPreparedTailData().collect{ list->
                preparedTailDataFlow.value = list
            }
        }
    }

    override fun deletePreparedTailData(objectName: String) {
        viewModelScope.launch {
            val result = commonRepository.deletePreparedTailData(objectName)
            when(result){
                true -> deletePreparedTailDataResult.value = Unit
                else -> deletePreparedTailDataResult.value = null
            }
        }
    }

    override fun deleteProcessShoeMakerData(objectName: String) {
        viewModelScope.launch {
            commonRepository.deleteProcessShoeMakerData(objectName)
        }
    }

    override fun addPreparedTailData(data: PreparedTailData) {
        viewModelScope.launch {
            commonRepository.addPreparedTailData(data)
        }
    }

    override fun addPreparedShoeMakerData(data: PreparedShoeMakerData) {
        viewModelScope.launch {
            commonRepository.addPreparedShoeMakerData(data)
        }
    }

    override fun addFinishedShoeMake(data: FinishedShoeMakeData) {
        viewModelScope.launch {
            commonRepository.addFinishedShoeMake(data)
        }
    }

    override fun showProgressBar(state: Boolean) {
        progressBarSecondLiveData.value = state
    }

    override fun addProcessShoeMakerDataList(data: List<ProcessShoeMakerData>) {
        progressBarLiveData.value = Unit
        viewModelScope.launch {
            val result = commonRepository.addProcessShoeMakerDataList(data)
            addProcessShoeMakerDataResult.postValue(result)
        }
    }

}