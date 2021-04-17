package com.example.springsecurity.config;

import com.example.springsecurity.service.UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.sql.DataSource;

@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final DataSource dataSource;
    private final UserService userService;

    public SecurityConfig(DataSource dataSource, UserService userService) {
        this.dataSource = dataSource;
        this.userService = userService;
    }

    @Bean
    CustomTokenFilter customTokenFilter(){
        return new CustomTokenFilter(userService);
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userService)
                .and()
                .jdbcAuthentication()
                .dataSource(dataSource);
//                .withDefaultSchema()
//                .withUser(
//                        User.withUsername("testUser")
//                        .password(passwordEncoder().encode("userPass"))
//                        .roles("USER")
//                ).withUser(
//                User.withUsername("testAdmin")
//                        .password(passwordEncoder().encode("adminPass"))
//                        .roles("ADMIN")
//        );
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(BCryptPasswordEncoder.BCryptVersion.$2B, 11);
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .addFilterBefore(customTokenFilter(), UsernamePasswordAuthenticationFilter.class)
                .authorizeRequests().antMatchers("/h2-console/**").permitAll()
                .anyRequest().authenticated();
//                .and()
//                .httpBasic();
        http.headers().frameOptions().disable(); // for test purposes
    }
}
