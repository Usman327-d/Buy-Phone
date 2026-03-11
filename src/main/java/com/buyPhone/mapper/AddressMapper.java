package com.buyPhone.mapper;

import com.buyPhone.dto.AddressDTO;
import com.buyPhone.entity.Address;

import java.util.UUID;

public class AddressMapper {

    public AddressDTO toDTO(Address address){

        new AddressDTO();
        return AddressDTO.builder()
                .id(String.valueOf(address.getId()))
                .city(address.getCity())
                .state(address.getState())
                .isDefault(address.getIsDefault())
                .country(address.getCountry())
                .postalCode(address.getPostalCode())
                .street(address.getStreet())
                .build();

    }

    public Address toEntity(AddressDTO addressDTO){
        Address add  = new Address();

        add.setId(UUID.fromString(addressDTO.getId()));
        add.setCity(addressDTO.getCity());
        add.setCountry(addressDTO.getCountry());
        add.setState(addressDTO.getState());
        add.setIsDefault(addressDTO.getIsDefault());
        add.setPostalCode(addressDTO.getPostalCode());
        add.setStreet(addressDTO.getStreet());

        return add;
    }
}
