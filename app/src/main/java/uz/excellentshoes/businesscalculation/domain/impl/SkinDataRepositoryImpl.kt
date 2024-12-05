package uz.excellentshoes.businesscalculation.domain.impl


import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.tasks.await
import uz.excellentshoes.businesscalculation.data.types.SkinData
import uz.excellentshoes.businesscalculation.domain.SkinDataRepository
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import uz.excellentshoes.businesscalculation.app.App
import uz.excellentshoes.businesscalculation.data.AppPreferences
import uz.excellentshoes.businesscalculation.data.types.FinishedSkinCutData
import uz.excellentshoes.businesscalculation.data.types.ShoeColorData
import uz.excellentshoes.businesscalculation.data.types.ShoeModelData
import uz.excellentshoes.businesscalculation.data.types.SkinTypeData

class SkinDataRepositoryImpl private constructor() : SkinDataRepository {
    private val databaseReference = App.database.getReference("SkinData")
    private val databaseReferenceFinishedSkinCutData =
        App.database.getReference("FinishedSkinCutData")
    private val databaseReferenceShoeColorData = App.database.getReference("ShoeColorData")
    private val databaseReferenceSkinTypeData = App.database.getReference("SkinTypeData")
    private val databaseReferenceShoeModelData = App.database.getReference("ShoeModelData")
    private val appPreferences = AppPreferences.getInstance()

    override suspend fun addSkinData(skinData: SkinData): Boolean {
        return try {
            val newReference = databaseReference.push()
            val objectName = newReference.key ?: return false
            skinData.objectName = objectName
            skinData.employeeName = appPreferences.fullName
            newReference.setValue(skinData).await()
            true
        } catch (e: Exception) {
            false
        }
    }

    override suspend fun addFinishedSkinCut(data: FinishedSkinCutData) {
        val newReference = databaseReferenceFinishedSkinCutData.push()
        val objectName = newReference.key ?: return
        data.objectName = objectName
        data.employeeName = appPreferences.fullName
        data.phoneNumber = appPreferences.phoneNumber
        newReference.setValue(data).await()
    }

    override suspend fun deleteSkinData(objectName: String): Boolean {
        return try {
            val objectRef = databaseReference.child(objectName)

            objectRef.removeValue().await()

            val snapshot = objectRef.get().await()
            !snapshot.exists()
        } catch (e: Exception) {
            false
        }
    }

    override fun getAllSkinData(): Flow<List<SkinData>> = callbackFlow {
        val skinDataListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val skinDataList = if(snapshot.exists() && snapshot.hasChildren()){
                    snapshot.children.mapNotNull { data ->
                        data.getValue(SkinData::class.java)
                    }
                }else{
                    emptyList()
                }
                trySend(skinDataList)
            }

            override fun onCancelled(error: DatabaseError) {
                trySend(emptyList())
            }
        }
        databaseReference.addValueEventListener(skinDataListener)

        awaitClose {
            databaseReference.removeEventListener(skinDataListener)
        }
    }.flowOn(Dispatchers.IO)

    override fun getAllColors(): Flow<List<ShoeColorData>> = callbackFlow<List<ShoeColorData>> {
        val listener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val dataList = if(snapshot.exists() && snapshot.hasChildren()){
                    snapshot.children.mapNotNull { data ->
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

    override fun getAllModels(): Flow<List<ShoeModelData>> = callbackFlow {
        val listener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val dataList = if(snapshot.exists() && snapshot.hasChildren()){
                    snapshot.children.mapNotNull { data ->
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

    override fun getAllTypes(): Flow<List<SkinTypeData>> = callbackFlow<List<SkinTypeData>> {
        val listener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val dataList = if(snapshot.exists() && snapshot.hasChildren()){
                    snapshot.children.mapNotNull { data ->
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

    companion object {
        private lateinit var instance: SkinDataRepositoryImpl

        fun getInstance(): SkinDataRepository {
            if (!::instance.isInitialized) {
                instance = SkinDataRepositoryImpl()
            }
            return instance
        }
    }


}