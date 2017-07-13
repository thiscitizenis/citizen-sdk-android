package is.citizen.citizenapi.util;

/**
 * Simple constant class
 */
public final class Constant {
    private Constant() {
    }

    public static final String CITIZEN_API_URL                            = "http://192.168.1.76:8080/";

    public static final String CITIZEN_USER_RESOURCE                      = "users",
                               CITIZEN_SESSION_RESOURCE                   = "sessions",
                               CITIZEN_TOKEN_RESOURCE                     = "tokens",
                               CITIZEN_PERSON_RESOURCE                    = "persons",
                               CITIZEN_PHONE_RESOURCE                     = "phones";


    public static final String CITIZEN_FINGERPRINT_AUTH_KEY               = "FINGERPRINT_AUTH_KEY";

    public static final int    CITIZEN_SIGNUP_CODE_SUCCESS                =  1,
                               CITIZEN_SIGNUP_CODE_FAIL                   = -1,
                               CITIZEN_SIGNUP_CODE_MISSING_PARAMETERS     = -2,
                               CITIZEN_SIGNUP_CODE_UNAUTHORIZED           = -3,
                               CITIZEN_SIGNUP_CODE_DUPLICATE_USER         = -4,
                               CITIZEN_SIGNUP_CODE_HTTP_ERROR             = -5;

    public static final int    CITIZEN_LOGIN_CODE_SUCCESS                 =  1,
                               CITIZEN_LOGIN_CODE_FAIL                    = -1,
                               CITIZEN_LOGIN_CODE_MISSING_PARAMETERS      = -2,
                               CITIZEN_LOGIN_CODE_UNAUTHORIZED            = -3,
                               CITIZEN_LOGIN_CODE_USER_NOT_FOUND          = -4,
                               CITIZEN_LOGIN_CODE_HTTP_ERROR              = -5,
                               CITIZEN_LOGIN_CODE_CRYPTO_ERROR            = -6,
                               CITIZEN_LOGIN_CODE_READER_ERROR            = -7;

    public static final int    CITIZEN_REST_CODE_SUCCESS                  =  1,
                               CITIZEN_REST_CODE_FAIL                     = -1,
                               CITIZEN_REST_CODE_MISSING_PARAMETERS       = -2,
                               CITIZEN_REST_CODE_UNAUTHORIZED             = -3,
                               CITIZEN_REST_CODE_USER_NOT_FOUND           = -4,
                               CITIZEN_REST_CODE_HTTP_ERROR               = -5,
                               CITIZEN_REST_CODE_CRYPTO_ERROR             = -6,
                               CITIZEN_REST_CODE_TOKEN_NOT_FOUND          = -7,
                               CITIZEN_REST_INVALID_TOKEN_STATUS          = -8,
                               CITIZEN_REST_CODE_PERSON_NOT_FOUND         = -9,
                               CITIZEN_REST_CODE_PHONE_NOT_FOUND          = -10,
                               CITIZEN_REST_CODE_MNEMONIC_CODE_NOT_FOUND  = -11;


    public static final String CITIZEN_SORT                               = "sort",
                               CITIZEN_SORT_STATUS                        = "sortStatus",
                               CITIZEN_SORT_DATE                          = "sortDate",
                               CITIZEN_SORT_TYPE                          = "sortType";
}
