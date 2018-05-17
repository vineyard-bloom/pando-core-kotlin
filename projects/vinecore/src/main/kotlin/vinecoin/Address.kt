package vinecoin

import java.security.KeyPair

typealias Address = String

data class AddressData(
    val address: Address,
    val data: String
)