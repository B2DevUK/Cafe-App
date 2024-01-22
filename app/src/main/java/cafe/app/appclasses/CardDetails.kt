package cafe.app.appclasses

data class CardDetails(
    val cardNumber: Long,
    val expiryDate: String,
    var securityNumber: Int,
    var fullName: String
)
