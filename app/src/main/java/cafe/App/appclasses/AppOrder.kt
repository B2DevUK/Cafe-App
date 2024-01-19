package cafe.App.appclasses

class AppOrder {

    //declaring variables
    var OrderID: Int? = null;
    var CustomerID: Int? = null;
    var ProductID: Int? = null;
    var isReady: Boolean = false;
    var price: Double? = null;

    //primary constructor
    constructor()

    //secondary constructor
    constructor(OrderId: Int, CustomerID: Int, ProductID: Int, isReady: Boolean, price: Double) {

        //sets values
        this.OrderID = OrderID;
        this.CustomerID = CustomerID;
        this.ProductID = ProductID;
        this.isReady = isReady;
        this.price = price;
    }
    //get functions
    fun getOrderID(): Int? {
        return OrderID;
    }
    fun getCustomerID(): Int? {
        return CustomerID;
    }
    fun getProductID(): Int? {
        return ProductID;
    }
    fun getIsReady(): Boolean {
        return isReady;
    }
    fun getPrice(): Double? {
        return price;
    }

    //set functions
    fun setOrderID(): Int? {
        this.OrderID = OrderID;
        return OrderID;
    }
    fun setCustomerID(): Int? {
        this.CustomerID = CustomerID;
        return CustomerID;
    }
    fun setProductID(): Int? {
        this.ProductID = ProductID;
        return ProductID;
    }
    fun setIsReady() : Boolean {
        this.isReady = isReady;
        return isReady;
    }
    fun setPrice() : Double? {
        this.price = price;
        return price;
    }

    //function to declare and order ready
    fun declareReady() : Boolean {
        isReady = true;
        return isReady;
    }





}