package com.intellias.basicsandbox.controller.dto;

import com.intellias.basicsandbox.controller.validation.group.Create;
import com.intellias.basicsandbox.controller.validation.group.Update;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ItemDTO {

    @NotNull(groups = Update.class)
    private UUID id;

    @Size(min = 1, max = 255, groups = {Create.class, Update.class}, message = "{name.notempty}")
    @NotNull
    private String name;

    @Pattern(regexp = "^(?:4[0-9]{12}(?:[0-9]{3})?|[25][1-7][0-9]{14}|6(?:011|5[0-9][0-9])[0-9]{12}|3[47][0-9]{13}|3(?:0[0-5]|[68][0-9])[0-9]{11}|(?:2131|1800|35\\d{3})\\d{11})$", groups = {Create.class, Update.class})
    private String creditCard;

    @Size(min = 3, max = 3, groups = {Create.class, Update.class})
    private String currencyCode;
}
