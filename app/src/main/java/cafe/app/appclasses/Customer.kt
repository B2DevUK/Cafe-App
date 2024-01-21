package cafe.app.appclasses

data class Customer(
    val id: Int,
    val fullName: String,
    val email: String,
    val phoneNo: String,
    val userName: String,
    val password: String,
    val isActive: Int
)
