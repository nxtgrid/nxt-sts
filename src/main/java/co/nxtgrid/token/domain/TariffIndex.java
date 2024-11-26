package co.nxtgrid.token.domain;

import co.nxtgrid.token.domain.base.BitString;
import co.nxtgrid.token.exceptions.InvalidTariffIndexException;
import co.nxtgrid.token.miscellaneous.Strings;

public class TariffIndex implements Entity {

    private String tariffIndex ;
    private final String NAME = "Tariff Index";

    public TariffIndex(String tariffIndex)
        throws InvalidTariffIndexException {
        setValue(tariffIndex);
    }

    public TariffIndex(BitString tariffIndexBitString)
        throws InvalidTariffIndexException {
        setBitStringValue(tariffIndexBitString);
    }

    public String getName () {
        return NAME;
    }

    private void setValue(String tariffIndex)
        throws InvalidTariffIndexException {
        if (!tariffIndex.matches("[0-9]{2}"))
            throw new InvalidTariffIndexException(Strings.INVALID_TARIFF_INDEX);
        this.tariffIndex = tariffIndex;
    }

    private void setBitStringValue(BitString tariffIndexBitString)
        throws InvalidTariffIndexException {
        String tariffString = String.valueOf(tariffIndexBitString.getValue());
        String paddedTariffString = "00"
                .substring(tariffString.length())
                + tariffString;
        setValue(paddedTariffString);
    }

    public String getValue() {
        return tariffIndex;
    }
}
