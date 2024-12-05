package uz.excellentshoes.businesscalculation.data.types

data class MoneySpendData(
    var objectName: String = "",
    var addedTime: String = "",
    var employeeName: String = "",
    var spendType: String = "",
    var kontragentName: String = "",
    var moneyAmount: Double = 0.0,
    var checked: Boolean = false,
    var comment: String = ""
)
