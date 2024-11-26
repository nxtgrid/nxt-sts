package co.nxtgrid.token.domain.table.substitutiontable;

import co.nxtgrid.token.domain.table.Table;

public class DecryptingFirstSubstitutionTable extends Table {

    // Sample table
    // int firstSubstitutionTable [] = { 12, 10, 8, 4, 3, 15, 0, 2, 14, 1, 5, 13, 6, 9, 7, 11 } ;

    // Real table
    int firstSubstitutionTable[] = { 12, 6, 2, 7, 13, 9, 5, 4, 1, 10, 14, 8, 0, 3, 15, 11 } ;

    public DecryptingFirstSubstitutionTable() {
        setValues(firstSubstitutionTable);
    }

}