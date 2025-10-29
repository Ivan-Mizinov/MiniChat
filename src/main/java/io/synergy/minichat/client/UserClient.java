package io.synergy.minichat.client;

import feign.RequestLine;
import io.synergy.minichat.dto.UserResponseDto;

public interface UserClient {
    @RequestLine("GET /api")
    UserResponseDto findAll();
}
