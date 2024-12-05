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
import uz.excellentshoes.businesscalculation.data.types.KontragentData
import uz.excellentshoes.businesscalculation.data.types.MoneySpendData
import uz.excellentshoes.businesscalculation.domain.CommonMoneySpenderRepository

class CommonMoneySpenderRepositoryImpl private constructor(): CommonMoneySpenderRepository {
    private val databaseReferenceMoneySpendData = App.database.getReference("MoneySpendData")
    private val databaseReferenceKontragentData = App.database.getReference("KontragentData")
    private val appPreferences = AppPreferences.getInstance()

    companion object{
        private lateinit var instance: CommonMoneySpenderRepository

        fun getInstance(): CommonMoneySpenderRepository{
            if(!::instance.isInitialized){
                instance = CommonMoneySpenderRepositoryImpl()
            }
            return instance
        }
    }

    override suspend fun addMoneySpendData(data: MoneySpendData): Boolean {
        return try {
            val newReference = databaseReferenceMoneySpendData.push()
            val objectName = newReference.key ?: return false
            data.employeeName = appPreferences.fullName
            data.objectName = objectName
            newReference.setValue(data).await()
            true
        }catch (e: Exception){
            false
        }
    }

    override fun getAllMoneySpendData(): Flow<List<MoneySpendData>> = callbackFlow{
        val moneySpendDataListener = object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                val moneySpendDataList = if(snapshot.exists() && snapshot.hasChildren()){
                    snapshot.children.mapNotNull { data->
                        data.getValue(MoneySpendData::class.java)
                    }
                }else{
                    emptyList()
                }

                trySend(moneySpendDataList)
            }

            override fun onCancelled(error: DatabaseError) {
                trySend(emptyList())
            }
        }
        databaseReferenceMoneySpendData.addValueEventListener(moneySpendDataListener)
        awaitClose {
            databaseReferenceMoneySpendData.removeEventListener(moneySpendDataListener)
        }
    }.flowOn(Dispatchers.IO)

    override suspend fun deleteMoneySpendData(objectName: String) {
        val objectRef = databaseReferenceMoneySpendData.child(objectName)
        objectRef.removeValue().await()
    }

    override suspend fun editMoneySpendData(data: MoneySpendData): Boolean {
        return try {
            val query = databaseReferenceMoneySpendData.orderByChild("objectName").equalTo(data.objectName)
            val snapshot = query.get().await()

            if (snapshot.exists()) {
                val dataSnapshot = snapshot.children.firstOrNull()
                if (dataSnapshot != null) {
                    dataSnapshot.ref.setValue(data).await()
                    true
                } else {
                    false
                }
            } else {
                false
            }
        } catch (e: Exception) {
            false
        }
    }

    override fun getAllKontragent(): Flow<List<KontragentData>> = callbackFlow<List<KontragentData>> {
        val listener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val kontragentDataList = if(snapshot.exists() && snapshot.hasChildren()){
                    snapshot.children.mapNotNull { dataSnapshot ->
                        dataSnapshot.getValue(KontragentData::class.java)
                    }
                }else{
                    emptyList()
                }
                trySend(kontragentDataList)
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

    override fun getInitialPaidAmount(): Task<Double> {
        return databaseReferenceMoneySpendData.get().continueWith { task ->
            if (task.isSuccessful) {
                task.result?.children?.sumOf { dataSnapshot ->
                    val data = dataSnapshot.getValue(MoneySpendData::class.java)
                    data?.takeIf { it.spendType == "To'langan" }?.moneyAmount ?: 0.0
                } ?: 0.0
            } else {
                throw task.exception ?: Exception("Something went wrong")
            }
        }
    }

    override fun getInitialUnpaidAmount(): Task<Double> {
        return databaseReferenceMoneySpendData.get().continueWith { task ->
            if (task.isSuccessful) {
                task.result?.children?.sumOf { dataSnapshot ->
                    val data = dataSnapshot.getValue(MoneySpendData::class.java)
                    data?.takeIf { it.spendType == "To'lanishi kerak" }?.moneyAmount ?: 0.0
                } ?: 0.0
            } else {
                throw task.exception ?: Exception("Something went wrong")
            }
        }
    }

    override fun getInitialReceiveAmount(): Task<Double> {
        return databaseReferenceMoneySpendData.get().continueWith { task ->
            if (task.isSuccessful) {
                task.result?.children?.sumOf { dataSnapshot ->
                    val data = dataSnapshot.getValue(MoneySpendData::class.java)
                    data?.takeIf { it.spendType == "Olindi" }?.moneyAmount ?: 0.0
                } ?: 0.0
            } else {
                throw task.exception ?: Exception("Something went wrong")
            }
        }
    }

    override fun getInitialUnReceiveAmount(): Task<Double> {
        return databaseReferenceMoneySpendData.get().continueWith { task ->
            if (task.isSuccessful) {
                task.result?.children?.sumOf { dataSnapshot ->
                    val data = dataSnapshot.getValue(MoneySpendData::class.java)
                    data?.takeIf { it.spendType == "Olinishi kerak" }?.moneyAmount ?: 0.0
                } ?: 0.0
            } else {
                throw task.exception ?: Exception("Something went wrong")
            }
        }
    }

    override fun observePaidAmount(): Flow<Double> = callbackFlow {
        val listener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val totalAmount = snapshot.children.sumOf { dataSnapshot ->
                    val data = dataSnapshot.getValue(MoneySpendData::class.java)
                    data?.takeIf { it.spendType == "To'langan" }?.moneyAmount ?: 0.0
                }
                trySend(totalAmount).isSuccess
            }

            override fun onCancelled(error: DatabaseError) {

            }
        }
        databaseReferenceMoneySpendData.addValueEventListener(listener)

        awaitClose {
            databaseReferenceMoneySpendData.removeEventListener(listener)
        }
    }

    override fun observerUnpaidAmount(): Flow<Double> = callbackFlow{
        val listener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val totalAmount = snapshot.children.sumOf { dataSnapshot ->
                    val data = dataSnapshot.getValue(MoneySpendData::class.java)
                    data?.takeIf { it.spendType == "To'lanishi kerak" }?.moneyAmount ?: 0.0
                }
                trySend(totalAmount).isSuccess
            }

            override fun onCancelled(error: DatabaseError) {

            }
        }
        databaseReferenceMoneySpendData.addValueEventListener(listener)

        awaitClose {
            databaseReferenceMoneySpendData.removeEventListener(listener)
        }
    }

    override fun observeReceiveAmount(): Flow<Double> = callbackFlow{
        val listener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val totalAmount = snapshot.children.sumOf { dataSnapshot ->
                    val data = dataSnapshot.getValue(MoneySpendData::class.java)
                    data?.takeIf { it.spendType == "Olindi" }?.moneyAmount ?: 0.0
                }
                trySend(totalAmount).isSuccess
            }

            override fun onCancelled(error: DatabaseError) {

            }
        }
        databaseReferenceMoneySpendData.addValueEventListener(listener)

        awaitClose {
            databaseReferenceMoneySpendData.removeEventListener(listener)
        }
    }

    override fun observeUnReceiveAmount(): Flow<Double> = callbackFlow{
        val listener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val totalAmount = snapshot.children.sumOf { dataSnapshot ->
                    val data = dataSnapshot.getValue(MoneySpendData::class.java)
                    data?.takeIf { it.spendType == "Olinishi kerak" }?.moneyAmount ?: 0.0
                }
                trySend(totalAmount).isSuccess
            }

            override fun onCancelled(error: DatabaseError) {

            }
        }
        databaseReferenceMoneySpendData.addValueEventListener(listener)

        awaitClose {
            databaseReferenceMoneySpendData.removeEventListener(listener)
        }
    }
}