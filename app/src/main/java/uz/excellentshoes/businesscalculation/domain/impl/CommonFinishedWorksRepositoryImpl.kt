package uz.excellentshoes.businesscalculation.domain.impl

import android.health.connect.datatypes.units.Velocity
import android.util.Log
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
import uz.excellentshoes.businesscalculation.data.types.FinishedShoeMakeData
import uz.excellentshoes.businesscalculation.data.types.FinishedSkinCutData
import uz.excellentshoes.businesscalculation.data.types.FinishedSkinTailData
import uz.excellentshoes.businesscalculation.domain.CommonFinishedWorksRepository

class CommonFinishedWorksRepositoryImpl private constructor() : CommonFinishedWorksRepository {
    private val databaseReferenceFinishedShoeMakeData =
        App.database.getReference("FinishedShoeMakeData")
    private val databaseReferenceFinishedSkinTailData =
        App.database.getReference("FinishedSkinTailData")
    private val databaseReferenceFinishedSkinCutData =
        App.database.getReference("FinishedSkinCutData")


    companion object {
        private lateinit var instance: CommonFinishedWorksRepository

        fun getInstance(): CommonFinishedWorksRepository {
            if (!::instance.isInitialized) {
                instance = CommonFinishedWorksRepositoryImpl()
            }
            return instance
        }
    }

    override fun getALlFinishedShoeMake(): Flow<List<FinishedShoeMakeData>> = callbackFlow {
        val listener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val dataList = if(snapshot.exists() && snapshot.hasChildren()){
                    snapshot.children.mapNotNull { item ->
                        item.getValue(FinishedShoeMakeData::class.java)
                    }.filter {
                        it.paidMoney.isEmpty()
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
        databaseReferenceFinishedShoeMakeData.addValueEventListener(listener)
        awaitClose {
            databaseReferenceFinishedShoeMakeData.removeEventListener(listener)
        }
    }.flowOn(Dispatchers.IO)


    override fun getAllFinishedSkinTail(): Flow<List<FinishedSkinTailData>> =
        callbackFlow<List<FinishedSkinTailData>> {
            val listener = object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val dataList = if(snapshot.exists() && snapshot.hasChildren()){
                        snapshot.children.mapNotNull { item ->
                            item.getValue(FinishedSkinTailData::class.java)
                        }.filter {
                            it.paidMoney.isEmpty()
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
            databaseReferenceFinishedSkinTailData.addValueEventListener(listener)
            awaitClose {
                databaseReferenceFinishedSkinTailData.removeEventListener(listener)
            }
        }.flowOn(Dispatchers.IO)


    override fun getAllFinishedSkinCut(): Flow<List<FinishedSkinCutData>> =
        callbackFlow<List<FinishedSkinCutData>> {
            val listener = object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val dataList = if(snapshot.exists() && snapshot.hasChildren()){
                        snapshot.children.mapNotNull { item ->
                            item.getValue(FinishedSkinCutData::class.java)
                        }.filter {
                            it.paidMoney.isEmpty()
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
            databaseReferenceFinishedSkinCutData.addValueEventListener(listener)
            awaitClose {
                databaseReferenceFinishedSkinCutData.removeEventListener(listener)
            }
        }.flowOn(Dispatchers.IO)

    override suspend fun editFinishedShoeMae(data: FinishedShoeMakeData) {
        val snapshot = databaseReferenceFinishedShoeMakeData.orderByChild("objectName")
            .equalTo(data.objectName)
            .get()
            .await()

        if(snapshot.exists() && snapshot.children.count() > 0){
            val key = snapshot.children.first().key
            key?.let {
                databaseReferenceFinishedShoeMakeData.child(key).setValue(data).await()
            }
        }

    }

    override suspend fun editFinishedSkinTail(data: FinishedSkinTailData) {
        val snapshot = databaseReferenceFinishedSkinTailData.orderByChild("objectName")
            .equalTo(data.objectName)
            .get()
            .await()
        if(snapshot.exists() && snapshot.children.count() > 0){
            val key = snapshot.children.first().key
            key?.let {
                databaseReferenceFinishedSkinTailData.child(key).setValue(data).await()
            }
        }
    }

    override suspend fun editFinishedSkinCut(data: FinishedSkinCutData) {
        val snapshot = databaseReferenceFinishedSkinCutData.orderByChild("objectName")
            .equalTo(data.objectName)
            .get()
            .await()

        if(snapshot.exists() && snapshot.children.count() > 0){
            val key = snapshot.children.first().key
            key?.let {
                databaseReferenceFinishedSkinCutData.child(key).setValue(data).await()
            }
        }
    }

}