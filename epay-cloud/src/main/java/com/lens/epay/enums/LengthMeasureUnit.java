package com.lens.epay.enums;

import lombok.Getter;

@Getter
public enum LengthMeasureUnit {
    METER("m"), CENTIMETER("cm"), MILLIMETER("mm"), INCH("inch");

    private final String abbreviation;

    LengthMeasureUnit(String abbreviation) {
        this.abbreviation = abbreviation;
    }
}
