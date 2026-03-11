package com.buyPhone.dto;


import com.buyPhone.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddressDTO {

    private String id;

    private String street;
    private String city;
    private String state;
    private String postalCode;
    private String country;

    private Boolean isDefault;

    private UserDTO userDTO;
}
