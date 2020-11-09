package com.lens.epay.model.entity;

import com.lens.epay.common.AbstractEntity;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.UUID;

/**
 * Created by Emir Gökdemir
 * on 29 Eki 2020
 */

@Data
@Entity
@Table(name = "brand")
public class Brand extends AbstractEntity<UUID> {

    String name;

    String country;
}
