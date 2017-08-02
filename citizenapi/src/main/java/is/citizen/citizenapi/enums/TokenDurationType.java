package is.citizen.citizenapi.enums;

public enum TokenDurationType {

    MINUTE,
    DAY,
    WEEK,
    MONTH,
    YEAR, // billing, typically
    PERPETUAL // see token service for limits
}
