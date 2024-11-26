package co.nxtgrid.tokens.constant;

public interface StringConstants {

    // -- Generic Error Codes
    int SUCCESS_CODE = 200;
    int BAD_REQUEST = 400;
    int INVALID_REQUEST = 405;
    int INTERNAL_SERVER_ERROR = 500;
    int EXCEPTION_CODE=600;
    int ALREADY_EXIST_CODE=700;
    int PHONE_NUMBER_MAX_LENGTH=10;

    // -- Specific Error Messages
    String INVALID_TOKEN_REF = "Invalid token ref";
    String INVALID_TOKEN_NO = "Invalid token no";
    String INVALID_TOKEN_PARAMS = "Invalid token parameters";
    String INVALID_TOKEN_PARAM = "Invalid token parameter";
    String EMPTY_REF_VALUE= "Ref value should not be empty";
    String EMPTY_TOKEN_NO_VALUE = "Token No should not be empty";
    String EMPTY_USER_NAME_VALUE= "User Name should not be empty";
    String NO_CLASS_OR_SUBCLASS = "No class or subclass provided";
    String TOKEN_NOT_AVAILABLE = "Token not generated or available";
    String SUCCESS_TOKEN_GENERATED = "Successfully generated token";
    String SUCCESS_USER_TOKEN_DETAILS = "Successfully obtained user token details";
    String SUCCESS_TOKEN_DETAILS = "Successfully obtained token details";
    String DELETED_MSG_TOKEN_BY_REF = "Successfully deleted token";
    String DELETED_MSG_TOKEN_BY_TOKEN_NO = "Successfully deleted token by token no";
    String INVALID_DECODER_KEY_GENERATION_ALGORITHM = "Invalid decoder key generation algorithm";
    String  INVALID_BASE_DATE = "Invalid base date %s";
    String INVALID_ENCRYPTION_ALGORITHM = "Invalid encryption algorithm %s";
    String INVALID_AGGREGATE_TOKEN_PARAM = "Invalid aggregate token parameter";
    String INVALID_TOKEN = "Invalid token";
    String SUCCESS_TOKEN_DECODED = "Successfully decoded token";
    String INVALID_USER_REF = "Invalid user ref";
}
