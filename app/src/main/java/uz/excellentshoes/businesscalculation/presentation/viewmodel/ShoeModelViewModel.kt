package uz.excellentshoes.businesscalculation.presentation.viewmodel

import kotlinx.coroutines.flow.StateFlow
import uz.excellentshoes.businesscalculation.data.types.ShoeModelData

interface ShoeModelViewModel {
    val shoeModelsListStateFlow: StateFlow<List<ShoeModelData>>

    fun getAllShoeModels()
    fun addShoeModel(data: ShoeModelData)
    fun delete(objectName: String)
}