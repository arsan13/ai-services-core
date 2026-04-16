package com.arsan.chatbot.service;

import com.arsan.chatbot.model.AuthRequest;
import com.arsan.chatbot.model.AuthResponse;

public interface AuthService {

    AuthResponse login(AuthRequest authRequest);

    AuthResponse register(AuthRequest authRequest);
}
