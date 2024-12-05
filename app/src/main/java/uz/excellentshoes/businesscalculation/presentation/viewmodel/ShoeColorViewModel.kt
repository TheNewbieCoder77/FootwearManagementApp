package uz.excellentshoes.businesscalculation.presentation.viewmodel

import kotlinx.coroutines.flow.StateFlow
import uz.excellentshoes.businesscalculation.data.types.ShoeColorData

interface ShoeColorViewModel {
    val shoeColorListStateFlow: StateFlow<List<ShoeColorData>>

    fun getAllShoeColors()
    fun addShoeColor(data: ShoeColorData)
    fun deleteShoeColor(objectName: String)
}