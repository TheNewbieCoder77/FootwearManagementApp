package uz.excellentshoes.businesscalculation.presentation.viewmodel.impl

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import uz.excellentshoes.businesscalculation.data.types.FinishedSkinCutData
import uz.excellentshoes.businesscalculation.data.types.ShoeColorData
import uz.excellentshoes.businesscalculation.data.types.ShoeModelData
import uz.excellentshoes.businesscalculation.data.types.SkinData
import uz.excellentshoes.businesscalculation.data.types.SkinTypeData
import uz.excellentshoes.businesscalculation.domain.impl.SkinDataRepositoryImpl
import uz.excellentshoes.businesscalculation.presentation.viewmodel.SkinDataViewModel

class SkinDataViewModelImpl : ViewModel(), SkinDataViewModel {
    private val repository = SkinDataRepositoryImpl.getInstance()
    override val addSkinDataResult = MutableStateFlow<Boolean?>(null)
    override val progressBarLiveData = MutableLiveData<Unit>()
    override val openNextScreenLiveData = MutableLiveData<Unit>()
    override val skinDataFlow = MutableStateFlow<List<SkinData>>(emptyList())
    override val deleteSkinDataResult = MutableStateFlow<Boolean>(false)
    override val colorsStateFlow = MutableStateFlow<List<ShoeColorData>>(emptyList())
    override val modelsStateFlow = MutableStateFlow<List<ShoeModelData>>(emptyList())
    override val skinTypesStateFlow = MutableStateFlow<List<SkinTypeData>>(emptyList())


    override fun addSkinData(skinData: SkinData) {
        viewModelScope.launch {
            progressBarLiveData.value = Unit
            val result = repository.addSkinData(skinData)
            addSkinDataResult.value = result
        }
    }

    override fun addFinishedSkinCut(data: FinishedSkinCutData) {
        viewModelScope.launch {
            repository.addFinishedSkinCut(data)
        }
    }


    override fun openAddSkinCutScreen() {
        openNextScreenLiveData.value = Unit
    }

    override fun getSkinDataList() {
        viewModelScope.launch {
            repository.getAllSkinData().collect{ list->
                skinDataFlow.value = list
            }
        }
    }

    override fun deleteSkinData(objectName: String) {
        viewModelScope.launch {
            val result = repository.deleteSkinData(objectName)
            deleteSkinDataResult.value = result
        }
    }

    override fun getColors() {
        viewModelScope.launch {
            repository.getAllColors().collect{ list->
                colorsStateFlow.value = list
            }
        }
    }

    override fun getModels() {
        viewModelScope.launch {
            repository.getAllModels().collect{ list->
                modelsStateFlow.value = list
            }
        }
    }


    override fun getSkinTypes() {
        viewModelScope.launch {
            repository.getAllTypes().collect{ list->
                skinTypesStateFlow.value = list
            }
        }
    }


}