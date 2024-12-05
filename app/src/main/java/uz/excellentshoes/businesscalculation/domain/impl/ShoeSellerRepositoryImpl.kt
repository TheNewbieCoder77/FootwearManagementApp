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
import uz.excellentshoes.businesscalculation.data.types.PreparedToSailData
import uz.excellentshoes.businesscalculation.data.types.SoldShoeData
import uz.excellentshoes.businesscalculation.domain.ShoeSellerRepository

class ShoeSellerRepositoryImpl private constructor() : ShoeSellerRepository {
    private val databaseReferencePreparedToSailData = App.database.getReference("PreparedToSailData")
    private val databaseReferenceSoldShoeData = App.database.getReference("SoldShoeData")
    private val appPreferences = AppPreferences.getInstance()

    override fun getAllPreparedToSailData(): Flow<List<PreparedToSailData>> = callbackFlow{
        val listener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val dataList = if(snapshot.exists() && snapshot.hasChildren()){
                    snapshot.children.mapNotNull { data ->
                        data.getValue(PreparedToSailData::class.java)
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
        databaseReferencePreparedToSailData.addValueEventListener(listener)
        awaitClose {
            databaseReferencePreparedToSailData.removeEventListener(listener)
        }
    }.flowOn(Dispatchers.IO)

    override suspend fun addSoldShoeDataList(dataList: List<SoldShoeData>): Boolean {
        return try {
            dataList.forEach { data->
                val newReference = databaseReferenceSoldShoeData.push()
                val objectName = newReference.key ?: return false
                data.objectName = objectName
                data.employeeName = appPreferences.fullName
                newReference.setValue(data).await()
            }
            true
        }
        catch (e: Exception){
            false
        }
    }

    override suspend fun deletePreparedToSailData(objectName: String) {
        val objectRef = databaseReferencePreparedToSailData.child(objectName)
        objectRef.removeValue().await()
    }




    companion object {
        private lateinit var instance: ShoeSellerRepositoryImpl
        fun getInstance(): ShoeSellerRepositoryImpl {
            if (!::instance.isInitialized) {
                instance = ShoeSellerRepositoryImpl()
            }
            return instance
        }
    }
}