package co.nxtgrid.token.domain.table.permutationtable;

import co.nxtgrid.token.domain.table.Table;

public class EncryptingPermutationTable extends Table {

    // Sample table
    //    int permutationTable [] = { 29, 27, 34, 9, 16, 62, 55, 2, 40, 49, 38, 25, 33, 61, 30, 23, 1, 41, 21, 57, 42, 15,
    //            5, 58, 19, 53, 22, 17, 48, 28, 24, 39, 3, 60, 36, 14, 11, 52, 54, 12, 31, 51, 10, 26,
    //            0, 45, 37, 43, 44, 6, 59, 4, 7, 35, 56, 50, 13, 18, 32, 47, 46, 63, 20, 8 } ;

    // Actual
    int permutationTable [] = { 55, 42, 10, 18, 24, 21, 44, 35, 2, 22, 56, 43, 27,
                                58, 9, 50, 6, 36, 12, 61, 37, 38, 53, 16, 62, 3, 7, 4,
                                32, 20, 63, 25, 51, 52, 54, 33, 49, 19, 46, 29, 48,
                                31, 23, 30, 41, 28, 13, 5, 40, 60, 39, 11, 15, 17,
                                1, 0, 57, 34, 59, 8, 47, 14, 45, 26};

    public EncryptingPermutationTable() {
        setValues(permutationTable);
    }
}