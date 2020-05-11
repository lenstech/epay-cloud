package com.lens.epay.model.entity;

import com.lens.epay.common.AbstractEntity;
import lombok.Data;
import org.eclipse.persistence.annotations.Index;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.Email;
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
    @Index
    private String name;

    private String city;

    private String taxId;

    private String Address;

    private String phoneNo;

    @Email
    private String email;
}
