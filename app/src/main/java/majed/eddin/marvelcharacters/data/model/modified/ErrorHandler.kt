package majed.eddin.marvelcharacters.data.model.modified

data class ErrorHandler(var key: String?) {

    var value: String? = null
    var isDataValid: Boolean = true

    constructor(key: String, value: String?) : this(key) {
        this.value = value
        this.isDataValid = false
    }

    constructor(isDataValid: Boolean) : this(null) {
        this.value = null
        this.isDataValid = isDataValid
    }


    override fun toString(): String {
        return "ErrorHandler(key=$key, value=$value, isDataValid=$isDataValid)"
    }

}