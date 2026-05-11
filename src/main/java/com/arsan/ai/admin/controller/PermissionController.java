package com.arsan.ai.admin.controller;

import com.arsan.ai.admin.model.PermissionRequest;
import com.arsan.ai.admin.service.PermissionService;
import com.arsan.ai.auth.enums.RoleType;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/admin/permissions")
@RequiredArgsConstructor
public class PermissionController {

    private final PermissionService permissionService;

    @GetMapping("/available")
    public List<String> getAvailablePermissions() {
        return permissionService.availablePermissions();
    }

    @GetMapping("/available/{id}")
    public List<String> getAvailablePermissions(@PathVariable Long id) {
        return permissionService.availablePermissions(id);
    }

    @GetMapping("/available")
    public List<String> getAvailablePermissions(@RequestParam String role) {
        return permissionService.availablePermissions(RoleType.fromValue(role));
    }

    @PatchMapping("/grant/{id}")
    public void grantPermissions(
            @PathVariable Long id,
            @RequestBody @Valid PermissionRequest request) {
        permissionService.grantPermission(id, request.getPermissions());
    }

    @PatchMapping("/revoke/{id}")
    public void revokePermissions(
            @PathVariable Long id,
            @RequestBody @Valid PermissionRequest request) {
        permissionService.revokePermission(id, request.getPermissions());
    }
}
