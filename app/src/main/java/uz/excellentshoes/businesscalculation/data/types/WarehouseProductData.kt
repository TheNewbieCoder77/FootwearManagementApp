package uz.excellentshoes.businesscalculation.data.types

data class WarehouseProductData(
    var objectName: String = "",
    var productName: String = "",
    var productCount: Int = 0,
    var productPrice: Double = 0.0,
    var productUnit: String = "",
    var productColor: String = "",
    var anotherInfo: String = ""
)