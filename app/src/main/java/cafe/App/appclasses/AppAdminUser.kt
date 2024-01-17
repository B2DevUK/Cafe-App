package cafe.App.appclasses

class AppAdminUser {

    // Variable declarations
    var email: String? = null;
    private var password: String? = null;
    var name: String? = null;

    constructor() // No-Args constructor

    // Constructor for account creation via email & password
    constructor(email: String, password: String) {
        this.email = email;
        this.password = password;
    }

    // Function for password retrieval (private variable)
    fun getPassword(): String? {
        return password;
    }
}