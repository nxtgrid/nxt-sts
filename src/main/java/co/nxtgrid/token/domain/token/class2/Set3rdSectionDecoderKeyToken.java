package co.nxtgrid.token.domain.token.class2;

import co.nxtgrid.token.domain.NewKeyMiddleOrder2;
import co.nxtgrid.token.domain.base.BitString;
import co.nxtgrid.token.domain.supplygroupcode.SupplyGroupCodeLowOrder;
import co.nxtgrid.token.domain.tokenclass.class2.Set3rdSectionDecoderKeyTokenClass;
import co.nxtgrid.token.domain.tokensubclass.class2.Set3rdSectionDecoderKeyTokenSubClass;
import co.nxtgrid.token.exceptions.BitConcatOverflowError;
import co.nxtgrid.token.exceptions.InvalidNewKeyMiddleOrder2Exception;
import co.nxtgrid.token.exceptions.InvalidRangeException;
import co.nxtgrid.token.exceptions.InvalidSgcloException;
import co.nxtgrid.token.generators.tokensdecoder.error.CRCError;

import java.util.HashMap;

public class Set3rdSectionDecoderKeyToken extends Class2Token {

    private SupplyGroupCodeLowOrder supplyGroupCodeLowOrder;
    private NewKeyMiddleOrder2 newKeyMiddleOrder2;

    public Set3rdSectionDecoderKeyToken(String requestID)
            throws InvalidRangeException {
        super(requestID);
        setTokenClass(new Set3rdSectionDecoderKeyTokenClass());
        setTokenSubClass(new Set3rdSectionDecoderKeyTokenSubClass());
    }

    public Set3rdSectionDecoderKeyToken(String requestID,
                                        SupplyGroupCodeLowOrder supplyGroupCodeLowOrder,
                                        NewKeyMiddleOrder2 newKeyMiddleOrder2)
        throws InvalidRangeException  {
        super(requestID);
        setTokenClass(new Set3rdSectionDecoderKeyTokenClass());
        setTokenSubClass(new Set3rdSectionDecoderKeyTokenSubClass());
        setSupplyGroupCodeLowOrder(supplyGroupCodeLowOrder);
        setNewKeyMiddleOrder2(newKeyMiddleOrder2);
    }

    @Override
    public String getType() {
        return "Set3rdSectionDecoderKey_28";
    }

    public String getBitString() {
        return encryptedTokenBitString;
    }

    public SupplyGroupCodeLowOrder getSupplyGroupCodeLowOrder() {
        return supplyGroupCodeLowOrder;
    }

    public void setSupplyGroupCodeLowOrder(SupplyGroupCodeLowOrder supplyGroupCodeLowOrder) {
        this.supplyGroupCodeLowOrder = supplyGroupCodeLowOrder;
    }

    public NewKeyMiddleOrder2 getNewKeyMiddleOrder2() {
        return newKeyMiddleOrder2;
    }

    public void setNewKeyMiddleOrder2(NewKeyMiddleOrder2 newKeyMiddleOrder2) {
        this.newKeyMiddleOrder2 = newKeyMiddleOrder2;
    }

    @Override
    public HashMap<String, Object> getParams() {
        HashMap<String, Object> params = new HashMap<>();
        params.put("class", getTokenClass().getBitString().getValue());
        params.put("subclass", getTokenSubClass().getBitString().getValue());
        params.put("type", getType());

        if (getSupplyGroupCodeLowOrder() != null)
            params.put("supply_group_code_low_order", getSupplyGroupCodeLowOrder().getSgcloBitString().getValue());

        if (getNewKeyMiddleOrder2() != null)
            params.put("new_key_middle_order_2", getNewKeyMiddleOrder2().getBitString().getValue());

        return params;
    }

    public void decode(BitString decryptedTokenBitString,
                       BitString encryptedTokenBitString) throws
            CRCError, InvalidRangeException,
            InvalidSgcloException,
            InvalidNewKeyMiddleOrder2Exception, BitConcatOverflowError {
        if (checkCrc(decryptedTokenBitString, getTokenClass())) {
            setSupplyGroupCodeLowOrder(new SupplyGroupCodeLowOrder(decryptedTokenBitString.extractBits(48, 12)));
            setNewKeyMiddleOrder2(new NewKeyMiddleOrder2(decryptedTokenBitString.extractBits(16, 32)));
            setEncryptedTokenBitString(Long.toBinaryString(encryptedTokenBitString.getValue()));
            setDecryptedTokenBitString(Long.toBinaryString(decryptedTokenBitString.getValue()));
        }
    }
}
