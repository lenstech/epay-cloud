package com.lens.epay.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.lens.epay.common.AbstractEntity;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;
import java.util.UUID;

/**
 * Created by Emir Gökdemir
 * on 17 Nis 2020
 */

@Data
@Entity
@Table(name = "category")
public class Category extends AbstractEntity<UUID> {

    @NotNull
    private String name;

    @Column(name = "description")
    @Size(max = 1024)
    private String description;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "category")
    @JsonIgnore
    private List<Product> products;
}
