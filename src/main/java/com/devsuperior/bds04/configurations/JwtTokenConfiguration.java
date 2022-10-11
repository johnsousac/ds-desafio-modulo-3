package com.devsuperior.bds04.configurations;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;

@Configuration
public class JwtTokenConfiguration {
	
	@Value("${jwt.secret}")
	private String jwtSecret;

	@Bean
	public JwtAccessTokenConverter jwtAccessTokenConverter() {
		JwtAccessTokenConverter token = new JwtAccessTokenConverter();
		token.setSigningKey(jwtSecret);
		return token;
	}
	
	@Bean
	public JwtTokenStore jwtTokenStore() {
		return new JwtTokenStore(jwtAccessTokenConverter());
	}
}
