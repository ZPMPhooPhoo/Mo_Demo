package mo.demo.controller;

import lombok.RequiredArgsConstructor;
import mo.demo.model.User;
import mo.demo.repository.UserRepository;
import mo.demo.security.JwtUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@CrossOrigin(origins = "${cors.allowed-origins:*}")
public class AuthController {
    private static final Logger log = LoggerFactory.getLogger(AuthController.class);
    
    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    
    
    // Role-All: All authenticated users can register
    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody User registrationRequest) {
        if (userRepository.existsByEmail(registrationRequest.getEmail())) {
            
            log.warn("Registration attempt with existing email: " + registrationRequest.getEmail());
            return ResponseEntity.badRequest().body("Email is already in use");
        }
        
        User user = new User();
        user.setName(registrationRequest.getName());
        user.setEmail(registrationRequest.getEmail());
        user.setPassword(passwordEncoder.encode(registrationRequest.getPassword()));
        user.setRole(registrationRequest.getRole() != null ? registrationRequest.getRole() : "USER");
        user.setStatus("ACTIVE");
        
        User savedUser = userRepository.save(user);
        
        Map<String, Object> response = new HashMap<>();
        response.put("message", "User registered successfully");
        response.put("userId", savedUser.getId());
        response.put("email", savedUser.getEmail());
        response.put("role", savedUser.getRole());
        log.info("User registered: " + savedUser.getEmail() + " with role: " + savedUser.getRole());
        return ResponseEntity.ok(response);
    }
    
    // Role-All: All authenticated users can login
    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@RequestBody Map<String, String> loginRequest) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                    loginRequest.get("email"),
                    loginRequest.get("password")
                )
            );
            
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            String jwt = jwtUtil.generateToken(userDetails);
            
            User user = userRepository.findByEmail(loginRequest.get("email"))
                .orElseThrow(() -> new RuntimeException("User not found"));
            
            Map<String, Object> response = new HashMap<>();
            response.put("token", jwt);
            response.put("type", "Bearer");
            response.put("userId", user.getId());
            response.put("email", user.getEmail());
            response.put("name", user.getName());
            response.put("role", user.getRole());
            log.info("User logged in: " + user.getEmail() + " with role: " + user.getRole());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.warn("Login failed for email: " + loginRequest.get("email"), e);
            return ResponseEntity.badRequest().body("Invalid email or password");
        }
    }
    
    // Role-All: All authenticated users can view their profile
    @GetMapping("/profile")
    public ResponseEntity<?> getUserProfile(Authentication authentication) {
        User user = userRepository.findByEmail(authentication.getName())
            .orElseThrow(() -> new RuntimeException("User not found"));
        
        Map<String, Object> response = new HashMap<>();
        response.put("userId", user.getId());
        response.put("email", user.getEmail());
        response.put("name", user.getName());
        response.put("role", user.getRole());
        response.put("status", user.getStatus());
        response.put("createdAt", user.getCreated_at());
        log.info("User profile retrieved for: " + user.getEmail());
        return ResponseEntity.ok(response);
    }
}
