package com.arsan.ai.admin.controller;

import com.arsan.ai.admin.model.RoleActionRequest;
import com.arsan.ai.admin.service.RoleService;
import com.arsan.ai.auth.enums.RoleType;
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
@RequestMapping("/admin/roles")
@RequiredArgsConstructor
public class RoleController {

    private final RoleService roleService;

    @GetMapping("/available")
    public List<RoleType> getAvailableRoles() {
        return roleService.availableRoles();
    }

    @GetMapping("/available/{userId}")
    public List<RoleType> getAvailableRoles(@PathVariable Long userId) {
        return roleService.availableRoles(userId);
    }

    @PatchMapping("/grant/{userId}")
    public void makeAdmin(
            @PathVariable Long userId,
            @RequestBody @Valid RoleActionRequest request) {
        roleService.grantRoles(userId, request.getRoles());
    }

    @PatchMapping("/revoke/{userId}")
    public void revokeAdmin(
            @PathVariable Long userId,
            @RequestBody @Valid RoleActionRequest request) {
        roleService.revokeRoles(userId, request.getRoles());
    }
}
