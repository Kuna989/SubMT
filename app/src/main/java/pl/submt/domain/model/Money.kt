package pl.submt.domain.model

/**
 * An amount of money in minor units (grosze, cents) together with its ISO 4217 currency code.
 * Integer minor units avoid floating point rounding errors when summing costs.
 */
data class Money(
    val amountMinor: Long,
    val currencyCode: String,
) {
    operator fun plus(other: Money): Money {
        require(currencyCode == other.currencyCode) {
            "Cannot add $currencyCode and ${other.currencyCode}; convert first"
        }
        return copy(amountMinor = amountMinor + other.amountMinor)
    }

    operator fun times(factor: Int): Money = copy(amountMinor = amountMinor * factor)

    companion object {
        fun zero(currencyCode: String) = Money(0, currencyCode)
    }
}
