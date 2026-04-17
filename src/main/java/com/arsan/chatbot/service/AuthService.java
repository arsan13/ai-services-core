package com.arsan.chatbot.service;

import com.arsan.chatbot.model.auth.AuthRequest;
import com.arsan.chatbot.model.auth.AuthResponse;
import com.arsan.chatbot.model.auth.RegisterRequest;

public interface AuthService {

    AuthResponse login(AuthRequest authRequest);

    AuthResponse register(RegisterRequest authRequest);
}
