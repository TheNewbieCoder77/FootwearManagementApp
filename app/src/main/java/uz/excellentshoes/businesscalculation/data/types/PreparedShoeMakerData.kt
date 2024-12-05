package uz.excellentshoes.businesscalculation.data.types

data class PreparedShoeMakerData(
    var objectName: String = "",
    var employeeName: String = "",
    var addedTime: String = "",
    val modelName: String = "",
    val color: String = "",
    val countPair: Int = 0,
    val skinType: String = "",
    val comment: String = ""
)
