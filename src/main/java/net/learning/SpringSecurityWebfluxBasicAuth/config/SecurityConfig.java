package net.learning.SpringSecurityWebfluxBasicAuth.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.userdetails.MapReactiveUserDetailsService;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.server.SecurityWebFilterChain;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {

    @Value("${app.security.username}")
    private String userName;

    @Value("${app.security.password}")
    private String password;

    @Value("${app.security.role}")
    private String role;


    @Bean
    public MapReactiveUserDetailsService userDetailsService() {

        UserDetails userDetails = User.builder()
                .username(userName)
                .password(password)
                .roles(role)
                .build();

        return new MapReactiveUserDetailsService(userDetails);
    }

    @Bean
    public PasswordEncoder getPasswordEncoder() {

        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String encodedPassword = passwordEncoder.encode(password);
        System.out.print("Encoded Password : "+encodedPassword);
        password = encodedPassword;
        return new BCryptPasswordEncoder(10);
    }

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {

        return http.csrf().disable()
                .authorizeExchange()
                .pathMatchers("/test/**").hasRole(role)
                .pathMatchers("/**").permitAll()
                .and().httpBasic()
                .and().build();
    }
}

