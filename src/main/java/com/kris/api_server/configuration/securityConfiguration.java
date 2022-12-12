// package com.kris.api_server.configuration;

// import org.springframework.context.annotation.Bean;
// import org.springframework.context.annotation.Configuration;
// import org.springframework.http.HttpMethod;
// import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
// import org.springframework.security.config.annotation.web.builders.HttpSecurity;
// import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
// import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
// import org.springframework.security.config.http.SessionCreationPolicy;
// import org.springframework.security.core.userdetails.User;
// import org.springframework.security.core.userdetails.UserDetailsService;
// import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
// import org.springframework.security.crypto.password.PasswordEncoder;
// import org.springframework.security.provisioning.InMemoryUserDetailsManager;
// import org.springframework.security.web.SecurityFilterChain;

// @Configuration
// @EnableWebSecurity
// @EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true, jsr250Enabled = true)
// public class securityConfiguration {

//     @Bean
//     public PasswordEncoder passwordEncoder() {
//         return new BCryptPasswordEncoder();
//     }

//     @Bean
//     public UserDetailsService userDetailsService(PasswordEncoder PasswordEncoder) {
//         InMemoryUserDetailsManager manager = new InMemoryUserDetailsManager();
//         manager.createUser(
//                 User.withUsername("kris")
//                         .password(PasswordEncoder.encode("root"))
//                         .roles("ADMIN")
//                         .build());
//         return manager;
//     }

//     @Bean
//     public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
//         http.csrf().disable().authorizeRequests().antMatchers(HttpMethod.DELETE)
//                 .hasRole("ADMIN")
//                 .antMatchers("/users/**")
//                 .hasAnyRole("ADMIN")
//                 .antMatchers("/user/**")
//                 .hasAnyRole("USER", "ADMIN")
//                 .antMatchers("/login/**")
//                 .anonymous()
//                 .anyRequest()
//                 .authenticated()
//                 .and()
//                 .httpBasic()
//                 .and().formLogin().defaultSuccessUrl("/users/5");
//         // .and()
//         // .sessionManagement()
//         // .sessionCreationPolicy(SessionCreationPolicy.STATELESS);

//         return http.build();
//     }

//     @Bean
//     public WebSecurityCustomizer webSecurityCustomizer() {
//         return (web) -> web.debug(true)
//                 .ignoring()
//                 .antMatchers("/css/**", "/js/**", "/img/**", "/lib/**", "/favicon.ico");
//     }

// }
