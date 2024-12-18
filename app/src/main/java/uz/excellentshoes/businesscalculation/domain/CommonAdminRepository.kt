package uz.excellentshoes.businesscalculation.domain

import kotlinx.coroutines.flow.Flow
import uz.excellentshoes.businesscalculation.data.types.CategoryItemData
import uz.excellentshoes.businesscalculation.data.types.DollarCurrencyData
import uz.excellentshoes.businesscalculation.data.types.KontragentData
import uz.excellentshoes.businesscalculation.data.types.PhoneData
import uz.excellentshoes.businesscalculation.data.types.ShoeColorData
import uz.excellentshoes.businesscalculation.data.types.ShoeModelData
import uz.excellentshoes.businesscalculation.data.types.SkinTypeData
import uz.excellentshoes.businesscalculation.data.types.UnitData
import uz.excellentshoes.businesscalculation.data.types.UserPasswordData

interface CommonAdminRepository {
    suspend fun addCategory(data: CategoryItemData)
    suspend fun deleteCategory(objectName: String)
    fun getAllCategories(): Flow<List<CategoryItemData>>

    suspend fun addShoeColor(data: ShoeColorData)
    suspend fun deleteShoeColor(objectName: String)
    fun getAllShoeColors(): Flow<List<ShoeColorData>>


    suspend fun addSkinType(data: SkinTypeData)
    suspend fun deleteSkinType(objectName: String)
    fun getAllSkinTypes(): Flow<List<SkinTypeData>>

    suspend fun addKontragent(data: KontragentData)
    suspend fun deleteKontragent(objectName: String)
    fun getAllKontragent(): Flow<List<KontragentData>>

    suspend fun addShoeModel(data: ShoeModelData)
    suspend fun deleteShoeModel(objectName: String)
    fun getAllShoeModels(): Flow<List<ShoeModelData>>

    suspend fun addDollarCurrency(data: DollarCurrencyData)
    suspend fun addPhoneData(phoneData: PhoneData)
    suspend fun addUnitData(data: UnitData)

    suspend fun upsertUserData(data: UserPasswordData)
    suspend fun deleteUserData(objectName: String)
    fun getAllUserData(): Flow<List<UserPasswordData>>
}