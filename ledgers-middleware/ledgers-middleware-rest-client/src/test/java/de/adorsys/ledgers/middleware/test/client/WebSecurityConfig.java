package de.adorsys.ledgers.middleware.test.client;

import de.adorsys.ledgers.middleware.api.domain.um.AccessTokenTO;
import de.adorsys.ledgers.middleware.client.rest.AuthRequestInterceptor;
import de.adorsys.ledgers.middleware.rest.security.JWTAuthenticationFilter;
import de.adorsys.ledgers.middleware.rest.security.MiddlewareAuthentication;
import de.adorsys.ledgers.middleware.rest.security.TokenAuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.web.context.WebApplicationContext;

import java.security.Principal;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
	
	@Autowired
    private TokenAuthenticationService tokenAuthenticationService;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .authorizeRequests().antMatchers("/management/app/admin",
            		"/management/app/ping", 
            		"/users/login",
            		"/users/register",
                "/staff-access/users/register",
                "/staff-access/users/login")
                .permitAll()
            .and()
            .authorizeRequests().antMatchers("/v2/api-docs", "/swagger-resources", "/swagger-ui.html", "/webjars/**").permitAll()
            .and()
            .authorizeRequests().antMatchers("/console/**").permitAll()
            .and()
        	.authorizeRequests().anyRequest().authenticated();
        http.csrf().disable();
        http.headers().frameOptions().disable();
		http.addFilterBefore(new JWTAuthenticationFilter(tokenAuthenticationService), BasicAuthenticationFilter.class);
    }

	@Bean
    @Scope(scopeName = WebApplicationContext.SCOPE_REQUEST,proxyMode = ScopedProxyMode.TARGET_CLASS)
    public Principal getPrincipal() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication;
    }

	//	AccessTokenTO
	@Bean
    @Scope(scopeName = WebApplicationContext.SCOPE_REQUEST,proxyMode = ScopedProxyMode.TARGET_CLASS)
    public AccessTokenTO getAccessTokenTO() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(!(authentication instanceof MiddlewareAuthentication)){
        	return null;
        }
        MiddlewareAuthentication ma = (MiddlewareAuthentication) authentication;
        return ma.getBearerToken().getAccessTokenObject();
    }

	@Bean
	public AuthRequestInterceptor getClientAuth() {
		return new AuthRequestInterceptor();
	}
}

