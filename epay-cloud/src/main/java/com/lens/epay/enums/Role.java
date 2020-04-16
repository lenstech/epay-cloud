package com.lens.epay.enums;

public enum Role {
    BASIC_USER(1),
    DEPARTMENT_ADMIN(2),
    BRANCH_ADMIN(3),
    FIRM_ADMIN(4),
    ADMIN(5);  //the developer mode

    Role(int value) {
        this.value = value;
    }

    private int value;

    public int toValue() {
        return value;
    }
}
