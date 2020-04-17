package com.lens.epay.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.lens.epay.common.AbstractEntity;
import com.lens.epay.enums.Role;
import lombok.Data;
import org.eclipse.persistence.annotations.Index;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.UUID;

/**
 * Created by Emir Gökdemir
 * on 12 Eki 2019
 */

@Data
@Entity
@Table(name = "users", uniqueConstraints = {@UniqueConstraint(columnNames = {"email"})})
public class User extends AbstractEntity<UUID> {

    @Index
    @NotNull
    @Email(message = "Please provide acceptable mail address")
    private String email;

    @Column(length = 60)
    @NotEmpty(message = "Please provide your password")
    private String password;

    @NotNull
    private String name;

    @NotNull
    private String surname;

    @NotNull
    @Column(name = "role")
    @Enumerated(EnumType.STRING)
    private Role role;

    @ManyToOne
    @JoinColumn(name = "department")
    private Department department;

    private String userFirmId;

    private String title;

    @Column(name = "confirmed")
    private boolean confirmed = false;


    public String toStringForSearch() {
        return (" " + email +
                " " + name +
                " " + surname).toLowerCase(/*Locale.ENGLISH*/);
    }
}
