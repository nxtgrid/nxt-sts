package co.nxtgrid.token.domain.table.substitutiontable;

import co.nxtgrid.token.domain.table.Table;

public class EncryptingSecondSubstitutionTable extends Table {

    // Sample table
    // int secondSubstitutionTable [] = { 6, 9, 7, 4, 3, 10, 12, 14, 2, 13, 1, 15, 0, 11, 8, 5 };

    // Real table
    int secondSubstitutionTable [] = { 12, 8, 2, 13, 7, 6, 1, 3, 11, 5, 9, 15, 0, 4, 10, 14 };

    public EncryptingSecondSubstitutionTable() {
        setValues(secondSubstitutionTable);
    }
}
