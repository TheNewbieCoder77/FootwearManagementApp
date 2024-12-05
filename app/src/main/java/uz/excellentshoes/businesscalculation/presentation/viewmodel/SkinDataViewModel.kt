package uz.excellentshoes.businesscalculation.presentation.viewmodel

import androidx.lifecycle.LiveData
import kotlinx.coroutines.flow.StateFlow
import uz.excellentshoes.businesscalculation.data.types.FinishedSkinCutData
import uz.excellentshoes.businesscalculation.data.types.ShoeColorData
import uz.excellentshoes.businesscalculation.data.types.ShoeModelData
import uz.excellentshoes.businesscalculation.data.types.SkinData
import uz.excellentshoes.businesscalculation.data.types.SkinTypeData

interface SkinDataViewModel {
    //AddSkinCutScreen
    val addSkinDataResult: StateFlow<Boolean?>
    val progressBarLiveData: LiveData<Unit>


    fun addSkinData(skinData: SkinData)
    fun addFinishedSkinCut(data: FinishedSkinCutData)

    //SkinCutterScreen
    val openNextScreenLiveData: LiveData<Unit>
    val skinDataFlow: StateFlow<List<SkinData>>
    val deleteSkinDataResult: StateFlow<Boolean>
    val colorsStateFlow: StateFlow<List<ShoeColorData>>
    val modelsStateFlow: StateFlow<List<ShoeModelData>>
    val skinTypesStateFlow: StateFlow<List<SkinTypeData>>


    fun openAddSkinCutScreen()
    fun getSkinDataList()
    fun deleteSkinData(objectName: String)
    fun getColors()
    fun getModels()
    fun getSkinTypes()

}