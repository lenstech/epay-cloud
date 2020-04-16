package com.lens.epay.model.entity;

import com.lens.epay.common.AbstractEntity;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.util.UUID;

/**
 * Created by Emir Gökdemir
 * on 22 Şub 2020
 */
@Data
@Entity
@Table(name = "department")
public class Department extends AbstractEntity<UUID> {

    @NotNull
    private String name;

    @NotNull
    private String description;

    @NotNull(message = "Branch cannot be blank")
    @ManyToOne
    @JoinColumn(name = "branch_id")
    private Branch branch;
}
