package com.lens.epay.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.lens.epay.common.AbstractEntity;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

/**
 * Created by Emir Gökdemir
 * on 5 Nis 2020
 */

@Data
@Entity
@Table(name = "user_group")
public class UserGroup extends AbstractEntity<UUID> {


    @NotNull
    private String name;

    @ManyToMany
    @JoinTable(name = "user_user_group",
            joinColumns = @JoinColumn(name = "user_group_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"))
    @JsonIgnore
    private Set<User> users = new HashSet<>();

    private Boolean isPrivate = false;
}
