package uz.excellentshoes.businesscalculation.data.types

data class SoldShoeData(
    var employeeName: String = "",
    var objectName: String = "",
    var phoneNumber: String = "",
    val modelName: String = "",
    val countPair: Int = 0,
    val color: String = "",
    val skinType: String = "",
    val addedTime: String = ""
)
