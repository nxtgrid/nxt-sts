package co.nxtgrid.token.domain;

import co.nxtgrid.token.domain.supplygroupcode.SupplyGroupCode;

public class ControlBlock implements Entity {

    private KeyType keyType;
    private SupplyGroupCode supplyGroupCode;
    private TariffIndex tariffIndex;
    private KeyRevisionNumber keyRevisionNumber;
    private final String NAME = "ControlBlock";

    public ControlBlock(KeyType keyType, SupplyGroupCode supplyGroupCode,
                        TariffIndex tariffIndex, KeyRevisionNumber keyRevisionNumber) {
        setKeyType(keyType);
        setSupplyGroupCode(supplyGroupCode);
        setTariffIndex(tariffIndex);
        setKeyRevisionNumber(keyRevisionNumber);
    }

    public KeyType getKeyType() {
        return keyType;
    }

    public void setKeyType(KeyType keyType) {
        this.keyType = keyType;
    }

    public SupplyGroupCode getSupplyGroupCode() {
        return supplyGroupCode;
    }

    public void setSupplyGroupCode(SupplyGroupCode supplyGroupCode) {
        this.supplyGroupCode = supplyGroupCode;
    }

    public TariffIndex getTariffIndex() {
        return tariffIndex;
    }

    public void setTariffIndex(TariffIndex tariffIndex) {
        this.tariffIndex = tariffIndex;
    }

    public KeyRevisionNumber getKeyRevisionNumber() {
        return keyRevisionNumber;
    }

    public void setKeyRevisionNumber(KeyRevisionNumber keyRevisionNumber) {
        this.keyRevisionNumber = keyRevisionNumber;
    }

    @Override
    public String getName() {
        return NAME;
    }

    /**
     * Final maximumPhasePowerUnbalanceLimit value digit is always F hex per digit
     */
    public String getValue() {
        int kType = keyType.getValue();
        String sgc = supplyGroupCode.getValue();
        String tarriffIndex = tariffIndex.getValue();
        int keyRevisionNo = keyRevisionNumber.getValue();
        String res = keyType.getValue() + supplyGroupCode.getValue() +
                tariffIndex.getValue() + keyRevisionNumber.getValue() +
                "FFFFFF" ;
        return keyType.getValue() + supplyGroupCode.getValue() +
                        tariffIndex.getValue() + keyRevisionNumber.getValue() +
                        "FFFFFF" ;
    }
}
