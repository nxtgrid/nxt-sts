package co.nxtgrid.token.domain;


public interface Entity {
    default String getName() {
        return Class.class.getName();
    }
}
