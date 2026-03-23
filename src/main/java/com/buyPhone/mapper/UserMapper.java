package com.buyPhone.mapper;

import com.buyPhone.dto.UserDTO;
import com.buyPhone.entity.User;
import com.buyPhone.enums.UserRole;
import lombok.Builder;


import java.util.stream.Collectors;


public class UserMapper {

    public UserDTO toDTO(User user){
        if(user == null) return null;

        return UserDTO.builder()
                .id(user.getId() != null ? user.getId().toString() : null)
                .email(user.getEmail())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .passwordHash(user.getPasswordHash())
                .role(String.valueOf(user.getRole()))
                .createdAt(user.getCreatedAt())
                .orders(user.getOrders() != null
                        ? user.getOrders().stream()
                        .map(o -> new OrderMapper().toDTO(o))
                        .collect(Collectors.toList())
                        : null)
                .addresses(user.getAddresses() != null
                        ? user.getAddresses().stream()
                        .map(a -> new AddressMapper().toDTO(a))
                        .collect(Collectors.toList())
                        : null)
                .build();
    }

    public User toEntity(UserDTO dto){
        if(dto == null) return null;

        User user = new User();
        user.setId(dto.getId() != null ? java.util.UUID.fromString(dto.getId()) : null);
        user.setEmail(dto.getEmail());
        user.setFirstName(dto.getFirstName());
        user.setLastName(dto.getLastName());
        user.setPasswordHash(dto.getPasswordHash());
        user.setRole(UserRole.valueOf(dto.getRole()));
        return user;
    }
}
