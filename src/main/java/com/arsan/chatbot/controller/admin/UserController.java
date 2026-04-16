package com.arsan.chatbot.controller.admin;

import com.arsan.chatbot.model.UpdateRoleRequest;
import com.arsan.chatbot.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PatchMapping("/{id}/role")
    public String makeAdmin(@PathVariable Long id, @RequestBody UpdateRoleRequest request) {
        userService.updateRole(id, request.getRole());
        return "Success";
    }
}
