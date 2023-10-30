package com.intellias.basicsandbox.persistence.entity;

import com.intellias.basicsandbox.persistence.converter.EncryptedStringConverter;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "items", schema = "public")
public class ItemEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(name = "name")
    private String name;

    @Column(name = "credit_card")
    @Convert(converter = EncryptedStringConverter.class)
    private String creditCard;

    @Column(name = "currency_code")
    private String currencyCode;
}
