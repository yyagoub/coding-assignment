package com.coding.assignment.configuration.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class ApplicationSecurity extends WebSecurityConfigurerAdapter {

    @Autowired
    private SecurityService securityService;

    @Autowired
    private JwtUnAuthorizedResponseAuthenticationEntryPoint jwtUnAuthorizedResponseAuthenticationEntryPoint;

    @Autowired
    private JwtTokenAuthorizationOncePerRequestFilter jwtAuthenticationTokenFilter;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    private static final String[] AUTH_WHITELIST = {
            // authenticate
            "/authenticate"
            // -- swagger ui
            , "/swagger-resources/**", "/swagger-ui/**", "/v2/api-docs", "/webjars/**"
            // for testing
            //, "/**"
    };

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(securityService).passwordEncoder(passwordEncoder);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http //.cors().and() // enable ( Cross Origin Resource Sharing ) // moved to a separated class
                .csrf().disable() // disable CSRF
                .exceptionHandling().authenticationEntryPoint(jwtUnAuthorizedResponseAuthenticationEntryPoint).and()
                .authorizeRequests()
                .antMatchers(AUTH_WHITELIST).permitAll() // nabling URL to be accessed by any user (even un-authenticated)
                .anyRequest().authenticated() // Any resources (URLs) not mentioned above needs to be authenticated
                .and().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and().addFilterBefore(jwtAuthenticationTokenFilter, UsernamePasswordAuthenticationFilter.class);
        ;
    }
}
