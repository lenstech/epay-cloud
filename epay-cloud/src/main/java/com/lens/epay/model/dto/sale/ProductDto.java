package com.lens.epay.model.dto.sale;

import com.lens.epay.enums.Unit;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.util.UUID;

/**
 * Created by Emir Gökdemir
 * on 17 Nis 2020
 */

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductDto {

    private String name;

    private String description;

    @Enumerated(EnumType.STRING)
    private Unit unit = Unit.PIECE;

    private Float price;

    private UUID categoryId;

    private String brand;
}
