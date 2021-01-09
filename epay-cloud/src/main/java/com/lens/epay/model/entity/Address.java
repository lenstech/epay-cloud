package com.lens.epay.model.entity;

import com.lens.epay.common.AbstractEntity;
import com.lens.epay.enums.AddressType;
import com.lens.epay.enums.InvoiceType;
import lombok.Data;
import org.eclipse.persistence.annotations.Index;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.UUID;

/**
 * Created by Emir Gökdemir
 * on 18 Nis 2020
 */

@Data
@Entity
@Table(name = "address")
public class Address extends AbstractEntity<UUID> {

    private String name;

    @Index
    @ManyToOne(optional = false)
    private User user;

    @NotNull
    private String country = "Türkiye";

    @NotNull
    private String city;

    @NotNull
    private String district;

    @NotNull
    private String neighborhood;

    private String street;

    private String buildNo;

    private String innerNo;

    private String otherInfo;

    private Integer postalCode;

    @NotNull
    private String receiverName;

    @NotNull
    private String receiverSurname = "";

    @NotNull
    private String receiverPhoneNumber;

    @Enumerated(EnumType.STRING)
    @NotNull
    private AddressType addressType = AddressType.DELIVERY;

    @Enumerated(EnumType.STRING)
    @NotNull
    private InvoiceType invoiceType = InvoiceType.INDIVIDUAL;

    //Bireysel Fatura
    @Size(max = 11)
    private String identityNo;

    //Kurumsal Fatura
    private String firmName;

    //Kurumsal Fatura
    @Size(max = 10)
    private String taxNo;

    //Kurumsal Fatura
    private String taxAdministration;


    public String toString() {
        return "country='" + country + '\'' +
                ", city='" + city + '\'' +
                ", district='" + district + '\'' +
                ", neighborhood='" + neighborhood + '\'' +
                ", street='" + street + '\'' +
                ", buildNo='" + buildNo + '\'' +
                ", innerNo='" + innerNo + '\'' +
                ", otherInfo='" + otherInfo + '\'' +
                ", postalCode=" + postalCode;
    }

    public String toStringForTurkishAddress() {
        return nullToEmpty(neighborhood, " mah. ")
                + nullToEmpty(street, " cad. ")
                + nullToEmptyNo("no: ", buildNo)
                + nullToEmptyNo("/", innerNo)
                + " "
                + nullToEmpty(otherInfo, " ")
                + nullToEmpty(district, " ")
                + nullToEmpty(city, " ")
                + nullToEmpty(country, " ")
                + nullToEmptyPostalCode(postalCode);
    }

    private String nullToEmpty(String s, String other) {
        if (s == null) {
            return "";
        }
        return s + other;
    }

    private String nullToEmptyNo(String init, String s) {
        if (s == null) {
            return "";
        }
        return init + s;
    }

    private String nullToEmptyPostalCode(Integer s) {
        if (s == null) {
            return "";
        }
        return "posta kodu: " + s;
    }
}
