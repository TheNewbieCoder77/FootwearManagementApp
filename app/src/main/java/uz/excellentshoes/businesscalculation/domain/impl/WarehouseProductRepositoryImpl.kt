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
import uz.excellentshoes.businesscalculation.data.types.UnitData
import uz.excellentshoes.businesscalculation.data.types.WarehouseProductData
import uz.excellentshoes.businesscalculation.domain.WarehouseProductRepository

class WarehouseProductRepositoryImpl : WarehouseProductRepository {
    private val databaseReferenceWarehouseProduct =
        FirebaseDatabase.getInstance().getReference("WarehouseProduct")
    private val databaseReferenceUnitData = FirebaseDatabase.getInstance().getReference("UnitData")

    override suspend fun addWarehouseProduct(data: WarehouseProductData): Boolean {
        return try {
            val newReference = databaseReferenceWarehouseProduct.push()
            val objectName = newReference.key ?: return false
            data.objectName = objectName
            newReference.setValue(data).await()
            true
        } catch (e: Exception) {
            false
        }
    }


    override suspend fun deleteWarehouseProduct(objectName: String) {
        val objectRef = databaseReferenceWarehouseProduct.child(objectName)
        objectRef.removeValue().await()
    }

    override suspend fun editWarehouseProduct(data: WarehouseProductData) {
        val snapshot = databaseReferenceWarehouseProduct
            .orderByChild("objectName")
            .equalTo(data.objectName)
            .get()
            .await()
        if (snapshot.exists()) {
            databaseReferenceWarehouseProduct.child(data.objectName).setValue(data).await()
        }
    }

    override fun getAllWarehouseProducts(): Flow<List<WarehouseProductData>> = callbackFlow {
        val listener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val dataList = if (snapshot.exists() && snapshot.hasChildren()) {
                    snapshot.children.mapNotNull { data ->
                        data.getValue(WarehouseProductData::class.java)
                    }
                } else {
                    emptyList()
                }
                trySend(dataList)
            }

            override fun onCancelled(error: DatabaseError) {
                trySend(emptyList())
            }
        }

        databaseReferenceWarehouseProduct.addValueEventListener(listener)
        awaitClose {
            databaseReferenceWarehouseProduct.removeEventListener(listener)
        }
    }.flowOn(Dispatchers.IO)

    override fun getAllUnitData(): Flow<List<UnitData>> = callbackFlow {
        val listener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val dataList = if (snapshot.exists() && snapshot.hasChildren()) {
                    snapshot.children.mapNotNull { data ->
                        data.getValue(UnitData::class.java)
                    }
                } else {
                    emptyList()
                }
                trySend(dataList)
            }

            override fun onCancelled(error: DatabaseError) {
                trySend(emptyList())
            }
        }
        databaseReferenceUnitData.addValueEventListener(listener)
        awaitClose {
            databaseReferenceUnitData.removeEventListener(listener)
        }
    }.flowOn(Dispatchers.IO)


    companion object {
        private lateinit var instance: WarehouseProductRepositoryImpl
        fun getInstance(): WarehouseProductRepositoryImpl {
            if (!::instance.isInitialized) {
                instance = WarehouseProductRepositoryImpl()
            }
            return instance
        }

    }
}