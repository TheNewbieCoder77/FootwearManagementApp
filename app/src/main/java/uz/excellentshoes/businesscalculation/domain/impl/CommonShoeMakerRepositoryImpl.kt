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
import uz.excellentshoes.businesscalculation.data.types.FinishedShoeMakeData
import uz.excellentshoes.businesscalculation.data.types.PreparedShoeMakerData
import uz.excellentshoes.businesscalculation.data.types.PreparedTailData
import uz.excellentshoes.businesscalculation.data.types.ProcessShoeMakerData
import uz.excellentshoes.businesscalculation.domain.CommonShoeMakerRepository

class CommonShoeMakerRepositoryImpl private constructor(): CommonShoeMakerRepository {
    private val appPreferences = AppPreferences.getInstance()
    private val databaseReferencePreparedTailData = App.database.getReference("PreparedTailData")
    private val databaseReferenceProcessShoeMakerData = App.database.getReference("ProcessShoeMakerData")
    private val databaseReferencePreparedShoeMakerData = App.database.getReference("PreparedShoeMakerData")
    private val databaseReferenceFinishedShoeMakeData = App.database.getReference("FinishedShoeMakeData")

    override fun getAllPreparedTailData(): Flow<List<PreparedTailData>> = callbackFlow{
        val preparedTailDataListener = object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                val preparedTailDataList = if(snapshot.exists() && snapshot.hasChildren()){
                    snapshot.children.mapNotNull { data->
                        data.getValue(PreparedTailData::class.java)
                    }
                }else{
                    emptyList()
                }
                trySend(preparedTailDataList)
            }

            override fun onCancelled(error: DatabaseError) {
                trySend(emptyList())
            }
        }
        databaseReferencePreparedTailData.addValueEventListener(preparedTailDataListener)
        awaitClose {
            databaseReferencePreparedTailData.removeEventListener(preparedTailDataListener)
        }

    }.flowOn(Dispatchers.IO)

    override suspend fun addProcessShoeMakerDataList(dataList: List<ProcessShoeMakerData>): Boolean {
        return try {
            dataList.forEach { data->
                val newReference = databaseReferenceProcessShoeMakerData.push()
                val objectName = newReference.key ?: return false
                data.objectName = objectName
                data.phoneNumber = appPreferences.phoneNumber
                data.employeeName = appPreferences.fullName
                newReference.setValue(data).await()
            }
            true
        }catch (e: Exception){
            false
        }
    }

    override fun getProcessShoeMakerDataByPhone(phoneNumber: String): Flow<List<ProcessShoeMakerData>> = callbackFlow<List<ProcessShoeMakerData>> {
        val processShoeMakerListener = object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                val processShoeMakerList = if(snapshot.exists() && snapshot.hasChildren()){
                    snapshot.children.mapNotNull { data ->
                        val processShoeMakerData = data.getValue(ProcessShoeMakerData::class.java)
                        if(processShoeMakerData?.phoneNumber == phoneNumber) processShoeMakerData else null
                    }
                }else{
                    emptyList()
                }
                trySend(processShoeMakerList)
            }

            override fun onCancelled(error: DatabaseError) {
                trySend(emptyList())
            }
        }
        databaseReferenceProcessShoeMakerData.addValueEventListener(processShoeMakerListener)
        awaitClose{
            databaseReferenceProcessShoeMakerData.removeEventListener(processShoeMakerListener)
        }
    }.flowOn(Dispatchers.IO)


    override suspend fun addPreparedTailData(data: PreparedTailData) {
        val newReference = databaseReferencePreparedTailData.push()
        val objectName = newReference.key ?:return
        data.objectName = objectName
        data.employeeName = appPreferences.fullName
        newReference.setValue(data).await()
    }

    override suspend fun deleteProcessShoeMakerData(objectName: String): Boolean {
        return try {
            val objectRef = databaseReferenceProcessShoeMakerData.child(objectName)
            objectRef.removeValue().await()
            val snapshot = objectRef.get().await()
            !snapshot.exists()
        }
        catch (e: Exception){
            false
        }
    }

    override suspend fun deletePreparedTailData(objectName: String): Boolean {
        return try {
            val objectRef = databaseReferencePreparedTailData.child(objectName)
            objectRef.removeValue().await()
            val snapshot = objectRef.get().await()
            !snapshot.exists()
        }catch (e: Exception){
            false
        }
    }

    override suspend fun addPreparedShoeMakerData(data: PreparedShoeMakerData) {
        val newReference = databaseReferencePreparedShoeMakerData.push()
        val objectName = newReference.key ?: return
        data.objectName = objectName
        data.employeeName = appPreferences.fullName
        newReference.setValue(data).await()
    }

    override suspend fun addFinishedShoeMake(data: FinishedShoeMakeData) {
        val newReference = databaseReferenceFinishedShoeMakeData.push()
        val objectName = newReference.key ?: return
        data.objectName = objectName
        data.employeeName = appPreferences.fullName
        data.phoneNumber = appPreferences.phoneNumber
        newReference.setValue(data).await()
    }


    companion object{
        private lateinit var instance: CommonShoeMakerRepository

        fun getInstance(): CommonShoeMakerRepository{
            if(!::instance.isInitialized){
                instance = CommonShoeMakerRepositoryImpl()
            }
            return instance
        }
    }
}