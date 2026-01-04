package mo.demo.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import mo.demo.model.User;
import mo.demo.repository.UserRepository;
import mo.demo.security.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureWebMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureWebMvc
public class AuthControllerTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    private MockMvc mockMvc;

    @MockBean
    private AuthenticationManager authenticationManager;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private PasswordEncoder passwordEncoder;

    @MockBean
    private JwtUtil jwtUtil;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    void testRegisterUser_Success() throws Exception {
        User user = new User();
        user.setId(java.util.UUID.randomUUID());
        user.setName("approver");
        user.setEmail("admin@gmail.com");
        user.setPassword("123456");
        user.setRole("APPROVER");

        when(userRepository.existsByEmail("admin@gmail.com")).thenReturn(false);
        when(passwordEncoder.encode("123456")).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(user);

        mockMvc.perform(post("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.message").value("User registered successfully"))
                .andExpect(jsonPath("$.email").value("admin@gmail.com"))
                .andExpect(jsonPath("$.role").value("APPROVER"))
                .andExpect(jsonPath("$.userId").value(user.getId().toString()));
    }

    @Test
    void testRegisterUser_EmailAlreadyExists() throws Exception {
        User user = new User();
        user.setEmail("admin@gmail.com");

        when(userRepository.existsByEmail("admin@gmail.com")).thenReturn(true);

        mockMvc.perform(post("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Email is already in use."));
    }

    @Test
    void testLoginUser_Success() throws Exception {
        User user = new User();
        user.setId(java.util.UUID.randomUUID());
        user.setEmail("admin@gmail.com");
        user.setName("approver");
        user.setRole("APPROVER");

        Authentication auth = org.mockito.Mockito.mock(Authentication.class);
        UserDetails userDetails = org.mockito.Mockito.mock(UserDetails.class);
        
        when(auth.getPrincipal()).thenReturn(userDetails);
        when(userRepository.findByEmail("admin@gmail.com")).thenReturn(Optional.of(user));
        when(authenticationManager.authenticate(any())).thenReturn(auth);
        when(jwtUtil.generateToken(userDetails)).thenReturn("mock-jwt-token");

        mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"email\":\"admin@gmail.com\",\"password\":\"123456\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("mock-jwt-token"))
                .andExpect(jsonPath("$.type").value("Bearer"))
                .andExpect(jsonPath("$.email").value("admin@gmail.com"))
                .andExpect(jsonPath("$.name").value("approver"))
                .andExpect(jsonPath("$.role").value("APPROVER"))
                .andExpect(jsonPath("$.userId").exists());
    }

    @Test
    void testLoginUser_InvalidCredentials() throws Exception {
        when(authenticationManager.authenticate(any()))
                .thenThrow(new RuntimeException("Invalid credentials"));

        mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"email\":\"admin@gmail.com\",\"password\":\"wrongpassword\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Invalid email or password."));
    }
}
