package uz.excellentshoes.businesscalculation.presentation.viewmodel.impl

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import uz.excellentshoes.businesscalculation.data.types.DeclinedShoeData
import uz.excellentshoes.businesscalculation.data.types.FinishedShoeCheckData
import uz.excellentshoes.businesscalculation.data.types.PreparedShoeMakerData
import uz.excellentshoes.businesscalculation.data.types.PreparedToSailData
import uz.excellentshoes.businesscalculation.domain.impl.CommonShoeCheckerRepositoryImpl
import uz.excellentshoes.businesscalculation.presentation.viewmodel.CommonShoeCheckerViewModel

class CommonShoeCheckerViewModelImpl(private val currentDate: String) : ViewModel(), CommonShoeCheckerViewModel {
    override val progressBarShoeCheckerLiveData = MutableLiveData<Boolean>()
    override val preparedShoeMakerDataListStateFlow = MutableStateFlow<List<PreparedShoeMakerData>>(emptyList())
    override val shoeCountStateFlow = MutableStateFlow<Int?>(null)
    override val finishedShoeCountStateFlow = MutableStateFlow<Int?>(null)
    override val progressBarDeclinedShoeLiveData = MutableLiveData<Boolean>()
    override val declinedShoeDataListStateFlow = MutableStateFlow<List<DeclinedShoeData>>(emptyList())
    private val commonRepository = CommonShoeCheckerRepositoryImpl.getInstance()
    private var counter = -1

    init {
        loadingInitialShoeCount()
        loadingInitialFinishedShoeCount()
        observeShoeCount()
        observeFinishedShoeCount()
    }

    private fun loadingInitialShoeCount(){
        commonRepository.getInitialShoeCount(currentDate)
            .addOnSuccessListener { initialCount->
                shoeCountStateFlow.value = initialCount
            }
    }
    private fun loadingInitialFinishedShoeCount(){
        commonRepository.getInitialFinishedShoeCount(currentDate)
            .addOnSuccessListener { initialFinishedCount->
                finishedShoeCountStateFlow.value = initialFinishedCount
            }
    }

    private fun observeShoeCount(){
        viewModelScope.launch {
            commonRepository.observeShoeCount(currentDate)
                .collect { newCount->
                    if(counter > 0 && newCount == 0) shoeCountStateFlow.value = counter
                    else shoeCountStateFlow.value = newCount
                }
        }
    }

    private fun observeFinishedShoeCount(){
        viewModelScope.launch {
            commonRepository.observeFinishedShoeCount(currentDate)
                .collect{ newFinishedCount->
                    finishedShoeCountStateFlow.value = newFinishedCount
                }
        }
    }

    override fun getAllPreparedShoeMakerData() {
        progressBarShoeCheckerLiveData.value = true
        viewModelScope.launch {
            commonRepository.getAllPreparedShoeMakerData().collect{ dataList->
                counter = dataList.size
                preparedShoeMakerDataListStateFlow.value = dataList
                progressBarShoeCheckerLiveData.postValue(false)
            }
        }
    }

    override fun addDeclinedShoeData(data: DeclinedShoeData) {
        viewModelScope.launch {
            commonRepository.addDeclinedShoeData(data)
        }
    }

    override fun deletePreparedShoeMakerData(objectName: String) {
        viewModelScope.launch {
            commonRepository.deletePreparedShoeMakerData(objectName)
        }
    }

    override fun addShoeSellerData(data: PreparedToSailData) {
        viewModelScope.launch {
            commonRepository.addShoeSellerData(data)
        }
    }

    override fun addFinishedShoeCheck(data: FinishedShoeCheckData) {
        viewModelScope.launch {
            commonRepository.addFinishedShoeCheck(data)
        }
    }

    override fun showShoeCheckerProgressBar(state: Boolean) {
        progressBarShoeCheckerLiveData.value = state
    }


    override fun getAllDeclinedShoeData() {
        progressBarDeclinedShoeLiveData.value = true
        viewModelScope.launch {
            commonRepository.getAllDeclinedShoeData().collect { list ->
                declinedShoeDataListStateFlow.value = list
                progressBarDeclinedShoeLiveData.postValue(false)
            }
        }
    }

    override fun deleteDeclinedShoeData(objectName: String) {
        viewModelScope.launch {
            commonRepository.deleteDeclinedShoeData(objectName)
        }
    }

    override fun addPreparedShoeDataList(dataList: List<PreparedShoeMakerData>) {
        progressBarDeclinedShoeLiveData.value = true
        viewModelScope.launch {
            commonRepository.addPreparedShoeMakeList(dataList)
            progressBarDeclinedShoeLiveData.postValue(false)
        }
    }

}