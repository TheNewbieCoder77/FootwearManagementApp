package uz.excellentshoes.businesscalculation.presentation.viewmodel.impl

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import uz.excellentshoes.businesscalculation.data.types.SoldShoeData
import uz.excellentshoes.businesscalculation.domain.impl.ShoeSellerRepositoryImpl
import uz.excellentshoes.businesscalculation.presentation.viewmodel.SoldShoesViewModel

class SoldShoesViewModelImpl : SoldShoesViewModel, ViewModel() {
    override val progressBarLiveData = MutableLiveData<Boolean>()
    override val soldShoeStateFlow = MutableStateFlow<List<SoldShoeData>>(emptyList())
    private val repository = ShoeSellerRepositoryImpl.getInstance()

    override fun getAllSoldShoes() {
        progressBarLiveData.value = true
        viewModelScope.launch {
            repository.getAllSoldShoeData().collect{
                soldShoeStateFlow.value = it
                progressBarLiveData.postValue(false)
            }
        }
    }

    override fun deleteSoldShoe(objectName: String) {
        viewModelScope.launch {
            repository.deleteSoldShoeData(objectName)
        }
    }

}