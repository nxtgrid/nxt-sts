package co.nxtgrid.token.domain.token.class2;

import co.nxtgrid.token.domain.KeyExpiryNumberLowOrder;
import co.nxtgrid.token.domain.NewKeyLowOrder;
import co.nxtgrid.token.domain.TariffIndex;
import co.nxtgrid.token.domain.base.BitString;
import co.nxtgrid.token.domain.tokenclass.class2.Set2ndSectionDecoderKeyTokenClass;
import co.nxtgrid.token.domain.tokensubclass.class2.Set2ndSectionDecoderKeyTokenSubClass;
import co.nxtgrid.token.exceptions.*;
import co.nxtgrid.token.generators.tokensdecoder.error.CRCError;

import java.util.HashMap;

public class Set2ndSectionDecoderKeyToken extends Class2Token {

    private KeyExpiryNumberLowOrder keyExpiryNumberLowOrder;
    private TariffIndex tariffIndex;
    private NewKeyLowOrder newKeyLowOrder;

    public Set2ndSectionDecoderKeyToken(String requestID)
            throws InvalidRangeException {
        super(requestID);
        setTokenClass(new Set2ndSectionDecoderKeyTokenClass());
        setTokenSubClass(new Set2ndSectionDecoderKeyTokenSubClass());
    }

    public Set2ndSectionDecoderKeyToken(String requestID,
                                        KeyExpiryNumberLowOrder keyExpiryNumberLowOrder,
                                        TariffIndex tariffIndex,
                                        NewKeyLowOrder newKeyLowOrder) throws InvalidRangeException {
        super(requestID);
        setTokenClass(new Set2ndSectionDecoderKeyTokenClass());
        setTokenSubClass(new Set2ndSectionDecoderKeyTokenSubClass());
        setKeyExpiryNumberLowOrder(keyExpiryNumberLowOrder);
        setTariffIndex(tariffIndex);
        setNewKeyLowOrder(newKeyLowOrder);
    }

    @Override
    public String getType() {
        return "Set2ndSectionDecoderKey_24";
    }

    public String getBitString() {
        return encryptedTokenBitString;
    }

    public KeyExpiryNumberLowOrder getKeyExpiryNumberLowOrder() {
        return keyExpiryNumberLowOrder;
    }

    public void setKeyExpiryNumberLowOrder(KeyExpiryNumberLowOrder keyExpiryNumberLowOrder) {
        this.keyExpiryNumberLowOrder = keyExpiryNumberLowOrder;
    }

    public TariffIndex getTariffIndex() {
        return tariffIndex;
    }

    public void setTariffIndex(TariffIndex tariffIndex) {
        this.tariffIndex = tariffIndex;
    }

    public NewKeyLowOrder getNewKeyLowOrder() {
        return newKeyLowOrder;
    }

    public void setNewKeyLowOrder(NewKeyLowOrder newKeyLowOrder) {
        this.newKeyLowOrder = newKeyLowOrder;
    }

    @Override
    public HashMap<String, Object> getParams() {
        HashMap<String, Object> params = new HashMap<>();
        params.put("class", getTokenClass().getBitString().getValue());
        params.put("subclass", getTokenSubClass().getBitString().getValue());
        params.put("type", getType());

        if (getKeyExpiryNumberLowOrder() != null)
            params.put("key_expiry_number_low_order", getKeyExpiryNumberLowOrder().getKenloBitString().getValue());

        if (getTariffIndex() != null)
            params.put("tariff_index", getTariffIndex().getValue());

        if (getNewKeyLowOrder() != null)
            params.put("new_key_low_order", getNewKeyLowOrder().getNkloBitString().getValue());

        return params;
    }

    public void decode(BitString decryptedTokenBitString,
                       BitString encryptedTokenBitString) throws
            InvalidRangeException, CRCError,
            InvalidKenloException, InvalidTariffIndexException,
            InvalidNKLOException, BitConcatOverflowError {
        if (checkCrc(decryptedTokenBitString, getTokenClass())) {
            setKeyExpiryNumberLowOrder(new KeyExpiryNumberLowOrder(decryptedTokenBitString.extractBits(56, 4)));
            setTariffIndex(new TariffIndex(decryptedTokenBitString.extractBits(48, 8)));
            setNewKeyLowOrder(new NewKeyLowOrder(decryptedTokenBitString.extractBits(16, 32)));
            setEncryptedTokenBitString(Long.toBinaryString(encryptedTokenBitString.getValue()));
            setDecryptedTokenBitString(Long.toBinaryString(decryptedTokenBitString.getValue()));
        }
    }
}
