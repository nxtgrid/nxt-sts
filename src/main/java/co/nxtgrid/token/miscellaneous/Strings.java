package co.nxtgrid.token.miscellaneous;

public class Strings {

    // Strings and abbreviations of keys
    public static final String VENDING_KEY_NAME = "Vending Key";

    // Exceptions
    public static final String ILLEGAL_BITSTRING_LENGTH_COMPARISON = "Bit strings of different lengths being compared";
    public static final String BIT_CONCAT_OVERFLOW_ERROR = "Concatenated bit strings would exceed max number of available bits";
    public static final String INVALID_RANGE_SET = "Range will exceed length of max bit string length";
    public static final String TOO_LONG_BIT_RANGE = "Bit domain.range required will exceed maximum number of bits";
    public static final String ILLEGAL_NIBBLE_BITSTRING = "Invalid bit string supplied for nibble";
    public static final String BITS_SET_LENGTH_DIFF_RANGE = "The bits to be set and the domain.range provided are not equivalent";
    public static final String EXTRACT_ONE_LESS_THAN_BITS = "Trying to extract 0 or less bits from bitstring";
    public static final String NIBBLE_OUT_OF_RANGE = "Desired nibble out of domain.range";
    public static final String BIT_STRING_SIZE_ERROR = "Bit String length must be > than 0 and have the required number of bits";
    public static final String INVALID_BIT = "Invalid bit %d set";
    public static final String INVALID_STEPS_EXCEPTION = "No of steps must be a multiple of 15" ;
    public static final String METER_COMPONENT_LENGTHS_INVALID = "Invalid length of MeterPAN components";
    public static final String INVALID_ISSUER_IDENTIFICATION_NUMBER = "Invalid Issuer Identification Number";
    public static final String INVALID_IAIN_COMPONENTS = "Invalid Individual Account Identification Number";
    public static final String INVALID_MANUFACTURER_CODE = "Invalid Manufacturer code";
    public static final String INVALID_IIN_ZEROS =  "Invalid IIN. When the IAIN has 13 digits, the IIN should be 0000";
    public static final String INVALID_DECODER_SERIAL_NUMBER = "Invalid decoder serial number";
    public static final String INVALID_DRN_CHECK_DIGIT = "Invalid DRN check digit";
    public static final String INVALID_PAN_CHECK_DIGIT = "Invalid Pan Check Digit";
    public static final String INVALID_SUPPLY_GROUP_CODE = "Invalid Supply Group Code";
    public static final String INVALID_TARIFF_INDEX = "Invalid Tariff Index";
    public static final String INVALID_KEY_REVISION_NUMBER = "Invalid Key Revision Number";
    public static final String INVALID_KEY_TYPE = "Invalid Key Type";
    public static final String INVALID_KEY_EXPIRY_NUMBER = "Invalid Key Expiry Number";
    public static final String INVALID_DATE_OF_EXPIRY_EXCEPTION = "Invalid Date of Expiry Exception";
    public static final String INVALID_PRIMARY_ACCOUNT_NUMBER_BLOCK = "Invalid Primary Account Number Block Parameters";
    public static final String KEY_REVISION_NUMBER_EXCEPTION = "Invalid Key Revision Number";
    public static final String INVALID_DECODER_KEY_PARAMETERS = "Invalid Decoder Key Parameters Parameters";
    public static final String INVALID_CONTROL_BIT_STRING = "Invalid Control Bit String";
    public static final String INVALID_REGISTER_BITSTRING = "Invalid Register BitString";
    public static final String INVALID_PAD_EXCEPTION = "Invalid Pad";
    public static final String INVALID_KENHO_EXCEPTION = "Invalid Key Expiry Number High Order (KENHO)";
    public static final String INVALID_SGCHO_EXCEPTION = "Invalid Supply Group Code High Order (SGCHO)";
    public static final String INVALID_SGCLO_EXCEPTION = "Invalid Supply Group Code Low Order (SGCLO)";
    public static final String INVALID_ROLL_OVER_KEY_CHANGE = "Invalid Roll Over Key Change";
    public static final String INVALID_NKHO = "Invalid New Key High Order (NKHO)";
    public static final String INVALID_KENLO = "Invalid Key Expiry Number Low Order";
    public static final String INVALID_NKLO = "Invalid New Key Low Order";
    public static final String INVALID_MPPU = "Invalid Maximum Phase Power Unbalance maximumPhasePowerUnbalanceLimit";
    public static final String INVALID_MPL_SIZE_ERROR = "Invalid Maximum Power Limit";
    public static final String INVALID_RATE = "Invalid Rate";
    public static final String INVALID_WMF = "Invalid Water Meter Factor";
    public static final String INVALID_NEW_KEY_MIDDLE_ORDER_1_EXCEPTION = "Invalid New Key Middle Order 1 Exception";
    public static final String INVALID_NEW_KEY_MIDDLE_ORDER_2_EXCEPTION = "Invalid New Key Middle Order 2 Exception";
    public static final String INVALID_METER_PAN = "Invalid Meter Primary Account Number";
    public static final String INVALID_BITSTRING = "Invalid BitString";
    public static final String INVALID_VENDING_OR_DECODER_KEY_EXCEPTION = "Invalid Vending or Decoder Key Exception";
    public static final String INVALID_IAIN = "Invalid Individual Account Identification Number";
    public static final String INVALID_SUBSTITUTION_DATA_BLOCK = "Invalid Substitution Data Block";
    public static final String INVALID_TOKEN_CLASS = "Invalid token class";
    public static final String INVALID_TOKEN_SUBCLASS = "Invalid token subclass";
    public static final String INVALID_TOKEN_SUBCLASS_SPECIFIC = "Invalid token subclass %d";
    public static final String INVALID_CONTROL = "Invalid control";

    // System messages
    public static final String ENTER_DESIRED_UNITS = "Enter desired units: ";
    public static final String INVALID_TOKEN = "Invalid token entered";
    public static final String ENTER_TOKEN = "Enter valid 20 digit token (e.g. 51043465443420856213): ";
    public static final String INVALID_NO_UNITS = "Invalid number of bits for quantity" ;
}