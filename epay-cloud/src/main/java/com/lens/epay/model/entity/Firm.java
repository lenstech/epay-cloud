package com.lens.epay.model.entity;

import com.lens.epay.common.AbstractEntity;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.util.UUID;

/**
 * Created by Emir Gökdemir
 * on 1 Mar 2020
 */

@Data
@Entity
@Table(name = "firm")
public class Firm extends AbstractEntity<UUID> {

    @NotNull
    private String name;

    private String city;

    private String taxId;
}
