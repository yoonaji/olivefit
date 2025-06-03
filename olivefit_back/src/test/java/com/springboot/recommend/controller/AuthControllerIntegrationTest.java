package com.springboot.recommend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.springboot.recommend.dto.LoginRequest;
import com.springboot.recommend.dto.SignupRequest;
import com.springboot.recommend.entity.User;
import com.springboot.recommend.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class AuthControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    void cleanUp() {
        userRepository.deleteAll();  // 매 테스트 전 유저 초기화
    }

    @Test
    @DisplayName("회원가입 성공")
    void testSignupSuccess() throws Exception {
        SignupRequest signup = new SignupRequest();
        signup.setUsername("testuser");
        signup.setEmail("test@example.com");
        signup.setPassword("password123");

        mockMvc.perform(post("/api/auth/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(signup)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("User registered successfully!"));
    }

    @Test
    @DisplayName("회원가입 실패 - 중복 username")
    void testSignupDuplicateUsername() throws Exception {
        // 사전 저장
        userRepository.save(new User("testuser", "test@example.com", passwordEncoder.encode("1234")));

        SignupRequest signup = new SignupRequest();
        signup.setUsername("testuser");
        signup.setEmail("other@example.com");
        signup.setPassword("password123");

        mockMvc.perform(post("/api/auth/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(signup)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Error: Username is already taken!"));
    }

    @Test
    @DisplayName("로그인 성공")
    void testLoginSuccess() throws Exception {
        String rawPassword = "password123";
        userRepository.save(new User("testuser", "test@example.com", passwordEncoder.encode(rawPassword)));

        LoginRequest login = new LoginRequest();
        login.setUsername("testuser");
        login.setPassword(rawPassword);

        mockMvc.perform(post("/api/auth/signin")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(login)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken").exists())
                .andExpect(jsonPath("$.username").value("testuser"));
    }

    @Test
    @DisplayName("로그인 실패 - 잘못된 비밀번호")
    void testLoginFailureWrongPassword() throws Exception {
        userRepository.save(new User("testuser", "test@example.com", passwordEncoder.encode("password123")));

        LoginRequest login = new LoginRequest();
        login.setUsername("testuser");
        login.setPassword("wrongpassword");

        mockMvc.perform(post("/api/auth/signin")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(login)))
                .andExpect(status().isUnauthorized());
    }
}
