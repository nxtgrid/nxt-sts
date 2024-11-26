package co.nxtgrid.tokens.service.impl.generate;

import co.nxtgrid.token.domain.*;
import co.nxtgrid.token.domain.base.BitString;
import co.nxtgrid.token.domain.encryptionalgorithm.DataEncryptionAlgorithm;
import co.nxtgrid.token.domain.encryptionalgorithm.EncryptionAlgorithm;
import co.nxtgrid.token.domain.encryptionalgorithm.Misty1AlgorithmEncryptionAlgorithm;
import co.nxtgrid.token.domain.encryptionalgorithm.StandardTransferAlgorithmEncryptionAlgorithm;
import co.nxtgrid.token.domain.keys.decoder.DecoderKey;
import co.nxtgrid.token.domain.supplygroupcode.SupplyGroupCode;
import co.nxtgrid.token.exceptions.*;
import co.nxtgrid.tokens.service.impl.exceptions.InvalidIndividualAccountIdentificationNumber;
import co.nxtgrid.tokens.service.impl.generate.exceptions.InvalidEncryptionAlgorithmException;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;

import java.util.Map;
import java.util.Random;

public abstract class Generator {

    public enum TokenType {
        NATIVE, PRISM_THRIFT
    }

    protected DecoderKey decoderKey;
    private TokenType tokenType;
    private String requestID;

    public Generator(String requestID, TokenType tokenType){
        setRequestID(requestID);
        setTokenType(tokenType);
    }

    protected Generator setDecoderKey(DecoderKey decoderKey) {
        this.decoderKey = decoderKey;
        return this;
    }

    protected String getRequestID() {
        return requestID;
    }

    protected void setRequestID(String requestID) {
        this.requestID = requestID;
    }

    protected TokenType getTokenType() {
        return tokenType;
    }

    protected void setTokenType(TokenType tokenType) {
        this.tokenType = tokenType;
    }

    private static BaseDate getBaseDate(String baseDate)
            throws InvalidBaseDateException {
        switch (baseDate) {
            case ("1993"):
                return BaseDate._1993;
            case("2014"):
                return BaseDate._2014;
            case("2035"):
                return BaseDate._2035;
            default:
                throw new InvalidBaseDateException(String.format("Invalid base date %s"));
        }
    }

    public static TokenIdentifier getTokenIdentifier(Map<String, String> params)
            throws InvalidBaseDateException {
        String dateTime = params.get("token_id");
        String baseDate = params.get("base_date");
        DateTime dateOfIssue = DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm").parseDateTime(dateTime);
        return new TokenIdentifier(dateOfIssue, getBaseDate(baseDate));
    }

    public static RandomNo getRandomNo(Map<String, String> params)
            throws Exception {
        Long randomNo = params.containsKey("random_no")
                ? Long.parseLong(params.get("random_no"))
                : new Random().nextInt(10);
        BitString randomBitString = new BitString(randomNo);
        randomBitString.setLength(4);
        return new RandomNo(randomBitString);
    }

    public static Amount getAmount(Map<String, String> params)
        throws Exception {
        String amount = params.get("amount");
        return new Amount(Double.parseDouble(amount));
    }

    public static KeyExpiryNumber getKeyExpiryNumber(Map<String, String> params)
        throws Exception {
        String keyExpiryNumber = params.get("key_expiry_no");
        return new KeyExpiryNumber(Long.parseLong(keyExpiryNumber));
    }

    public static EncryptionAlgorithm getEncryptionAlgorithm(Map<String, String> params)
        throws Exception {
        String encryptionAlgorithm = params.get("encryption_algorithm");
        switch (encryptionAlgorithm) {
            case ("sta"):
                return new StandardTransferAlgorithmEncryptionAlgorithm();
            case("dea"):
                return new DataEncryptionAlgorithm();
            case("misty1"):
                return new Misty1AlgorithmEncryptionAlgorithm();
            default:
                throw new InvalidEncryptionAlgorithmException(
                        String.format("Invalid Encryption algorithm %s", encryptionAlgorithm));
        }
    }

    public static String getHost(Map<String, String> params) {
        return params.get("host");
    }

    public static int getPort(Map<String, String> params) {
        return Integer.parseInt(params.get("port"));
    }

    public static String getRealm(Map<String, String> params) {
        return params.get("realm");
    }

    public static String getUsername(Map<String, String> params) {
        return params.get("username");
    }

    public static String getPassword(Map<String, String> params) {
        return params.get("password");
    }

    public static SupplyGroupCode getSupplyGroupCode(Map<String, String> params)
            throws InvalidSupplyGroupCodeException  {
        return new SupplyGroupCode(params.get("supply_group_code"));
    }

    public static KeyRevisionNumber getKeyRevisionNumber(Map<String, String> params)
            throws InvalidKeyRevisionNumber  {
        return new KeyRevisionNumber(Integer.parseInt(params.get("key_revision_no")));
    }

    public static TariffIndex getTariffIndex(Map<String, String> params)
            throws InvalidTariffIndexException  {
        return new TariffIndex(params.get("tariff_index"));
    }

    public static TokenCarrierType getTokenCarrierType(Map<String, String> params) {
        return new TokenCarrierType(TokenCarrierType.Code.valueOf(params.get("token_carrier_type").toUpperCase()));
    }

    public static IndividualAccountIdentificationNumber
        getIndividualAccountIdentificationNumber(Map<String, String> params)
            throws InvalidIndividualAccountIdentificationNumber, InvalidManufacturerCodeException,
                    InvalidDecoderSerialNumberException, InvalidDrnCheckDigitException,
                    InvalidIssuerIAINComponents{
        return new IndividualAccountIdentificationNumber(params.get("decoder_reference_number"));
    }


    protected DecoderKey generateDecoderKey(Map<String, String> params)
            throws Exception {
        DecoderKeyGeneratorManager manager = new DecoderKeyGeneratorManager();
        manager.initializeDecoderKeyGenerationParameters(params);
        return manager.generateDecoderKey(params);
    }
}
