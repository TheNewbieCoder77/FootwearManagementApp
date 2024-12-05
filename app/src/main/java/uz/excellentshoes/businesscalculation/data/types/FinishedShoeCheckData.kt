package uz.excellentshoes.businesscalculation.data.types

data class FinishedShoeCheckData(
    var objectName: String = "",
    var employeeName: String = "",
    var phoneNumber: String = "",
    val modelName: String = "",
    val color: String = "",
    val countPair: Int = 0,
    val skinType: String = "",
    var addedTime: String = "",
    val comment: String = ""
)
