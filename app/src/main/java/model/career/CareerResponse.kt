package model.career

data class CareerResponse(
    val resultVal: Int,
    val resultMsg: String,
    val careers: List<Career>
)
