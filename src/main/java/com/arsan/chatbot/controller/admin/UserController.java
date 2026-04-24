package com.arsan.chatbot.controller.admin;

import com.arsan.chatbot.projection.UserResponse;
import com.arsan.chatbot.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
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

    @PatchMapping("/make-admin/{id}")
    public String makeAdmin(@PathVariable Long id) {
        userService.makeAdmin(id);
        return "Success";
    }

    @PatchMapping("/revoke-admin/{id}")
    public String revokeAdmin(@PathVariable Long id) {
        userService.revokeAdmin(id);
        return "Success";
    }
}
