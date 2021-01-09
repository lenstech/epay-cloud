package com.lens.epay.enums;

public enum Role {
    CUSTOMER(0),
    BASIC_USER(1),
    DEPARTMENT_ADMIN(2),
    BRANCH_ADMIN(3),
    FIRM_ADMIN(4),
    ADMIN(5);  //the developer mode

    private int value;

    Role(int value) {
        this.value = value;
    }

    public int toValue() {
        return value;
    }
}
