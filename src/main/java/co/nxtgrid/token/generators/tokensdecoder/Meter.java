package co.nxtgrid.token.generators.tokensdecoder;

import co.nxtgrid.token.domain.base.BitString;
import co.nxtgrid.token.domain.encryptionalgorithm.EncryptionAlgorithm;
import co.nxtgrid.token.domain.keys.decoder.DecoderKey;
import co.nxtgrid.token.domain.token.Token;
import co.nxtgrid.token.exceptions.InvalidBitException;
import co.nxtgrid.token.exceptions.InvalidRangeException;
import co.nxtgrid.token.exceptions.InvalidTokenClassException;
import co.nxtgrid.token.exceptions.InvalidTokenException;
import co.nxtgrid.token.exceptions.InvalidTokenSubclassException;
import co.nxtgrid.token.generators.tokensdecoder.class0.TransferElectricityCreditDecoder;
import co.nxtgrid.token.generators.tokensdecoder.class0.TransferGasCreditDecoder;
import co.nxtgrid.token.generators.tokensdecoder.class0.TransferWaterCreditDecoder;
import co.nxtgrid.token.generators.tokensdecoder.class1.InitiateMeterTestOrDisplay1TokenDecoder;
import co.nxtgrid.token.generators.tokensdecoder.class1.InitiateMeterTestOrDisplay2TokenDecoder;
import co.nxtgrid.token.generators.tokensdecoder.class2.ClearCreditTokenDecoder;
import co.nxtgrid.token.generators.tokensdecoder.class2.ClearTamperConditionTokenDecoder;
import co.nxtgrid.token.generators.tokensdecoder.class2.Set1stSectionDecoderKeyTokenDecoder;
import co.nxtgrid.token.generators.tokensdecoder.class2.Set2ndSectionDecoderKeyTokenDecoder;
import co.nxtgrid.token.generators.tokensdecoder.class2.Set3rdSectionDecoderKeyTokenDecoder;
import co.nxtgrid.token.generators.tokensdecoder.class2.Set4thSectionDecoderKeyTokenDecoder;
import co.nxtgrid.token.generators.tokensdecoder.class2.SetMaximumPhasePowerUnbalanceLimitTokenDecoder;
import co.nxtgrid.token.generators.tokensdecoder.class2.SetMaximumPowerLimitTokenDecoder;
import co.nxtgrid.token.generators.tokensdecoder.class2.SetTariffRateTokenDecoder;
import co.nxtgrid.token.generators.tokensdecoder.class2.SetWaterMeterFactorTokenDecoder;
import co.nxtgrid.token.generators.utils.Utils;
import co.nxtgrid.token.miscellaneous.Strings;

import java.math.BigInteger;

public class Meter {

    private BitString _64BitTokenString;
    private DecoderKey decoderKey;
    private String _20DigitToken;
    private String tokenClassBits;
    private EncryptionAlgorithm encryptionAlgorithm;

    public Meter(String _20DigitToken, DecoderKey decoderKey,
                 EncryptionAlgorithm encryptionAlgorithm)
            throws InvalidTokenException, InvalidBitException {
        setTokenString(_20DigitToken);
        set64BitTokenString(_20DigitToken);
        setDecoderKey(decoderKey);
        setEncryptionAlgorithm(encryptionAlgorithm);
    }

    public String getTokenString() {
        return _20DigitToken;
    }

    public EncryptionAlgorithm getEncryptionAlgorithm() {
        return encryptionAlgorithm;
    }

    public void setEncryptionAlgorithm(EncryptionAlgorithm encryptionAlgorithm) {
        this.encryptionAlgorithm = encryptionAlgorithm;
    }

    public void setTokenString(String _20DigitToken)
            throws InvalidTokenException {
        if (_20DigitToken.length() != 20)
            throw new InvalidTokenException(Strings.INVALID_TOKEN);
        this._20DigitToken = _20DigitToken;
    }

    public BitString get64BitTokenString() {
        return _64BitTokenString;
    }

    public void set64BitTokenString(String _20DigitToken)
            throws InvalidTokenException, InvalidBitException {
        if (_20DigitToken.length() != 20)
            throw new InvalidTokenException(Strings.INVALID_TOKEN);
        this._64BitTokenString = convertTo64BitString(_20DigitToken);
    }

    public DecoderKey getDecoderKey() {
        return decoderKey;
    }

    public void setDecoderKey(DecoderKey decoderKey) {
        this.decoderKey = decoderKey;
    }

    protected BitString convertTo64BitString(String token)
            throws InvalidBitException {

        BigInteger tokenValue = new BigInteger(token, 10);
        String convertedTokenValue = tokenValue.toString(2);
        String paddedString = "000000000000000000000000000000000000000000000000000000000000000000"
                .substring(convertedTokenValue.length())
                + convertedTokenValue;

        String replacementBits = paddedString.substring(0, 2);
        tokenClassBits = paddedString.substring(37, 39);

        paddedString = paddedString.substring(2, 37) + replacementBits + paddedString.substring(39);
        return new BitString(Utils.convertBitStringToLong(paddedString));
    }

    public Token decodeNative(String requestID) throws Exception {
        if (tokenClassBits.equals("01")) {
            String tokenSubclassBitString = _64BitTokenString.extractBits(60, 4).toString();
            if (tokenSubclassBitString.equals("0") || tokenSubclassBitString.equals("1")) {
                TokenDecoder tokenDecoder = setTokenDecoder(Integer.parseInt(tokenClassBits, 2),
                        Integer.parseInt(tokenSubclassBitString, 2));
                return tokenDecoder.decode(requestID, _64BitTokenString, _64BitTokenString);
            }
        } else {
            BitString _64bitStringDecryptedDataBlock = encryptionAlgorithm.decrypt(decoderKey, _64BitTokenString);
            String tokenSubclassBitString = _64bitStringDecryptedDataBlock.extractBits(60, 4).toString();
            TokenDecoder tokenDecoder = setTokenDecoder(Integer.parseInt(tokenClassBits, 2),
                    Integer.parseInt(tokenSubclassBitString, 2));
            return tokenDecoder.decode(requestID, _64bitStringDecryptedDataBlock, _64BitTokenString);
        }
        throw new InvalidTokenException(String.format("Unsupported token %s", _20DigitToken));
    }

    private TokenDecoder setTokenDecoder(int tokenClass, int tokenSubclass)
            throws InvalidTokenClassException, InvalidTokenSubclassException {
        if (tokenClass >= 0 && tokenClass <= 2) {
                switch (tokenClass) {
                    case (0):
                        if (tokenSubclass == 0) {
                            return new TransferElectricityCreditDecoder();
                        } else if (tokenSubclass == 1) {
                            return new TransferWaterCreditDecoder();
                        } else if (tokenSubclass == 2) {
                            return new TransferGasCreditDecoder();
                        } else {
                            throw new InvalidTokenSubclassException(String.format(
                                    Strings.INVALID_TOKEN_SUBCLASS_SPECIFIC, tokenSubclass));
                        }

                    case (1):
                        if (tokenSubclass == 0) {
                            return new InitiateMeterTestOrDisplay1TokenDecoder();
                        } else if (tokenSubclass == 1) {
                            return new InitiateMeterTestOrDisplay2TokenDecoder();
                        } else {
                            throw new InvalidTokenSubclassException(String.format(
                                    Strings.INVALID_TOKEN_SUBCLASS_SPECIFIC, tokenSubclass));
                        }

                    case(2):
                        if (tokenSubclass == 0) {
                            return new SetMaximumPowerLimitTokenDecoder();
                        } else if (tokenSubclass == 1) {
                            return new ClearCreditTokenDecoder();
                        } else if (tokenSubclass == 2) {
                            return new SetTariffRateTokenDecoder();
                        } else if (tokenSubclass == 3) {
                            return new Set1stSectionDecoderKeyTokenDecoder();
                        } else if (tokenSubclass == 4) {
                            return new Set2ndSectionDecoderKeyTokenDecoder();
                        } else if (tokenSubclass == 8) {
                            return new Set3rdSectionDecoderKeyTokenDecoder();
                        } else if (tokenSubclass == 9) {
                            return new Set4thSectionDecoderKeyTokenDecoder();
                        } else if (tokenSubclass == 5) {
                            return new ClearTamperConditionTokenDecoder();
                        } else if (tokenSubclass == 6) {
                            return new SetMaximumPhasePowerUnbalanceLimitTokenDecoder();
                        } else if (tokenSubclass == 7) {
                            return new SetWaterMeterFactorTokenDecoder();
                        } else {
                            throw new InvalidTokenSubclassException(String.format(
                                    Strings.INVALID_TOKEN_SUBCLASS_SPECIFIC, tokenSubclass));
                        }

                    default:
                        throw new InvalidTokenClassException(Strings.INVALID_TOKEN_CLASS);
                }

        } else {
            throw new InvalidTokenClassException(Strings.INVALID_TOKEN_CLASS);
        }
    }
}
