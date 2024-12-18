package uz.excellentshoes.businesscalculation.presentation.viewmodel.impl

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import uz.excellentshoes.businesscalculation.data.types.CategoryItemData
import uz.excellentshoes.businesscalculation.data.types.DollarCurrencyData
import uz.excellentshoes.businesscalculation.data.types.PhoneData
import uz.excellentshoes.businesscalculation.data.types.UnitData
import uz.excellentshoes.businesscalculation.domain.impl.CommonAdminRepositoryImpl
import uz.excellentshoes.businesscalculation.presentation.viewmodel.AdminCategoryViewModel

class AdminCategoryViewModelImpl : ViewModel(),AdminCategoryViewModel {
    override val progressBarLiveData = MutableLiveData<Boolean>()
    override val categoryDataListStateFlow = MutableStateFlow<List<CategoryItemData>>(emptyList())
    private val commonRepository = CommonAdminRepositoryImpl.getInstance()

    override fun getAllCategories() {
        progressBarLiveData.value = true
        viewModelScope.launch {
            commonRepository.getAllCategories().collect{ dataList->
                categoryDataListStateFlow.value = dataList
                progressBarLiveData.postValue(false)
            }
        }
    }

    override fun addCategory(data: CategoryItemData) {
        viewModelScope.launch {
            commonRepository.addCategory(data)
        }
    }

    override fun deleteCategory(objectName: String) {
        viewModelScope.launch {
            commonRepository.deleteCategory(objectName)
        }
    }

    override fun addDollarCurrency(data: DollarCurrencyData) {
        viewModelScope.launch {
            commonRepository.addDollarCurrency(data)
        }
    }

    override fun addPhoneData(phoneData: PhoneData) {
        viewModelScope.launch {
            commonRepository.addPhoneData(phoneData)
        }
    }

    override fun addUnit(unitData: UnitData) {
        viewModelScope.launch {
            commonRepository.addUnitData(unitData)
        }
    }

}