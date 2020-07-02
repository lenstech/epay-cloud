package com.lens.epay.enums;

import lombok.Getter;

/**
 * Created by Emir Gökdemir
 * on 17 Nis 2020
 */
@Getter
public enum Unit {
    PIECE("adet"),
    KILOGRAM("kilogram"),
    GRAM("gram");

    private String turkish;

    Unit(String turkish) {
        this.turkish = turkish;
    }

    public String getTurkish(Unit unit) {
        return unit.getTurkish();
    }
}
