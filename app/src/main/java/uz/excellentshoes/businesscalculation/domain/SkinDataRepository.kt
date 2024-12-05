package uz.excellentshoes.businesscalculation.domain

import kotlinx.coroutines.flow.Flow
import uz.excellentshoes.businesscalculation.data.types.FinishedSkinCutData
import uz.excellentshoes.businesscalculation.data.types.ShoeColorData
import uz.excellentshoes.businesscalculation.data.types.ShoeModelData
import uz.excellentshoes.businesscalculation.data.types.SkinData
import uz.excellentshoes.businesscalculation.data.types.SkinTypeData

interface SkinDataRepository {
    suspend fun addSkinData(skinData: SkinData): Boolean
    suspend fun addFinishedSkinCut(data: FinishedSkinCutData)
    suspend fun deleteSkinData(objectName: String): Boolean
    fun getAllSkinData(): Flow<List<SkinData>>

    fun getAllColors(): Flow<List<ShoeColorData>>
    fun getAllModels(): Flow<List<ShoeModelData>>
    fun getAllTypes(): Flow<List<SkinTypeData>>

}