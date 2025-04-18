import com.shk.hiltfeed.data.local.dto.UserDto

data class UserData(
    val users: List<User>,
    val total: Long,
    val skip: Long,
    val limit: Long,
)

data class User(
    val id: Long,
    val firstName: String,
    val lastName: String,
    val maidenName: String,
    val age: Long,
    val gender: String,
    val email: String,
    val phone: String,
    val username: String,
    val password: String,
    val birthDate: String,
    val image: String,
    val bloodGroup: String,
    val height: Double,
    val weight: Double,
    val eyeColor: String,
    val hair: Hair,
    val ip: String,
    val address: Address,
    val macAddress: String,
    val university: String,
    val bank: Bank,
    val company: Company,
    val ein: String,
    val ssn: String,
    val userAgent: String,
    val crypto: Crypto,
    val role: String,
) {
    companion object {
        fun toUserDtoList(users: List<User?>?): List<UserDto> {
            val userDtoList = mutableListOf<UserDto>()
            users?.forEach {
                it?.let { it1 -> toUserDto(it1) }?.let { it2 -> userDtoList.add(it2) }
            }
            return userDtoList
        }

        fun toUserDto(user: User): UserDto {
            return UserDto(
                id = user.id,
                firstName = user.firstName,
                lastName = user.lastName,
                maidenName = user.maidenName,
                age = user.age,
                gender = user.gender,
                email = user.email,
                phone = user.phone,
                username = user.username,
                password = user.password,
                birthDate = user.birthDate,
                image = user.image,
                bloodGroup = user.bloodGroup,
                height = user.height,
                weight = user.weight,
                eyeColor = user.eyeColor,
                ip = user.ip,
                address = user.address.address,
                macAddress = user.macAddress,
                university = user.university,
                company = user.company.name,
                ein = user.ein,
                ssn = user.ssn,
                userAgent = user.userAgent,
                role = user.role,
            );
        }
    }
}

data class Hair(
    val color: String,
    val type: String,
)

data class Address(
    val address: String,
    val city: String,
    val state: String,
    val stateCode: String,
    val postalCode: String,
    val coordinates: Coordinates,
    val country: String,
)

data class Coordinates(
    val lat: Double,
    val lng: Double,
)

data class Bank(
    val cardExpire: String,
    val cardNumber: String,
    val cardType: String,
    val currency: String,
    val iban: String,
)

data class Company(
    val department: String,
    val name: String,
    val title: String,
    val address: Address2,
)

data class Address2(
    val address: String,
    val city: String,
    val state: String,
    val stateCode: String,
    val postalCode: String,
    val coordinates: Coordinates2,
    val country: String,
)

data class Coordinates2(
    val lat: Double,
    val lng: Double,
)

data class Crypto(
    val coin: String,
    val wallet: String,
    val network: String,
)
