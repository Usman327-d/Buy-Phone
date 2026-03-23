package com.buyPhone.service.impl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import com.buyPhone.dto.UserDTO;
import com.buyPhone.entity.User;
import com.buyPhone.enums.UserRole;
import com.buyPhone.exception.ConflictException;
import com.buyPhone.exception.ResourceNotFoundException;
import com.buyPhone.mapper.UserMapper;
import com.buyPhone.repository.UserRepository;
import com.buyPhone.service.interfac.IUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class UserService implements IUserService {

    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper = new UserMapper();


    @Override
    public UserDTO createUser(UserDTO dto) {
        if(repository.existsByEmail(dto.getEmail()))
            throw new ConflictException("Email already exists");


        dto.setRole(String.valueOf(UserRole.CUSTOMER));
        dto.setPasswordHash(passwordEncoder.encode(dto.getPasswordHash()));
        dto.setCreatedAt(LocalDateTime.now());

        User user = userMapper.toEntity(dto);

        return userMapper.toDTO(repository.save(user));
    }


    @Override
    public UserDTO getUser(UUID id) {
        User user = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User", id.toString()));
        return userMapper.toDTO(user);
    }

    @Override
    public List<UserDTO> getAllUsers() {
        return repository.findAll()
                .stream()
                .map(userMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public UserDTO updateUser(UUID id, UserDTO dto) {
        User user = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User", id.toString()));

        user.setFirstName(dto.getFirstName());
        user.setLastName(dto.getLastName());
        return userMapper.toDTO(repository.save(user));
    }

    @Override
    public void deleteUser(UUID id) {
        if(!repository.existsById(id))
            throw new ResourceNotFoundException("User", id.toString());
        repository.deleteById(id);
    }

    @Override
    public Optional<UserDTO> findByEmail(String email) {
        User user = repository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User", email));
        return Optional.ofNullable(userMapper.toDTO(user));
    }

    @Override
    public boolean existByEmail(String email) {
        return repository.existsByEmail(email);
    }
}

