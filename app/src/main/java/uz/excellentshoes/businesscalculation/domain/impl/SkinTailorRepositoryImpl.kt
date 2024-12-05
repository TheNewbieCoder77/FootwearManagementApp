package uz.excellentshoes.businesscalculation.domain.impl

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.tasks.await
import uz.excellentshoes.businesscalculation.app.App
import uz.excellentshoes.businesscalculation.data.AppPreferences
import uz.excellentshoes.businesscalculation.data.types.FinishedSkinTailData
import uz.excellentshoes.businesscalculation.data.types.PreparedTailData
import uz.excellentshoes.businesscalculation.data.types.ProcessTailData
import uz.excellentshoes.businesscalculation.domain.SkinTailorRepository

class SkinTailorRepositoryImpl private constructor(): SkinTailorRepository {
    private val databaseReferenceProcessData = App.database.getReference("ProcessData")
    private val databaseReferencePreparedTailData = App.database.getReference("PreparedTailData")
    private val databaseReferenceFinishedSkinTailData = App.database.getReference("FinishedSkinTailData")
    private val appPreferences = AppPreferences.getInstance()

    override suspend fun addProcessDataList(processTailDataList: List<ProcessTailData>): Boolean {
        return try {
            processTailDataList.forEach { processData ->
                val newReference = databaseReferenceProcessData.push()
                val objectName = newReference.key ?: return false
                processData.objectName = objectName
                processData.phoneNumber = appPreferences.phoneNumber
                processData.employeeName = appPreferences.fullName
                newReference.setValue(processData).await()
            }
            true
        } catch (e: Exception) {
            false
        }
    }

    override suspend fun addFinishedSkinTail(data: FinishedSkinTailData) {
        val newReference = databaseReferenceFinishedSkinTailData.push()
        val objectName = newReference.key ?: return
        data.objectName = objectName
        data.employeeName = appPreferences.fullName
        data.phoneNumber = appPreferences.phoneNumber
        newReference.setValue(data).await()
    }

    override suspend fun addPreparedTailData(preparedTailData: PreparedTailData) {
            val newReference = databaseReferencePreparedTailData.push()
            val objectName = newReference.key ?:return
            preparedTailData.objectName = objectName
            preparedTailData.employeeName = appPreferences.fullName
            newReference.setValue(preparedTailData).await()
    }

    override fun getProcessDataByPhoneNumber(phoneNumber: String): Flow<List<ProcessTailData>> = callbackFlow {
        val processDataListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val processDataList = if(snapshot.exists() && snapshot.hasChildren()){
                    snapshot.children.mapNotNull { data ->
                        val processTailData = data.getValue(ProcessTailData::class.java)
                        if (processTailData?.phoneNumber == phoneNumber) processTailData else null
                    }
                }else{
                    emptyList()
                }

                trySend(processDataList)
            }

            override fun onCancelled(error: DatabaseError) {
                trySend(emptyList())
            }
        }

        databaseReferenceProcessData.addValueEventListener(processDataListener)

        awaitClose {
            databaseReferenceProcessData.removeEventListener(processDataListener)
        }
    }.flowOn(Dispatchers.IO)

    override suspend fun deleteProcessData(objectName: String): Boolean {
        return try {
            val objectRef = databaseReferenceProcessData.child(objectName)
            objectRef.removeValue().await()
            val snapshot = objectRef.get().await()
            !snapshot.exists()
        }catch (e: Exception){
            false
        }
    }


    companion object{
        private lateinit var instance: SkinTailorRepository

        fun getInstance(): SkinTailorRepository{
            if(!::instance.isInitialized){
                instance = SkinTailorRepositoryImpl()
            }
            return instance
        }
    }

}