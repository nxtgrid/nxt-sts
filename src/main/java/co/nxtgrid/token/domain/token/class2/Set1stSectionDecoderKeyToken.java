package co.nxtgrid.token.domain.token.class2;

import co.nxtgrid.token.domain.*;
import co.nxtgrid.token.domain.base.BitString;
import co.nxtgrid.token.domain.tokenclass.class2.Set1stSectionDecoderKeyTokenClass;
import co.nxtgrid.token.domain.tokensubclass.class2.Set1stSectionDecoderKeyTokenSubClass;
import co.nxtgrid.token.exceptions.*;
import co.nxtgrid.token.generators.tokensdecoder.error.CRCError;

import java.util.HashMap;

public class Set1stSectionDecoderKeyToken extends Class2Token {

    private KeyExpiryNumberHighOrder keyExpiryNumberHighOrder;
    private KeyRevisionNumber keyRevisionNumber;
    private RolloverKeyChange rolloverKeyChange;
    private KeyType keyType;
    private NewKeyHighOrder newKeyHighOrder;
    private _3KCT _3KCT;

    public Set1stSectionDecoderKeyToken(String requestID)
            throws InvalidRangeException {
        super(requestID);
        setTokenClass(new Set1stSectionDecoderKeyTokenClass());
        setTokenSubClass(new Set1stSectionDecoderKeyTokenSubClass());
    }

    public Set1stSectionDecoderKeyToken(String requestID,
                                        KeyExpiryNumberHighOrder keyExpiryNumberHighOrder,
                                        KeyRevisionNumber keyRevisionNumber,
                                        RolloverKeyChange rolloverKeyChange,
                                        KeyType keyType,
                                        NewKeyHighOrder newKeyHighOrder)
            throws InvalidBitStringException, InvalidRangeException {
        super(requestID);
        setTokenClass(new Set1stSectionDecoderKeyTokenClass());
        setTokenSubClass(new Set1stSectionDecoderKeyTokenSubClass());
        setKeyExpiryNumberHighOrder(keyExpiryNumberHighOrder);
        setKeyRevisionNumber(keyRevisionNumber);
        setRolloverKeyChange(rolloverKeyChange);
        setKeyType(keyType);
        setNewKeyHighOrder(newKeyHighOrder);
        set3KCT(new _3KCT(new BitString("0"))); // set to '0' since the 3rd Section Decoder Key is not in set
                                                // This bit is also called Res_B reserved for the
                                                // 128-bit decoder key transfer where it is also required
                                                // to be set to 0.
                                                // IEC 62055-41:2018, page 35, 36
    }

    @Override
    public String getType() {
        return "Set1stSectionDecoderKey_23";
    }

    public String getBitString() {
        return encryptedTokenBitString;
    }

    public KeyExpiryNumberHighOrder getKeyExpiryNumberHighOrder() {
        return keyExpiryNumberHighOrder;
    }

    public void setKeyExpiryNumberHighOrder(KeyExpiryNumberHighOrder keyExpiryNumberHighOrder) {
        this.keyExpiryNumberHighOrder = keyExpiryNumberHighOrder;
    }

    public KeyRevisionNumber getKeyRevisionNumber() {
        return keyRevisionNumber;
    }

    public void setKeyRevisionNumber(KeyRevisionNumber keyRevisionNumber) {
        this.keyRevisionNumber = keyRevisionNumber;
    }

    public RolloverKeyChange getRolloverKeyChange() {
        return rolloverKeyChange;
    }

    public void setRolloverKeyChange(RolloverKeyChange rolloverKeyChange) {
        this.rolloverKeyChange = rolloverKeyChange;
    }

    public KeyType getKeyType() {
        return keyType;
    }

    public void setKeyType(KeyType keyType) {
        this.keyType = keyType;
    }

    public NewKeyHighOrder getNewKeyHighOrder() {
        return newKeyHighOrder;
    }

    public void setNewKeyHighOrder(NewKeyHighOrder newKeyHighOrder) {
        this.newKeyHighOrder = newKeyHighOrder;
    }

    @Override
    public HashMap<String, Object> getParams() {
        HashMap<String, Object> params = new HashMap<>();
        params.put("class", getTokenClass().getBitString().getValue());
        params.put("subclass", getTokenSubClass().getBitString().getValue());
        params.put("type", getType());

        if (getKeyExpiryNumberHighOrder() != null)
            params.put("key_expiry_number_high_order", getKeyExpiryNumberHighOrder().getBitString().getValue());

        if (getKeyRevisionNumber() != null)
                params.put("key_revision_number", getKeyRevisionNumber().getValue());

        if (getRolloverKeyChange() != null)
            params.put("roll_over_key_change", getRolloverKeyChange().getBitString().getValue());

        if (getKeyType() != null)
            params.put("key_type", getKeyType().getValue());

        if (getNewKeyHighOrder() != null)
            params.put("new_key_high_order", getNewKeyHighOrder().getBitString().getValue());

        if (get3KCT() != null)
            params.put("3_kct", get3KCT().getBitString().getValue());

        return params;
    }

    public void decode(BitString decryptedTokenBitString,
                       BitString encryptedTokenBitString) throws
            InvalidRangeException, CRCError,
            InvalidBitStringException, InvalidNKHOException,
            InvalidKeyTypeException, InvalidRollOverKeyChangeException,
            InvalidKeyRevisionNumber, InvalidKenhoException, BitConcatOverflowError {
        if (checkCrc(decryptedTokenBitString, getTokenClass())) {
            setKeyExpiryNumberHighOrder(new KeyExpiryNumberHighOrder(decryptedTokenBitString.extractBits(56, 4)));
            setKeyRevisionNumber(new KeyRevisionNumber((int) decryptedTokenBitString.extractBits(52, 4).getValue()));
            setRolloverKeyChange(new RolloverKeyChange(decryptedTokenBitString.extractBits(51, 1)));
            setKeyType(new KeyType((int) decryptedTokenBitString.extractBits(48, 2).getValue()));
            setNewKeyHighOrder(new NewKeyHighOrder(decryptedTokenBitString.extractBits(16, 32)));
            set3KCT(new _3KCT(new BitString("0")));
            setEncryptedTokenBitString(Long.toBinaryString(encryptedTokenBitString.getValue()));
            setDecryptedTokenBitString(Long.toBinaryString(decryptedTokenBitString.getValue()));
        }
    }

    public _3KCT get3KCT() {
        return _3KCT;
    }

    public void set3KCT(_3KCT _3KCT) {
        this._3KCT = _3KCT;
    }
}
