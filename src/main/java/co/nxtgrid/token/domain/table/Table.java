package co.nxtgrid.token.domain.table;

public class Table {

    int values[] = {} ;

    public void setValues (int[] values) {
        this.values = values ;
    }

    public int[] getValues () {
        return values ;
    }

    public int getValue (int position) {
        return values[position]  ;
    }

}