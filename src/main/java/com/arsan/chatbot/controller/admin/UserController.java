package com.arsan.chatbot.controller.admin;

import com.arsan.chatbot.enums.PermissionType;
import com.arsan.chatbot.model.common.PermissionRequest;
import com.arsan.chatbot.model.common.RoleActionRequest;
import com.arsan.chatbot.projection.UserResponse;
import com.arsan.chatbot.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/admin/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping
    public List<UserResponse> getUsers() {
        return userService.getAll();
    }

    @GetMapping("/{id}")
    public UserResponse getUserById(@PathVariable Long id) {
        return userService.getUserById(id);
    }

    @PatchMapping("/role/grant/{id}")
    public void makeAdmin(
            @PathVariable Long id,
            @RequestBody @Valid RoleActionRequest request) {
        userService.grantRole(id, request.getRole());
    }

    @PatchMapping("/role/revoke/{id}")
    public void revokeAdmin(
            @PathVariable Long id,
            @RequestBody @Valid RoleActionRequest request) {
        userService.revokeRole(id, request.getRole());
    }

    @PatchMapping("/permission/grant/{id}")
    public void grantPermissions(
            @PathVariable Long id,
            @RequestBody @Valid PermissionRequest request) {
        userService.grantPermission(id, request.getPermissions());
    }

    @PatchMapping("/permission/revoke/{id}")
    public void revokePermissions(
            @PathVariable Long id,
            @RequestBody @Valid PermissionRequest request) {
        userService.revokePermission(id, request.getPermissions());
    }

    @GetMapping("permission/available")
    public List<PermissionType> getAvailablePermissions() {
        return userService.availablePermissions();
    }
}
