package co.nxtgrid.token.domain.table.substitutiontable;

import co.nxtgrid.token.domain.table.Table;

public class DecryptingSecondSubstitutionTable extends Table {

    // Sample table
    // int secondSubstitutionTable [] = { 6, 9, 7, 4, 3, 10, 12, 14, 2, 13, 1, 15, 0, 11, 8, 5 };

    // Real table
    int secondSubstitutionTable [] = { 9, 11, 6, 5, 12, 7, 14, 2, 13, 3, 1, 15, 4, 8, 0, 10 };

    public DecryptingSecondSubstitutionTable() {
        setValues(secondSubstitutionTable);
    }
}
