package uz.excellentshoes.businesscalculation.presentation.viewmodel

import androidx.lifecycle.LiveData
import kotlinx.coroutines.flow.StateFlow
import uz.excellentshoes.businesscalculation.data.types.CategoryItemData
import uz.excellentshoes.businesscalculation.data.types.DollarCurrencyData
import uz.excellentshoes.businesscalculation.data.types.PhoneData

interface AdminCategoryViewModel {
    val progressBarLiveData: LiveData<Boolean>
    val categoryDataListStateFlow: StateFlow<List<CategoryItemData>>

    fun getAllCategories()
    fun addCategory(data: CategoryItemData)
    fun deleteCategory(objectName: String)
    fun addDollarCurrency(data: DollarCurrencyData)
    fun addPhoneData(phoneData: PhoneData)
}