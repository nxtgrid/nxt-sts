package co.nxtgrid.token.domain.token.class2;

import co.nxtgrid.token.domain.NewKeyMiddleOrder1;
import co.nxtgrid.token.domain.base.BitString;
import co.nxtgrid.token.domain.supplygroupcode.SupplyGroupCodeHighOrder;
import co.nxtgrid.token.domain.tokenclass.class2.Set4thSectionDecoderKeyTokenClass;
import co.nxtgrid.token.domain.tokensubclass.class2.Set4thSectionDecoderKeyTokenSubClass;
import co.nxtgrid.token.exceptions.BitConcatOverflowError;
import co.nxtgrid.token.exceptions.InvalidNewKeyMiddleOrder1Exception;
import co.nxtgrid.token.exceptions.InvalidRangeException;
import co.nxtgrid.token.exceptions.InvalidSgchoException;
import co.nxtgrid.token.exceptions.decode.CRCError;

import java.util.HashMap;

public class Set4thSectionDecoderKeyToken extends Class2Token {

    private SupplyGroupCodeHighOrder supplyGroupCodeHighOrder;
    private NewKeyMiddleOrder1 newKeyMiddleOrder1;

    public Set4thSectionDecoderKeyToken(String requestID)
            throws InvalidRangeException {
        super(requestID);
        setTokenClass(new Set4thSectionDecoderKeyTokenClass());
        setTokenSubClass(new Set4thSectionDecoderKeyTokenSubClass());
    }

    public Set4thSectionDecoderKeyToken(String requestID,
                                        SupplyGroupCodeHighOrder supplyGroupCodeHighOrder,
                                        NewKeyMiddleOrder1 newKeyMiddleOrder1)
        throws InvalidRangeException  {
        super(requestID);
        setTokenClass(new Set4thSectionDecoderKeyTokenClass());
        setTokenSubClass(new Set4thSectionDecoderKeyTokenSubClass());
        setSupplyGroupCodeHighOrder(supplyGroupCodeHighOrder);
        setNewKeyMiddleOrder1(newKeyMiddleOrder1);
    }

    @Override
    public String getType() {
        return "Set4thSectionDecoderKey_29";
    }

    public String getBitString() {
        return encryptedTokenBitString;
    }

    public SupplyGroupCodeHighOrder getSupplyGroupCodeHighOrder() {
        return supplyGroupCodeHighOrder;
    }

    public void setSupplyGroupCodeHighOrder(SupplyGroupCodeHighOrder supplyGroupCodeHighOrder) {
        this.supplyGroupCodeHighOrder = supplyGroupCodeHighOrder;
    }

    public NewKeyMiddleOrder1 getNewKeyMiddleOrder1() {
        return newKeyMiddleOrder1;
    }

    public void setNewKeyMiddleOrder1(NewKeyMiddleOrder1 newKeyMiddleOrder1) {
        this.newKeyMiddleOrder1 = newKeyMiddleOrder1;
    }

    @Override
    public HashMap<String, Object> getParams() {
        HashMap<String, Object> params = new HashMap<>();
        params.put("class", getTokenClass().getBitString().getValue());
        params.put("subclass", getTokenSubClass().getBitString().getValue());
        params.put("type", getType());

        if (getSupplyGroupCodeHighOrder() != null)
            params.put("supply_group_code_high_order", getSupplyGroupCodeHighOrder().getBitString().getValue());

        if (getNewKeyMiddleOrder1() != null)
            params.put("new_key_middle_order_1", getNewKeyMiddleOrder1().getBitString().getValue());

        return params;
    }

    public void decode(BitString decryptedTokenBitString,
                       BitString encryptedTokenBitString) throws
            InvalidRangeException, InvalidSgchoException,
            InvalidNewKeyMiddleOrder1Exception,
            CRCError, BitConcatOverflowError {
        if (checkCrc(decryptedTokenBitString, getTokenClass())) {
            setSupplyGroupCodeHighOrder(new SupplyGroupCodeHighOrder(decryptedTokenBitString.extractBits(48, 12)));
            setNewKeyMiddleOrder1(new NewKeyMiddleOrder1(decryptedTokenBitString.extractBits(16, 32)));
            setEncryptedTokenBitString(Long.toBinaryString(encryptedTokenBitString.getValue()));
            setDecryptedTokenBitString(Long.toBinaryString(decryptedTokenBitString.getValue()));
        }
    }
}
