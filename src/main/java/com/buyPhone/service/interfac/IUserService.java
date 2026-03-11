package com.buyPhone.service.interfac;


import com.buyPhone.dto.UserDTO;

import java.util.List;
import java.util.UUID;


public interface IUserService {

    UserDTO createUser(UserDTO dto);

    UserDTO getUser(UUID id);

    List<UserDTO> getAllUsers();

    UserDTO updateUser(UUID id, UserDTO dto);

    void deleteUser(UUID id);

}

