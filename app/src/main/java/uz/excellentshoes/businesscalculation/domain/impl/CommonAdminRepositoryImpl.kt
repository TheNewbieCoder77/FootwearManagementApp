package uz.excellentshoes.businesscalculation.domain.impl

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.tasks.await
import uz.excellentshoes.businesscalculation.app.App
import uz.excellentshoes.businesscalculation.data.AppPreferences
import uz.excellentshoes.businesscalculation.data.types.CategoryItemData
import uz.excellentshoes.businesscalculation.data.types.DollarCurrencyData
import uz.excellentshoes.businesscalculation.data.types.KontragentData
import uz.excellentshoes.businesscalculation.data.types.PhoneData
import uz.excellentshoes.businesscalculation.data.types.ShoeColorData
import uz.excellentshoes.businesscalculation.data.types.ShoeModelData
import uz.excellentshoes.businesscalculation.data.types.SkinTypeData
import uz.excellentshoes.businesscalculation.data.types.UserPasswordData
import uz.excellentshoes.businesscalculation.domain.CommonAdminRepository

class CommonAdminRepositoryImpl private constructor(): CommonAdminRepository {
    private val databaseReferenceCategoryItem = App.database.getReference("CategoryItemData")
    private val databaseReferenceShoeColorData = App.database.getReference("ShoeColorData")
    private val databaseReferenceSkinTypeData = App.database.getReference("SkinTypeData")
    private val databaseReferenceShoeModelData = App.database.getReference("ShoeModelData")
    private val databaseReferenceKontragentData = App.database.getReference("KontragentData")
    private val databaseReferenceDollarCurrencyData = App.database.getReference("DollarCurrencyData")
    private val databaseReferencePhoneData = App.database.getReference("PhoneData")
    private val databaseReferenceUserPasswordData = App.database.getReference("UserPasswordData")
    private val appPreferences = AppPreferences.getInstance()



    companion object{
        private lateinit var instance: CommonAdminRepository

        fun getInstance(): CommonAdminRepository{
            if(!::instance.isInitialized){
                instance = CommonAdminRepositoryImpl()
            }
            return instance
        }
    }

    override suspend fun addCategory(data: CategoryItemData) {
        val newReference = databaseReferenceCategoryItem.push()
        val objectName = newReference.key ?: return
        data.objectName = objectName
        newReference.setValue(data).await()
    }

    override suspend fun deleteCategory(objectName: String) {
        val objectRef = databaseReferenceCategoryItem.child(objectName)
        objectRef.removeValue().await()
    }

    override fun getAllCategories(): Flow<List<CategoryItemData>> = callbackFlow{
        val categoryDataListener = object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                val categoryDataList = if(snapshot.exists() && snapshot.hasChildren()){
                    snapshot.children.mapNotNull { data->
                        data.getValue(CategoryItemData::class.java)
                    }
                }else{
                    emptyList()
                }
                trySend(categoryDataList)
            }

            override fun onCancelled(error: DatabaseError) {
                trySend(emptyList())
            }

        }
        databaseReferenceCategoryItem.addValueEventListener(categoryDataListener)
        awaitClose {
            databaseReferenceCategoryItem.removeEventListener(categoryDataListener)
        }

    }.flowOn(Dispatchers.IO)

    override suspend fun addShoeColor(data: ShoeColorData) {
        val newReference = databaseReferenceShoeColorData.push()
        val objectName = newReference.key?: return
        data.objectName = objectName
        newReference.setValue(data).await()
    }

    override suspend fun deleteShoeColor(objectName: String) {
        val objectRef = databaseReferenceShoeColorData.child(objectName)
        objectRef.removeValue().await()
    }

    override fun getAllShoeColors(): Flow<List<ShoeColorData>> = callbackFlow {
        val listener = object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                val dataList = if(snapshot.exists() && snapshot.hasChildren()){
                    snapshot.children.mapNotNull { data->
                        data.getValue(ShoeColorData::class.java)
                    }
                }else{
                    emptyList()
                }
                trySend(dataList)
            }

            override fun onCancelled(error: DatabaseError) {
                trySend(emptyList())
            }

        }
        databaseReferenceShoeColorData.addValueEventListener(listener)
        awaitClose {
            databaseReferenceShoeColorData.removeEventListener(listener)
        }
    }.flowOn(Dispatchers.IO)

    override suspend fun addSkinType(data: SkinTypeData) {
        val newReference = databaseReferenceSkinTypeData.push()
        val objectName = newReference.key?: return
        data.objectName = objectName
        newReference.setValue(data).await()
    }

    override suspend fun deleteSkinType(objectName: String) {
        val objectRef = databaseReferenceSkinTypeData.child(objectName)
        objectRef.removeValue().await()
    }

    override fun getAllSkinTypes(): Flow<List<SkinTypeData>> = callbackFlow {
        val listener = object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                val dataList = if(snapshot.exists() && snapshot.hasChildren()){
                    snapshot.children.mapNotNull { data->
                        data.getValue(SkinTypeData::class.java)
                    }
                }else{
                    emptyList()
                }
                trySend(dataList)
            }

            override fun onCancelled(error: DatabaseError) {
                trySend(emptyList())
            }

        }
        databaseReferenceSkinTypeData.addValueEventListener(listener)
        awaitClose {
            databaseReferenceSkinTypeData.removeEventListener(listener)
        }
    }.flowOn(Dispatchers.IO)

    override suspend fun addKontragent(data: KontragentData) {
        val newReference = databaseReferenceKontragentData.push()
        val objectName = newReference.key?: return
        data.objectName = objectName
        newReference.setValue(data).await()
    }

    override suspend fun deleteKontragent(objectName: String) {
        val objectRef = databaseReferenceKontragentData.child(objectName)
        objectRef.removeValue().await()
    }

    override fun getAllKontragent(): Flow<List<KontragentData>> = callbackFlow {
        val listener = object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                val dataList = if(snapshot.exists() && snapshot.hasChildren()){
                    snapshot.children.mapNotNull { data->
                        data.getValue(KontragentData::class.java)
                    }
                }else{
                    emptyList()
                }
                trySend(dataList)
            }

            override fun onCancelled(error: DatabaseError) {
                trySend(emptyList())
            }

        }
        databaseReferenceKontragentData.addValueEventListener(listener)
        awaitClose {
            databaseReferenceKontragentData.removeEventListener(listener)
        }
    }.flowOn(Dispatchers.IO)

    override suspend fun addShoeModel(data: ShoeModelData) {
        val newReference = databaseReferenceShoeModelData.push()
        val objectName = newReference.key?: return
        data.objectName = objectName
        newReference.setValue(data).await()
    }

    override suspend fun deleteShoeModel(objectName: String) {
        val objectRef = databaseReferenceShoeModelData.child(objectName)
        objectRef.removeValue().await()
    }

    override fun getAllShoeModels(): Flow<List<ShoeModelData>> = callbackFlow{
        val listener = object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                val dataList = if(snapshot.exists() && snapshot.hasChildren()){
                    snapshot.children.mapNotNull { data->
                        data.getValue(ShoeModelData::class.java)
                    }
                }else{
                    emptyList()
                }
                trySend(dataList)
            }

            override fun onCancelled(error: DatabaseError) {
                trySend(emptyList())
            }

        }
        databaseReferenceShoeModelData.addValueEventListener(listener)
        awaitClose {
            databaseReferenceShoeModelData.removeEventListener(listener)
        }
    }.flowOn(Dispatchers.IO)


    override suspend fun addDollarCurrency(data: DollarCurrencyData) {
        val snapshot = databaseReferenceDollarCurrencyData
            .get()
            .await()

        if (snapshot.exists() && snapshot.children.count() > 0) {
            val existingData = snapshot.children.first()
            val objectKey = existingData.key ?: return
            data.objectName = objectKey

            databaseReferenceDollarCurrencyData
                .child(objectKey)
                .setValue(data)
                .await()
        } else {
            val newReference = databaseReferenceDollarCurrencyData.push()
            val objectKey = newReference.key ?: return


            data.objectName = objectKey
            newReference.setValue(data)
                .await()
        }

        appPreferences.currencyDollar = data.dollarCurrency
    }

    override suspend fun addPhoneData(phoneData: PhoneData) {
        databaseReferencePhoneData.push().setValue(phoneData).await()
    }


    override suspend fun upsertUserData(data: UserPasswordData) {
        val snapshot = databaseReferenceUserPasswordData
            .orderByChild("objectName")
            .equalTo(data.objectName)
            .get()
            .await()

        if (snapshot.exists()) {
            databaseReferenceUserPasswordData.child(data.objectName).setValue(data).await()
        } else {
            val newReference = databaseReferenceUserPasswordData.push()
            val objectName = newReference.key ?: return
            data.objectName = objectName
            newReference.setValue(data).await()
        }
    }

    override fun getAllUserData(): Flow<List<UserPasswordData>> = callbackFlow<List<UserPasswordData>> {
        val listener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val dataList = if(snapshot.exists() && snapshot.hasChildren()){
                    snapshot.children.mapNotNull { data ->
                        data.getValue(UserPasswordData::class.java)
                    }
                }else{
                    emptyList()
                }
                trySend(dataList)
            }

            override fun onCancelled(error: DatabaseError) {
                trySend(emptyList())
            }
        }
        databaseReferenceUserPasswordData.addValueEventListener(listener)
        awaitClose {
            databaseReferenceUserPasswordData.removeEventListener(listener)
        }
    }.flowOn(Dispatchers.IO)

    override suspend fun deleteUserData(objectName: String) {
        val objectRef = databaseReferenceUserPasswordData.child(objectName)
        objectRef.removeValue().await()
    }

}