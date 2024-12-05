package uz.excellentshoes.businesscalculation.domain.impl

import android.util.Log
import com.google.android.gms.tasks.Task
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
import uz.excellentshoes.businesscalculation.data.types.DeclinedShoeData
import uz.excellentshoes.businesscalculation.data.types.FinishedShoeCheckData
import uz.excellentshoes.businesscalculation.data.types.FinishedShoeMakeData
import uz.excellentshoes.businesscalculation.data.types.PreparedShoeMakerData
import uz.excellentshoes.businesscalculation.data.types.PreparedToSailData
import uz.excellentshoes.businesscalculation.domain.CommonShoeCheckerRepository

class CommonShoeCheckerRepositoryImpl private constructor() : CommonShoeCheckerRepository {
    private val appPreferences = AppPreferences.getInstance()
    private val databaseReferencePreparedShoeMakerData =
        App.database.getReference("PreparedShoeMakerData")
    private val databaseReferencePreparedToSailData =
        App.database.getReference("PreparedToSailData")
    private val databaseReferenceDeclinedShoeData = App.database.getReference("DeclinedShoeData")
    private val databaseReferenceFinishedShoeCheckData =
        App.database.getReference("FinishedShoeCheckData")
    private val databaseReferenceFinishedShoeMakeData =
        App.database.getReference("FinishedShoeMakeData")
    private val databaseReferencePreparedShoeMakerDataCountDaily = App.database.getReference("PreparedShoeMakerDataCountDaily")


    override fun getAllPreparedShoeMakerData(): Flow<List<PreparedShoeMakerData>> = callbackFlow {
        val preparedShoeMakerListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val preparedShoeMakerDataList = if(snapshot.exists() && snapshot.hasChildren()){
                    snapshot.children.mapNotNull { data ->
                        data.getValue(PreparedShoeMakerData::class.java)
                    }
                }else{
                    emptyList()
                }
                trySend(preparedShoeMakerDataList)
            }

            override fun onCancelled(error: DatabaseError) {
                trySend(emptyList())
            }

        }
        databaseReferencePreparedShoeMakerData.addValueEventListener(preparedShoeMakerListener)
        awaitClose {
            databaseReferencePreparedShoeMakerData.removeEventListener(preparedShoeMakerListener)
        }

    }.flowOn(Dispatchers.IO)


    override suspend fun addDeclinedShoeData(data: DeclinedShoeData) {
        val newReference = databaseReferenceDeclinedShoeData.push()
        val objectName = newReference.key ?: return
        data.objectName = objectName
        newReference.setValue(data).await()
    }

    override fun getAllDeclinedShoeData(): Flow<List<DeclinedShoeData>> = callbackFlow {
        val declinedShoeListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val declinedShoesDataList = if(snapshot.exists() && snapshot.hasChildren()){
                    snapshot.children.mapNotNull { data ->
                        data.getValue(DeclinedShoeData::class.java)
                    }
                }else{
                    emptyList()
                }
                trySend(declinedShoesDataList)
            }

            override fun onCancelled(error: DatabaseError) {
                trySend(emptyList())
            }

        }
        databaseReferenceDeclinedShoeData.addValueEventListener(declinedShoeListener)
        awaitClose {
            databaseReferenceDeclinedShoeData.removeEventListener(declinedShoeListener)
        }

    }.flowOn(Dispatchers.IO)


    override suspend fun deletePreparedShoeMakerData(objectName: String): Boolean {
        return try {
            val objectRef = databaseReferencePreparedShoeMakerData.child(objectName)
            objectRef.removeValue().await()
            val snapshot = objectRef.get().await()
            !snapshot.exists()
        } catch (e: Exception) {
            false
        }
    }

    override suspend fun deleteDeclinedShoeData(objectName: String): Boolean {
        return try {
            val objectRef = databaseReferenceDeclinedShoeData.child(objectName)
            objectRef.removeValue().await()
            val snapshot = objectRef.get().await()
            !snapshot.exists()
        } catch (e: Exception) {
            false
        }
    }

    override suspend fun addShoeSellerData(data: PreparedToSailData) {
        val newReference = databaseReferencePreparedToSailData.push()
        val objectName = newReference.key ?: return
        data.objectName = objectName
        data.employeeName = appPreferences.fullName
        data.phoneNumber = appPreferences.phoneNumber
        newReference.setValue(data).await()
    }

    override suspend fun addFinishedShoeCheck(data: FinishedShoeCheckData) {
        val newReference = databaseReferenceFinishedShoeCheckData.push()
        val objectName = newReference.key ?: return
        data.objectName = objectName
        data.employeeName = appPreferences.fullName
        data.phoneNumber = appPreferences.phoneNumber
        newReference.setValue(data).await()
    }

    override suspend fun addPreparedShoeMakeList(dataList: List<PreparedShoeMakerData>) {
        dataList.forEach { data->
            val newReference = databaseReferencePreparedShoeMakerData.push()
            val objectName = newReference.key ?: return
            data.objectName = objectName
            data.employeeName = appPreferences.fullName
            newReference.setValue(data).await()
        }
    }

    override fun getInitialShoeCount(currentDate: String): Task<Int> {
        return databaseReferenceFinishedShoeMakeData.get().continueWith { task ->
            if (task.isSuccessful) {
                task.result?.children?.count{ dataSnapshot->
                    val data = dataSnapshot.getValue(FinishedShoeMakeData::class.java)
                    data?.let {
                        val addedDate = it.addedTime.split(" ")[0]
                        addedDate == currentDate
                    } ?:false
                } ?:0
            } else {
                throw task.exception ?: Exception("Error getting count")
            }
        }
    }

    override fun getInitialFinishedShoeCount(currentDate: String): Task<Int> {
        return databaseReferenceFinishedShoeCheckData.get().continueWith { task ->
            if (task.isSuccessful) {
                task.result?.children?.count{ dataSnapshot->
                    val data = dataSnapshot.getValue(FinishedShoeCheckData::class.java)
                    data?.let {
                        val addedDate = it.addedTime.split(" ")[0]
                        addedDate == currentDate
                    }?: false
                }?: 0
            } else {
                throw task.exception ?: Exception("Something went wrong")
            }
        }
    }

    override fun observeShoeCount(currentDate: String): Flow<Int> = callbackFlow {
        val listener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val count = snapshot.children.count { dataSnapshot->
                    val data = dataSnapshot.getValue(FinishedShoeMakeData::class.java)
                    data?.let {
                        val addedDate = it.addedTime.split(" ")[0]
                        addedDate == currentDate
                    }?:false
                }
                trySend(count).isSuccess
            }

            override fun onCancelled(error: DatabaseError) {

            }
        }
        databaseReferenceFinishedShoeMakeData.addValueEventListener(listener)

        awaitClose {
            databaseReferenceFinishedShoeMakeData.removeEventListener(listener)
        }
    }

    override fun observeFinishedShoeCount(currentDate: String): Flow<Int> = callbackFlow {
        val listener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val count = snapshot.children.count { dataSnapshot->
                    val data = dataSnapshot.getValue(FinishedShoeCheckData::class.java)
                    data?.let {
                        val addedDate = it.addedTime.split(" ")[0]
                        addedDate == currentDate
                    }?: false
                }
                trySend(count).isSuccess
            }

            override fun onCancelled(error: DatabaseError) {

            }
        }
        databaseReferenceFinishedShoeCheckData.addValueEventListener(listener)
        awaitClose {
            databaseReferenceFinishedShoeCheckData.removeEventListener(listener)
        }
    }


    companion object {
        private lateinit var instance: CommonShoeCheckerRepository

        fun getInstance(): CommonShoeCheckerRepository {
            if (!::instance.isInitialized) {
                instance = CommonShoeCheckerRepositoryImpl()
            }
            return instance
        }
    }


}