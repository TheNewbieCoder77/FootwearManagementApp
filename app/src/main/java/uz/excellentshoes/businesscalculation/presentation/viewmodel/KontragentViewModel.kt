package uz.excellentshoes.businesscalculation.presentation.viewmodel

import kotlinx.coroutines.flow.StateFlow
import uz.excellentshoes.businesscalculation.data.types.KontragentData

interface KontragentViewModel {
    val kontragentListStateFlow: StateFlow<List<KontragentData>>

    fun getAllKontragent()
    fun addKontragent(data: KontragentData)
    fun deleteKontragent(objectName: String)
}