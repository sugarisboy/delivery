package dev.muskrat.delivery.core.persons;

public enum Role {

    UNKNOWN(0),
    USER(1),
    PARTNER(2),
    ADMIN(3),

    SUPERVISIOR(10);


    private int id;

    Role(int id) {
        this.id = id;
    }
}
