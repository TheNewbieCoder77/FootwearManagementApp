package uz.excellentshoes.businesscalculation.presentation.viewmodel

import kotlinx.coroutines.flow.StateFlow
import uz.excellentshoes.businesscalculation.data.types.SkinTypeData

interface SkinTypeViewModel {
    val skinTypeListStateFlow: StateFlow<List<SkinTypeData>>

    fun getAllSkinTypes()
    fun addSkinType(data: SkinTypeData)
    fun deleteSkinType(objectName: String)
}