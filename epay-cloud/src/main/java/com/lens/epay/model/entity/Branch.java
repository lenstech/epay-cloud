package com.lens.epay.model.entity;

import com.lens.epay.common.AbstractEntity;
import lombok.Data;
import org.eclipse.persistence.annotations.Index;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.util.UUID;

/**
 * Created by Emir Gökdemir
 * on 29 Şub 2020
 */

@Data
@Entity
@Table(name = "branch")
public class Branch extends AbstractEntity<UUID> {

    @NotNull
    @Index
    private String name;

    @NotNull
    private String city;

    @NotNull
    private String address;

    @NotNull(message = "Firm cannot be blank")
    @ManyToOne
    @JoinColumn(name = "firm_id")
    private Firm firm;
}
