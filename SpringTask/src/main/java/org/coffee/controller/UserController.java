package org.coffee.controller;

import org.coffee.dto.UserDTO;
import org.coffee.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping
    public ResponseEntity<EntityModel<UserDTO>> createUser(@RequestBody UserDTO userDTO) {
        UserDTO createdUser = userService.createUser(userDTO);
        EntityModel<UserDTO> resource = EntityModel.of(createdUser);
        resource.add(linkTo(methodOn(UserController.class).getUserById(createdUser.getId())).withSelfRel());
        resource.add(linkTo(methodOn(UserController.class).getAllUsers()).withRel("all-users"));
        return ResponseEntity.ok(resource);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<UserDTO>> getUserById(@PathVariable Long id) {
        UserDTO userDTO = userService.getUserById(id);
        EntityModel<UserDTO> resource = EntityModel.of(userDTO);
        resource.add(linkTo(methodOn(UserController.class).getUserById(id)).withSelfRel());
        resource.add(linkTo(methodOn(UserController.class).getAllUsers()).withRel("all-users"));
        return ResponseEntity.ok(resource);
    }

    @GetMapping
    public ResponseEntity<List<EntityModel<UserDTO>>> getAllUsers() {
        List<UserDTO> users = userService.getAllUsers();
        List<EntityModel<UserDTO>> resources = users.stream()
                .map(user -> EntityModel.of(user,
                        linkTo(methodOn(UserController.class).getUserById(user.getId())).withSelfRel(),
                        linkTo(methodOn(UserController.class).getAllUsers()).withRel("all-users")))
                .collect(Collectors.toList());
        return ResponseEntity.ok(resources);
    }

    @PutMapping("/{id}")
    public ResponseEntity<EntityModel<UserDTO>> updateUser(@PathVariable Long id, @RequestBody UserDTO userDTO) {
        UserDTO updatedUser = userService.updateUser(id, userDTO);
        EntityModel<UserDTO> resource = EntityModel.of(updatedUser);
        resource.add(linkTo(methodOn(UserController.class).getUserById(id)).withSelfRel());
        resource.add(linkTo(methodOn(UserController.class).getAllUsers()).withRel("all-users"));
        return ResponseEntity.ok(resource);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
}