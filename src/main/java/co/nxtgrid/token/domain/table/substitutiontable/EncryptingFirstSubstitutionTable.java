package co.nxtgrid.token.domain.table.substitutiontable;

import co.nxtgrid.token.domain.table.Table;

public class EncryptingFirstSubstitutionTable extends Table {

    // Sample table
    // int firstSubstitutionTable [] = { 12, 10, 8, 4, 3, 15, 0, 2, 14, 1, 5, 13, 6, 9, 7, 11 } ;

    // Real table
    int firstSubstitutionTable[] = { 14, 10, 7, 9, 12, 3, 2, 5, 13, 0, 15, 1, 4, 8, 6, 11 } ;
    
    public EncryptingFirstSubstitutionTable() {
        setValues(firstSubstitutionTable);
    }

}