package com.lens.epay.enums;

public enum Role {
    NOT_AUTH(0),
    CUSTOMER(1),
    BASIC_USER(2),
    DEPARTMENT_ADMIN(3),
    BRANCH_ADMIN(4),
    FIRM_ADMIN(5),
    ADMIN(6);  //the developer mode

    private final int value;

    Role(int value) {
        this.value = value;
    }

    public int toValue() {
        return value;
    }
}
