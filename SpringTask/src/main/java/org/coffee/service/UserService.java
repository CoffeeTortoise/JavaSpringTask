package org.coffee.service;

import org.coffee.dto.UserDTO;
import org.coffee.model.User;
import org.coffee.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public UserDTO createUser(UserDTO userDTO) {
        User user = new User();
        user.setName(userDTO.getName());
        user.setEmail(userDTO.getEmail());
        user.setAge(userDTO.getAge());
        user.setCreatedAt(LocalDateTime.now());
        user = userRepository.save(user);
        userDTO.setId(user.getId());
        return userDTO;
    }

    public UserDTO getUserById(Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
        return convertToDTO(user);
    }

    public List<UserDTO> getAllUsers() {
        return userRepository.findAll().stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    public UserDTO updateUser(Long id, UserDTO userDTO) {
        User user = userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
        user.setName(userDTO.getName());
        user.setEmail(userDTO.getEmail());
        user.setAge(userDTO.getAge());
        user = userRepository.save(user);
        return convertToDTO(user);
    }

    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    private UserDTO convertToDTO(User user) {
        UserDTO userDTO = new UserDTO();
        userDTO.setId(user.getId());
        userDTO.setName(user.getName());
        userDTO.setEmail(user.getEmail());
        userDTO.setAge(user.getAge());
        return userDTO;
    }
}