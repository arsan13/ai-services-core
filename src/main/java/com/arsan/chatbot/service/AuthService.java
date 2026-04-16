package com.arsan.chatbot.service;

import com.arsan.chatbot.model.AuthRequest;
import com.arsan.chatbot.model.AuthResponse;
import com.arsan.chatbot.model.RegisterRequest;

public interface AuthService {

    AuthResponse login(AuthRequest authRequest);

    AuthResponse register(RegisterRequest authRequest);
}
