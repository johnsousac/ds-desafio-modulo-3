package com.devsuperior.bds04.configurations;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;

@Configuration
@EnableResourceServer
public class ResourceServerConfiguration extends ResourceServerConfigurerAdapter {
	
	@Autowired
	private Environment environment;

	@Autowired
	private JwtTokenConfiguration jwtTokenConfiguration;
	
	private static final String[] PUBLIC = { "/oauth/token", "/h2-console/**" };
	
	private static final String[] PUBLIC_GET = { "/cities/**", "/events/**" };
	
	private static final String[] CLIENT_POST = { "/events/**" };

	@Override
	public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
		resources.tokenStore(jwtTokenConfiguration.jwtTokenStore());
	}

	@Override
	public void configure(HttpSecurity http) throws Exception {
		
		if(Arrays.asList(environment.getActiveProfiles()).contains("test")) {
			http.headers().frameOptions().disable();
		}
		
		http.authorizeRequests()
			.antMatchers(HttpMethod.POST, CLIENT_POST).hasAnyRole("CLIENT", "ADMIN")
			.antMatchers(HttpMethod.GET, PUBLIC_GET).permitAll()
			.antMatchers(PUBLIC).permitAll()
			.anyRequest().hasRole("ADMIN");
	}

}
