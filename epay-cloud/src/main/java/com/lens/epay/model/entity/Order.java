package com.lens.epay.model.entity;

import com.lens.epay.common.AbstractEntity;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.UUID;

/**
 * Created by Emir Gökdemir
 * on 17 Nis 2020
 */

@Data
@Entity
@Table(name = "user_order")
public class Order extends AbstractEntity<UUID> {

}
