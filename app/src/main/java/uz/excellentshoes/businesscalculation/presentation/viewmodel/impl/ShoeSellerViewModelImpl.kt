package uz.excellentshoes.businesscalculation.presentation.viewmodel.impl


import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import uz.excellentshoes.businesscalculation.data.types.PreparedToSailData
import uz.excellentshoes.businesscalculation.data.types.SoldShoeData
import uz.excellentshoes.businesscalculation.domain.impl.ShoeSellerRepositoryImpl
import uz.excellentshoes.businesscalculation.presentation.viewmodel.ShoeSellerViewModel

class ShoeSellerViewModelImpl: ViewModel(), ShoeSellerViewModel {
    override val progressBarLiveData = MutableLiveData<Boolean>()
    override val preparedToSailStateFlow = MutableStateFlow<List<PreparedToSailData>>(emptyList())
    private val repository = ShoeSellerRepositoryImpl.getInstance()

    override fun addSoldShoeList(dataList: List<SoldShoeData>) {
        progressBarLiveData.value = true
        viewModelScope.launch {
            repository.addSoldShoeDataList(dataList)
            progressBarLiveData.postValue(false)
        }
    }

    override fun deleteShoeSell(objectName: String) {
        viewModelScope.launch {
            repository.deletePreparedToSailData(objectName)
        }
    }

    override fun getPreparedToSailData() {
        progressBarLiveData.value = true
        viewModelScope.launch {
            repository.getAllPreparedToSailData().collect{ dataList->
                preparedToSailStateFlow.value = dataList
                progressBarLiveData.postValue(false)
            }
        }
    }

}