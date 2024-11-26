package co.nxtgrid.token.generators.decoderkeygenerator;

import co.nxtgrid.token.domain.*;
import co.nxtgrid.token.domain.encryptionalgorithm.EncryptionAlgorithm;
import co.nxtgrid.token.domain.keys.decoder.DecoderKey;
import co.nxtgrid.token.domain.keys.vending.VendingKey;
import co.nxtgrid.token.domain.supplygroupcode.SupplyGroupCode;
import co.nxtgrid.token.exceptions.InvalidDecoderKeyParametersException;
import co.nxtgrid.token.exceptions.NotImplementedException;
import co.nxtgrid.token.miscellaneous.Strings;
import org.bouncycastle.util.encoders.Hex;

import java.util.ArrayList;

public class DecoderKeyGeneratorAlgorithm01 extends DecoderKeyGeneratorAlgorithm {

    private VendingKey vendingKey;

    public DecoderKeyGeneratorAlgorithm01(KeyType keyType, SupplyGroupCode supplyGroupCode, TariffIndex tariffIndex,
                                          KeyRevisionNumber keyRevisionNumber, IssuerIdentificationNumber issuerIdentificationNumber,
                                          IndividualAccountIdentificationNumber individualAccountIdentificationNumber, VendingKey vudk,
                                          EncryptionAlgorithm encryptionAlgorithm) {
        setIssuerIdentificationNumber(issuerIdentificationNumber);
        setIndividualAccountIdentificationNumber(individualAccountIdentificationNumber);
        setKeyType(keyType);
        setSupplyGroupCode(supplyGroupCode);
        setTariffIndex(tariffIndex);
        setKeyRevisionNumber(keyRevisionNumber);
        setVendingKey(vudk);
        setEncryptionAlgorithm(encryptionAlgorithm);
    }

    public String getName() {
        return "DKGA01";
    }

    public VendingKey getVendingKey() {
        return vendingKey;
    }

    public void setVendingKey(VendingKey vendingKey) {
        this.vendingKey = vendingKey;
    }

    @Override
    public DecoderKey generate() throws Exception {
        PrimaryAccountNumberBlock primaryAccountNumberBlock = createPrimaryAccountNumberBlock();
        ControlBlock controlBlock = createControlBlock();
        if(issuerIdentificationNumber.getValue().equals("600727") &&
                keyRevisionNumber.getValue() == 1 &&
                encryptionAlgorithm.getCode() == EncryptionAlgorithm.Code.STA) {
            if(((keyType.getValue() == 1 || keyType.getValue() == 2) && iainWithinRange(individualAccountIdentificationNumber)) ||
                    (keyType.getValue() == 3 && sgcHasValidValue(supplyGroupCode))) {
                byte[] panBlockData = Hex.decode(primaryAccountNumberBlock.getValue());
                byte[] controlBlockData = Hex.decode(controlBlock.getValue());
                byte[] panXorControlBlock = xor(panBlockData, controlBlockData);
                byte[] encryptedPanXorControlBlock = encrypt(panXorControlBlock);
                byte[] decoderKeyData = xor(encryptedPanXorControlBlock, vendingKey.getKeyData());
                return new DecoderKey(decoderKeyData);
            }
        }
        throw new InvalidDecoderKeyParametersException(Strings.INVALID_DECODER_KEY_PARAMETERS);
    }

    private boolean iainWithinRange(IndividualAccountIdentificationNumber individualAccountIdentificationNumber) {
        ArrayList<Long[]> iainRanges = new ArrayList<>() {{
            add(new Long[] {1090000000l, 1090004999l});
            add(new Long[] {1000000000l, 1004999999l});
            add(new Long[] {3000000000l, 3114000009l});
            add(new Long[] {4000000000l, 4059999999l});
            add(new Long[] {6010000000l, 6039999999l});
            add(new Long[] {6400000000l, 6419999999l});
            add(new Long[] {6660000000l, 6699999999l});
            add(new Long[] {6990000010l, 6990009999l});
            add(new Long[] {7000000000l, 7020999999l});
        }};
        int iainValue = Integer.parseInt(individualAccountIdentificationNumber.getValue());
        for (Long[] iainRange : iainRanges)
            if (iainValue >= iainRange[0] && iainValue <= iainRange[1])
                return true;
        return false;
    }

    private boolean sgcHasValidValue(SupplyGroupCode supplyGroupCode) {
        int[] dkga01ValidSGCCodes = { 100702, 990400, 990401, 990402, 990403, 990404, 990405 } ;
        int sgcCode = Integer.parseInt(supplyGroupCode.getValue());
        for (int currSgcCode : dkga01ValidSGCCodes)
            if (currSgcCode == sgcCode)
                return true;
        return false;
    }

    public byte[] encrypt (byte[] dataToEncrypt) throws NotImplementedException {
       throw new NotImplementedException();
    }

    public String decrypt(String str) throws NotImplementedException {
       throw new NotImplementedException();
    }
}
