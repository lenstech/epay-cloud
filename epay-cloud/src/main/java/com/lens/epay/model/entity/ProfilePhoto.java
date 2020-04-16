package com.lens.epay.model.entity;

import com.lens.epay.common.AbstractEntity;
import lombok.Data;

import javax.persistence.*;
import java.util.UUID;

/**
 * Created by Emir Gökdemir
 * on 5 Nis 2020
 */

@Entity
@Data
@Table(name = "profile_photo")
public class ProfilePhoto extends AbstractEntity<UUID> {

    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    @Column
    @Lob
    private byte[] file;

}
