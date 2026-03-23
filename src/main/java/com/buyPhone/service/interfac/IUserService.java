package com.buyPhone.service.interfac;


import com.buyPhone.dto.UserDTO;
import com.buyPhone.entity.User;

import java.util.List;
import java.util.Optional;
import java.util.UUID;


public interface IUserService {

    UserDTO createUser(UserDTO dto);

    UserDTO getUser(UUID id);

    List<UserDTO> getAllUsers();

    UserDTO updateUser(UUID id, UserDTO dto);

    void deleteUser(UUID id);

    Optional<UserDTO> findByEmail(String email);

    boolean existByEmail(String email);

}

