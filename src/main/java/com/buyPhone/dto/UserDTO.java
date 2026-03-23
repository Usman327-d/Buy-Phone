package com.buyPhone.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;


@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {

    private String id;
    private String email;
    private String firstName;
    private String lastName;
    private String passwordHash;
    private String role;
    private LocalDateTime createdAt;

    private List<OrderDTO> orders;

    private List<AddressDTO> addresses;

}
