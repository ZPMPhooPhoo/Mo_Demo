package mo.demo.config;

import lombok.RequiredArgsConstructor;
import mo.demo.security.JwtAuthFilter;
import mo.demo.service.CustomUserDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfigurationSource;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.hibernate6.Hibernate6Module;

import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    
    private final CustomUserDetailsService userDetailsService;
    private final JwtAuthFilter jwtAuthFilter;
    private final CorsConfigurationSource corsConfigurationSource;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
            .cors(cors -> cors.configurationSource(corsConfigurationSource))
            .csrf(AbstractHttpConfigurer::disable)
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(auth -> auth
                // Public endpoints
                .requestMatchers("/api/auth/**").permitAll()
                .requestMatchers("/login", "/register").permitAll()
                .requestMatchers("/css/**", "/js/**", "/images/**").permitAll()
                .requestMatchers("/error").permitAll()
                
                // USER endpoints
                .requestMatchers(HttpMethod.POST, "/api/tasks/create").hasAuthority("ROLE_USER")
                .requestMatchers(HttpMethod.POST, "/api/tasks/*/draft").hasAuthority("ROLE_USER")
                .requestMatchers(HttpMethod.PUT, "/api/tasks/*/submit").hasAuthority("ROLE_USER")
                .requestMatchers(HttpMethod.GET, "/api/tasks/task-by-user").hasAuthority("ROLE_USER")
                
                // APPROVER endpoints
                .requestMatchers(HttpMethod.PUT, "/api/tasks/*/approve").hasAuthority("ROLE_APPROVER")
                .requestMatchers(HttpMethod.PUT, "/api/tasks/*/reject").hasAuthority("ROLE_APPROVER")
                .requestMatchers(HttpMethod.PUT, "/api/tasks/all").hasAuthority("ROLE_APPROVER")
                
                // General authenticated endpoints
                .requestMatchers(HttpMethod.GET, "/api/tasks/submitted").authenticated()
                .requestMatchers(HttpMethod.GET, "/api/tasks/*").authenticated()
                .requestMatchers(HttpMethod.GET, "/api/tasks/status/*").authenticated()
                
                .anyRequest().authenticated()
            )
            .authenticationProvider(authenticationProvider())
            .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
            .build();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    ObjectMapper objectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new Hibernate6Module());
        return mapper;
    }

}
