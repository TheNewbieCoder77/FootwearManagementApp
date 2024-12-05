package uz.excellentshoes.businesscalculation.data.types

data class ProcessTailData(
    var phoneNumber: String = "",
    var objectName: String = "",
    var employeeName: String = "",
    val modelName: String = "",
    val color: String = "",
    val countPair: Int = 0,
    val skinType: String = "",
    val comment: String = ""
)