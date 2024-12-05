package uz.excellentshoes.businesscalculation.presentation.viewmodel.impl

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import uz.excellentshoes.businesscalculation.data.types.KontragentData
import uz.excellentshoes.businesscalculation.data.types.MoneySpendData
import uz.excellentshoes.businesscalculation.domain.impl.CommonMoneySpenderRepositoryImpl
import uz.excellentshoes.businesscalculation.presentation.viewmodel.CommonMoneySpenderViewModel

class CommonMoneySpenderViewModelImpl: ViewModel(), CommonMoneySpenderViewModel {
    override val progressBarLiveData = MutableLiveData<Boolean>()
    override val addMoneySpendDataStateFlow = MutableStateFlow<Boolean>(false)
    override val editMoneySpendDataStateFlow = MutableStateFlow<Boolean>(false)
    override val moneySpendDataListStateFlow = MutableStateFlow<List<MoneySpendData>>(emptyList())
    override val paidAmountStateFlow = MutableStateFlow(0.0)
    override val unPaidAmountStateFlow = MutableStateFlow(0.0)
    override val receiveAmountStateFlow = MutableStateFlow(0.0)
    override val unReceiveAmountStateFlow = MutableStateFlow(0.0)
    override val kontragentListStateFlow = MutableStateFlow<List<KontragentData>>(emptyList())
    val kontragentList = kontragentListStateFlow.asStateFlow()
    private val commonRepository = CommonMoneySpenderRepositoryImpl.getInstance()

    init {
        loadingInitialPaidAmount()
        loadingInitialUnPaidAmount()
        loadingInitialReceiveAmount()
        loadingInitialUnReceiveAmount()
        observePaidAmount()
        observeUnPaidAmount()
        observeReceiveAmount()
        observeUnReceiveAmount()
    }

    override fun getAllMoneyDataList() {
        progressBarLiveData.value = true
        viewModelScope.launch {
            commonRepository.getAllMoneySpendData().collect{ dataList->
                moneySpendDataListStateFlow.value = dataList
                progressBarLiveData.postValue(false)
            }
        }
    }

    override fun addMoneySpendData(data: MoneySpendData) {
        viewModelScope.launch {
            val state = commonRepository.addMoneySpendData(data)
            addMoneySpendDataStateFlow.value = state
        }
    }

    override fun editMoneySpendData(data: MoneySpendData) {
        viewModelScope.launch {
            val state = commonRepository.editMoneySpendData(data)
            editMoneySpendDataStateFlow.value = state
        }
    }

    override fun deleteMoneySpendData(objectName: String) {
        viewModelScope.launch {
            commonRepository.deleteMoneySpendData(objectName)
        }
    }

    override fun getAllKontragent() {
        viewModelScope.launch {
            commonRepository.getAllKontragent().collect{ data->
                kontragentListStateFlow.value = data
            }
        }
    }

    private fun loadingInitialPaidAmount(){
        commonRepository.getInitialPaidAmount()
            .addOnSuccessListener { data->
                paidAmountStateFlow.value = data
            }
    }

    private fun loadingInitialUnPaidAmount(){
        commonRepository.getInitialUnpaidAmount()
            .addOnSuccessListener { data->
                unPaidAmountStateFlow.value = data
            }
    }

    private fun loadingInitialReceiveAmount(){
        commonRepository.getInitialReceiveAmount()
            .addOnSuccessListener { data->
                receiveAmountStateFlow.value = data
            }
    }

    private fun loadingInitialUnReceiveAmount(){
        commonRepository.getInitialUnReceiveAmount()
            .addOnSuccessListener { data->
                unReceiveAmountStateFlow.value = data
            }
    }

    private fun observePaidAmount(){
        viewModelScope.launch {
            commonRepository.observePaidAmount()
                .collect{ amount->
                    paidAmountStateFlow.value = amount
                }
        }
    }

    private fun observeUnPaidAmount(){
        viewModelScope.launch {
            commonRepository.observerUnpaidAmount()
                .collect{ amount->
                    unPaidAmountStateFlow.value = amount
                }
        }
    }

    private fun observeReceiveAmount(){
        viewModelScope.launch {
            commonRepository.observeReceiveAmount()
                .collect{ amount->
                    receiveAmountStateFlow.value = amount
                }
        }
    }

    private fun observeUnReceiveAmount(){
        viewModelScope.launch {
            commonRepository.observeUnReceiveAmount()
                .collect{ amount->
                    unReceiveAmountStateFlow.value = amount
                }
        }
    }

}