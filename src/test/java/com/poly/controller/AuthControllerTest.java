package com.poly.controller;

import com.poly.entity.User;
import com.poly.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class AuthControllerTest {

    private MockMvc mockMvc;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private AuthController authController;

    @BeforeEach
    void setUp() {
        // Cấu hình ViewResolver để khắc phục triệt để lỗi Circular view path
        InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
        viewResolver.setPrefix("/templates/");
        viewResolver.setSuffix(".html");

        mockMvc = MockMvcBuilders.standaloneSetup(authController)
                .setViewResolvers(viewResolver)
                .build();
    }

    @Test
    @DisplayName("TC_AUTH_01: Hiển thị form đăng nhập")
    void testShowLoginForm() throws Exception {
        mockMvc.perform(get("/login"))
                .andExpect(status().isOk())
                .andExpect(view().name("login"));
    }

    @Test
    @DisplayName("TC_AUTH_02: Đăng nhập thành công với tài khoản chính xác")
    void testLoginSuccess() throws Exception {
        User user = new User(1L, "user01", "123456", "Nguyen Van A");
        when(userRepository.findByUsername("user01")).thenReturn(Optional.of(user));

        mockMvc.perform(post("/login")
                        .param("username", "user01")
                        .param("password", "123456"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/products"))
                .andExpect(request().sessionAttribute("loggedInUser", user));
    }

    @Test
    @DisplayName("TC_AUTH_03: Đăng nhập thất bại do sai mật khẩu")
    void testLoginFailedWrongPassword() throws Exception {
        User user = new User(1L, "user01", "123456", "Nguyen Van A");
        when(userRepository.findByUsername("user01")).thenReturn(Optional.of(user));

        mockMvc.perform(post("/login")
                        .param("username", "user01")
                        .param("password", "wrongpass"))
                .andExpect(status().isOk())
                .andExpect(view().name("login"))
                .andExpect(model().attributeExists("error"));
    }

    @Test
    @DisplayName("TC_AUTH_04: Đăng nhập thất bại khi tài khoản không tồn tại")
    void testLoginFailedUserNotFound() throws Exception {
        when(userRepository.findByUsername("unknown")).thenReturn(Optional.empty());

        mockMvc.perform(post("/login")
                        .param("username", "unknown")
                        .param("password", "123456"))
                .andExpect(status().isOk())
                .andExpect(view().name("login"))
                .andExpect(model().attributeExists("error"));
    }

    @Test
    @DisplayName("TC_AUTH_05: Hiển thị form đăng ký tài khoản")
    void testShowRegisterForm() throws Exception {
        mockMvc.perform(get("/register"))
                .andExpect(status().isOk())
                .andExpect(view().name("register"))
                .andExpect(model().attributeExists("user"));
    }

    @Test
    @DisplayName("TC_AUTH_06: Đăng ký thành công tài khoản mới")
    void testRegisterSuccess() throws Exception {
        when(userRepository.findByUsername("newuser")).thenReturn(Optional.empty());

        mockMvc.perform(post("/register")
                        .param("username", "newuser")
                        .param("password", "123456")
                        .param("fullName", "New User"))
                .andExpect(status().isOk())
                .andExpect(view().name("login"))
                .andExpect(model().attributeExists("success"));

        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    @DisplayName("TC_AUTH_07: Đăng ký thất bại do trùng tên đăng nhập")
    void testRegisterFailedUsernameExists() throws Exception {
        User existingUser = new User(1L, "existingUser", "123456", "Old User");
        when(userRepository.findByUsername("existingUser")).thenReturn(Optional.of(existingUser));

        mockMvc.perform(post("/register")
                        .param("username", "existingUser")
                        .param("password", "123456")
                        .param("fullName", "Existing User"))
                .andExpect(status().isOk())
                .andExpect(view().name("register"))
                .andExpect(model().attributeExists("error"));

        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    @DisplayName("TC_AUTH_08: Đăng xuất và xóa phiên làm việc")
    void testLogout() throws Exception {
        mockMvc.perform(get("/logout"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login"));
    }
}