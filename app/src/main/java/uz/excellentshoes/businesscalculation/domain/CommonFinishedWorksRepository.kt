package uz.excellentshoes.businesscalculation.domain

import kotlinx.coroutines.flow.Flow
import uz.excellentshoes.businesscalculation.data.types.FinishedShoeMakeData
import uz.excellentshoes.businesscalculation.data.types.FinishedSkinCutData
import uz.excellentshoes.businesscalculation.data.types.FinishedSkinTailData

interface CommonFinishedWorksRepository {
    fun getALlFinishedShoeMake(): Flow<List<FinishedShoeMakeData>>
    suspend fun editFinishedShoeMae(data: FinishedShoeMakeData)

    fun getAllFinishedSkinTail(): Flow<List<FinishedSkinTailData>>
    suspend fun editFinishedSkinTail(data: FinishedSkinTailData)

    fun getAllFinishedSkinCut(): Flow<List<FinishedSkinCutData>>
    suspend fun editFinishedSkinCut(data: FinishedSkinCutData)

}