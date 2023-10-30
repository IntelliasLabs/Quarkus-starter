package com.intellias.basicsandbox.controller.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class LocalizedItemDTO extends ItemDTO {

    private String currencyLocalized;

    public LocalizedItemDTO(ItemDTO itemDTO,
            String currencyLocalized) {
        super(itemDTO.getId(), itemDTO.getName(), itemDTO.getCreditCard(), itemDTO.getCurrencyCode());
        this.currencyLocalized = currencyLocalized;
    }
}
