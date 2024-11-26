package co.nxtgrid.token.domain.table.permutationtable;

import co.nxtgrid.token.domain.table.Table;

public class DecryptingPermutationTable extends Table {

    // Sample table
    //    int permutationTable [] = {
    //            44, 16, 7, 32, 51, 22, 49, 52, 63, 3, 42, 36, 39, 56, 35, 21, 4, 27, 57, 24, 62, 18, 26, 15,
    //            30, 11, 43, 1, 29, 0, 14, 40, 58, 12, 2, 53, 34, 46, 10, 31, 8, 17, 20, 47, 48, 45, 60, 59,
    //            28, 9, 55, 41, 37, 25, 38, 6, 54, 19, 23, 50, 33, 13, 5, 61
    // } ;

    // Real table
    int permutationTable [] = {
            55, 54, 8, 25, 27, 47, 16, 26, 59, 14, 2, 51, 18, 46,
            61, 52, 23, 53, 3, 37, 29, 5, 9, 42, 4, 31, 63, 12, 45,
            39, 43, 41, 28, 35, 57, 7, 17, 20, 21, 50, 48, 44, 1,
            11, 6, 62, 38, 60, 40, 36, 15, 32, 33, 22, 34, 0, 10, 56,
            13, 58, 49, 19, 24, 30
    };

    public DecryptingPermutationTable() {
        setValues(permutationTable);
    }
}
