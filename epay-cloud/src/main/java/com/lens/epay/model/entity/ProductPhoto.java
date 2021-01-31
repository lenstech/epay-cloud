package com.lens.epay.model.entity;

import com.lens.epay.common.AbstractEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.eclipse.persistence.annotations.Index;

import javax.persistence.*;
import java.util.UUID;

/**
 * Created by Emir Gökdemir
 * on 5 Nis 2020
 */

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@Table(name = "product_photo")
public class ProductPhoto extends AbstractEntity<UUID> {

    @OneToOne
    @Index
    @JoinColumn(name = "product_id", referencedColumnName = "id")
    private Product product;

    @Column
    @Lob
    private byte[] file;

    public ProductPhoto() {
    }

    public ProductPhoto(Product product) {
        this.product = product;
    }

}
