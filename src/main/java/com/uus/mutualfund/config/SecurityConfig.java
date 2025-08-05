package com.uus.mutualfund.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import com.uus.mutualfund.enums.Role;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {

	private final UserDetailsService userDetailsService;

	public SecurityConfig(UserDetailsService userDetailsService) {
		this.userDetailsService = userDetailsService;
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		return http.csrf(csrf -> csrf.disable()).authorizeHttpRequests(auth -> auth
				.requestMatchers("/swagger-ui/**", "/swagger-ui.html", "/v3/api-docs/**", "/webjars/**").permitAll()
				.requestMatchers("/h2-console/**").permitAll().requestMatchers("/api/v1/users-profile/**").hasRole(Role.ADMIN.toString())
				.requestMatchers("/api/v1/mutual-funds/funds").hasRole(Role.ADMIN.toString())
				.requestMatchers("/api/v1/mutual-funds/buy").hasAnyRole(Role.ADMIN.toString(), Role.USER.toString())
				.requestMatchers("/api/v1/mutual-funds/redeem").hasAnyRole(Role.ADMIN.toString(), Role.USER.toString())
				.anyRequest().authenticated()).headers(headers -> headers.frameOptions(frame -> frame.disable()))
				.httpBasic(Customizer.withDefaults()).csrf(csrf -> csrf.disable()).build();
	}

	@Bean
	public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
		AuthenticationManagerBuilder builder = http.getSharedObject(AuthenticationManagerBuilder.class);
		builder.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
		return builder.build();
	}
}
