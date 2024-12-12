package uz.excellentshoes.businesscalculation.presentation.viewmodel

import androidx.lifecycle.LiveData
import kotlinx.coroutines.flow.StateFlow
import uz.excellentshoes.businesscalculation.data.types.SoldShoeData

interface SoldShoesViewModel {
    val progressBarLiveData: LiveData<Boolean>
    val soldShoeStateFlow: StateFlow<List<SoldShoeData>>

    fun getAllSoldShoes()
    fun deleteSoldShoe(objectName: String)

}